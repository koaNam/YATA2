package de.koanam.yata2.kafka2elastic;

import de.koanam.yata2.kafka2elastic.task.ElasticSinkTask;
import de.koanam.yata2.kafka2elastic.util.ConnectorConfig;
import de.koanam.yata2.kafka2elastic.util.ProjectInfo;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.types.Password;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticSinkConnector extends SinkConnector {

    private String elasticHost;
    private Password elasticUsername;
    private String elasticPassword;
    private String elasticIndex;

    @Override
    public String version() {
        return ProjectInfo.version();
    }

    @Override
    public Class<? extends Task> taskClass() {
        return ElasticSinkTask.class;
    }

    @Override
    public void start(Map<String, String> properties) {
       AbstractConfig parsedConfig = new AbstractConfig(ConnectorConfig.CONFIG, properties);
       elasticHost = parsedConfig.getString(ConnectorConfig.ELASTIC_HOST);
       elasticUsername = parsedConfig.getPassword(ConnectorConfig.ELASTIC_PASSWORD);
       elasticPassword = parsedConfig.getString(ConnectorConfig.ELASTIC_USERNAME);
       elasticIndex = parsedConfig.getString(ConnectorConfig.ELASTIC_INDEX);
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> configs = new ArrayList<>(maxTasks);
        for(int i=0; i < maxTasks; i++){
            Map<String, String> config = new HashMap<>();
            config.put(ConnectorConfig.ELASTIC_HOST, elasticHost);
            config.put(ConnectorConfig.ELASTIC_PASSWORD, elasticPassword);
            config.put(ConnectorConfig.ELASTIC_USERNAME, elasticUsername.value());
            config.put(ConnectorConfig.ELASTIC_INDEX, elasticIndex);
            configs.add(config);
        }
        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return ConnectorConfig.CONFIG;
    }


}
