package indix.datamonster.handlers;

import com.google.common.base.Preconditions;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.shield.ShieldPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by sathsrinivasan on 8/20/2016.
 */
@Service
public class TransportClient {
    final String hostname = "86106386ef1d4352afecbfbe330f2227.us-east-1.aws.found.io";
    final String clusterId = "86106386ef1d4352afecbfbe330f2227"; // Your cluster ID here
    final String region = "us-east-1";

    @Bean(name = "searchBoxJestClient")
    public io.searchbox.client.JestClient getClient() {
        final JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder("http://86106386ef1d4352afecbfbe330f2227.us-east-1.aws.found.io:9200")
                .defaultCredentials("admin", "admin")
                .multiThreaded(true)
                .build());
        int timeout = 5;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
//        JestHttpClient client = (JestHttpClient) factory.getObject();
//        final CloseableHttpClient httpClient = client.getHttpClient();
//        httpClient.
//        final CloseableHttpAsyncClient asyncClient = client.getAsyncClient();
//        asyncClient
        return factory.getObject();

    }

    public void indexDoc(String document) throws IOException {
        final Index index = new Index.Builder(document).index("products").type("catalog").build();
        getClient().execute(index);
    }

    public void bulkIndex(List<String> documents) throws IOException {
        if (documents != null && !documents.isEmpty()) {
            System.out.println("Indexing docs : " + documents.size());
            final Bulk.Builder bulkBuilder = new Bulk.Builder();
            for (String document : documents) {
                bulkBuilder.addAction(new Index.Builder(document).index("products").type("catalog").build());
            }
            getClient().execute(bulkBuilder.build());
        }
    }

    public void shutdown() {
        getClient().shutdownClient();
    }

    @Bean
    public Client getElasticClient() {
        // Build the settings for our client.
        // Your region here
        boolean enableSsl = true;

        Settings settings = Settings.settingsBuilder()
                .put("transport.ping_schedule", "5s")
                //.put("transport.sniff", false) // Disabled by default and *must* be disabled.
                .put("cluster.name", clusterId)
                .put("shield.user", "admin:admin")
                .put("action.bulk.compress", false)
                .put("shield.transport.ssl", enableSsl)
                .put("request.headers.X-Found-Cluster", clusterId)
                .build();

        String hostname = clusterId + "." + region + ".aws.found.io";
        Client client = null;
        try {

            client = org.elasticsearch.client.transport.TransportClient.builder().addPlugin(ShieldPlugin.class)
                    .settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(
                            InetAddress.getByName(hostname), 9343));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        return client;
    }

    public <T> void bulkMetricsPush(List<T> jsonDocuments, String elasticIndex, String elasticType) {
        Preconditions.checkArgument(jsonDocuments != null, "Json document received to be persisted into Elastic should not be null");
        final Client elasticClient = getElasticClient();
        try {
            System.out.println("Before posting " + jsonDocuments.size() + " documents into elastic : " + elasticClient.settings().names());
            final BulkRequestBuilder bulkRequestBuilder = elasticClient.prepareBulk();
            for (T document : jsonDocuments) {
                bulkRequestBuilder.add(elasticClient.prepareIndex(elasticIndex,
                        elasticType).setSource(document.toString()));
            }
            final BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                throw new IllegalStateException
                        ("Failure occured while doing bulk load into Elastic : " + bulkResponse.buildFailureMessage());
            } else {
                System.out.println("Bulk response took : " + bulkResponse.getTook());
            }
        } finally {
            elasticClient.close();
        }
    }
}
