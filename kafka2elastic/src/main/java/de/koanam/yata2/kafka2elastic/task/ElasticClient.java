package de.koanam.yata2.kafka2elastic.task;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import de.koanam.yata2.kafka2elastic.Document;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

public class ElasticClient {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticClient.class);

    private String host;
    private String username;
    private String password;
    private String index;

    private ElasticsearchClient client;

    public ElasticClient(String host, String username, String password, String index) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.index = index;
    }

    public void connect() {
        LOG.info("Trying to connect to {}", host);

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        RestClientBuilder restClientBuilder = RestClient.builder(HttpHost.create(host));
        if (username != null && password != null) {
            LOG.info("Using username and password to authenticate");
            restClientBuilder.setHttpClientConfigCallback(
                    httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        ElasticsearchTransport transport = new RestClientTransport(restClientBuilder.build(), new JacksonJsonpMapper());
        this.client = new ElasticsearchClient(transport);
        LOG.info("Connection successful");
    }

    public void insertDocument(String id, Document document, LocalDateTime timestamp) throws IOException {
        LOG.trace("Trying to insert document with ID {} to index {}", id, this.index);
        document.setTimestamp(timestamp);
        client.index(c -> c.index(index).id(id).document(document));
        LOG.trace("Inserted document with ID {} successfully", id);
    }

}
