package indix.datamonster.kafka;

import indix.datamonster.handlers.TransportClient;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathsrinivasan on 8/20/2016.
 */
@Component
public class KafkaElasticSink extends Consumer {

    @Autowired
    private TransportClient transportClient;

    @Autowired
    public KafkaElasticSink(@Value("192.168.0.210:2181") String a_zookeeper,
                            @Value("SAMPLE_GROUP_ID") String a_groupId,
                            @Value("datamonster_prices") String a_topic,
                            @Value("2") int noOfThreads) {
        super(a_zookeeper, a_groupId, a_topic, noOfThreads);
    }

    @Override
    public void run(int noOfThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(noOfThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ElasticSinkRunnable(stream, threadNumber));
            threadNumber++;
        }
    }

    class ElasticSinkRunnable implements Runnable {
        private KafkaStream m_stream;
        private int m_threadNumber;

        public ElasticSinkRunnable(KafkaStream a_stream, int a_threadNumber) {
            m_threadNumber = a_threadNumber;
            m_stream = a_stream;
        }

        int rowCnt = 0;

        public void run() {
            System.out.println("run thread starting");
            ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
            while (it.hasNext()) {
                rowCnt++;
                final String document = new String(it.next().message());
                try {
                    if (!document.contains("data")) {
//                        System.out.println(document);
                        transportClient.indexDoc(document);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Shutting down Thread: " + m_threadNumber);
            System.out.println("Row Cnt for Thread - " + m_threadNumber + " is " + rowCnt);
        }
    }
}
