package de.koanam.yata2.kafka2neo4j.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.koanam.yata2.kafka2neo4j.dao.TokenizedDocumentDAO;
import de.koanam.yata2.kafka2neo4j.dao.TokenizedDocumentNeo4jDAO;
import de.koanam.yata2.kafka2neo4j.util.ConnectorConfig;
import de.koanam.yata2.kafka2neo4j.util.ProjectInfo;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Neo4jSinkTask extends SinkTask {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jSinkTask.class);
    private ObjectMapper jsonMapper;
    private TokenizedDocumentPersister persister;

    @Override
    public String version() {
        return ProjectInfo.version();
    }

    @Override
    public void start(Map<String, String> properties) {
        this.jsonMapper =  new ObjectMapper();
        this.persister = this.createPersister(properties);
    }

    private TokenizedDocumentPersister createPersister(Map<String, String> properties) {
        TokenizedDocumentDAO dao = this.initConnection(properties);
        return new TokenizedDocumentPersister(dao);
    }

    private TokenizedDocumentNeo4jDAO initConnection(Map<String, String> properties) {
        String host = properties.get(ConnectorConfig.NEO4J_HOST);
        String username = properties.get(ConnectorConfig.NEO4J_USERNAME);
        String password = properties.get(ConnectorConfig.NEO4J_PASSWORD);
        Driver driver = GraphDatabase.driver(host, AuthTokens.basic(username, password));

        return new TokenizedDocumentNeo4jDAO(driver);
    }

    @Override
    public void put(Collection<SinkRecord> sinkRecords) {
        LOG.info("Received {} messages from kafka", sinkRecords.size());
        int successCounter = 0;
        for (SinkRecord record : sinkRecords) {
            HashMap<String, Object> value = (HashMap<String, Object>) record.value();
            final TokenizedDocument document = jsonMapper.convertValue(value, TokenizedDocument.class);
            this.persister.persist(document);

            successCounter++;
        }
        LOG.info("Wrote {} messages successful to elastic", successCounter);
    }

    @Override
    public void stop() {

    }
}
