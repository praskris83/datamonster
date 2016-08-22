package indix.datamonster;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import indix.datamonster.kafka.KafkaElasticSink;

/**
 * @author prasad
 */
@SpringBootApplication
public class Application {

    @Autowired
    private KafkaElasticSink kafkaElasticSink;

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
//		DataProcessor.process();

//        JestClientFactory factory = new JestClientFactory();
//        factory.setHttpClientConfig(new HttpClientConfig.Builder("http://82a5c9dc304ca43e36e011674d860e61.us-east-1.aws.found.io:9200")
//                .defaultCredentials("admin", "admin")
//                .multiThreaded(true)
//                .build());
//        TransportClient client = factory.getObject();
//        String query = "";
//        Search.Builder searchBuilder = new Search.Builder(query).addIndex("products").addType("catalog");
//        SearchResult result = client.execute(searchBuilder.build());
//        System.out.println(result.getJsonString());
//
//        final Index index = new Index.Builder("").index("products").type("catalog").build();
//        client.execute(index);

        final Application application = ctx.getBean(Application.class);
//        application.execute();

    }

    public void execute() {
//        String zooKeeper = "192.168.0.210:2181";
//        String groupId = UUID.randomUUID().toString();
//        String topic = "datamonster_prices";
//        int threads = 2; // change as appropriate

//        KafkaElasticSink kafkaElasticSink = new KafkaElasticSink(zooKeeper, groupId, topic, threads);
        kafkaElasticSink.run(kafkaElasticSink.getNoOfThreads());

        try {
            Thread.sleep(100000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        kafkaElasticSink.shutdown();
    }
}
