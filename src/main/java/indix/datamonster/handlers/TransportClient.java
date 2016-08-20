package indix.datamonster.handlers;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by sathsrinivasan on 8/20/2016.
 */
@Service
public class TransportClient {

    @Bean(name = "searchBoxJestClient")
    public io.searchbox.client.JestClient getClient() {
        final JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder("http://82a5c9dc304ca43e36e011674d860e61.us-east-1.aws.found.io:9200")
                .defaultCredentials("admin", "admin")
                .multiThreaded(true)
                .build());
        return factory.getObject();

    }

    public void indexDoc(String document) throws IOException {
        final Index index = new Index.Builder(document).index("products").type("catalog").build();
        getClient().execute(index);
    }
}
