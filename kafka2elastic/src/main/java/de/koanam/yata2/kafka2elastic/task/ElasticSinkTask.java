package de.koanam.yata2.kafka2elastic.task;

import de.koanam.yata2.kafka2elastic.Document;
import de.koanam.yata2.kafka2elastic.converter.DocumentConverter;
import de.koanam.yata2.kafka2elastic.converter.JsonDocumentConverter;
import de.koanam.yata2.kafka2elastic.util.ConnectorConfig;
import de.koanam.yata2.kafka2elastic.util.ProjectInfo;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ElasticSinkTask extends SinkTask {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSinkTask.class);

    private ElasticClient client;
    private DocumentConverter converter;

    @Override
    public String version() {
        return ProjectInfo.version();
    }

    @Override
    public void start(Map<String, String> properties) {
        this.converter = new JsonDocumentConverter();

        this.client = new ElasticClient(
                properties.get(ConnectorConfig.ELASTIC_HOST),
                properties.get(ConnectorConfig.ELASTIC_USERNAME),
                properties.get(ConnectorConfig.ELASTIC_PASSWORD),
                properties.get(ConnectorConfig.ELASTIC_INDEX)
        );
        client.connect();
    }

    @Override
    public void put(Collection<SinkRecord> sinkRecords) {
        LOG.info("Received {} messages from kafka", sinkRecords.size());
        int successCounter = 0;
        for (SinkRecord record : sinkRecords) {
            HashMap<String, Object> value = (HashMap<String, Object>) record.value();
            Document document = this.converter.parseDocument(value);
            String id = this.converter.calculateId(document);
            try {
                client.insertDocument(id, document, LocalDateTime.now());
            } catch (IOException e) {
                LOG.error("Encountered Error. Wrote only {} of {} messages successful", successCounter, sinkRecords.size());
                throw new ConnectException("Error while writing document with id " + id + " to elastic", e);
            }
            successCounter++;
        }
        LOG.info("Wrote {} messages successful to elastic", successCounter);
    }

    @Override
    public void stop() {

    }
}
