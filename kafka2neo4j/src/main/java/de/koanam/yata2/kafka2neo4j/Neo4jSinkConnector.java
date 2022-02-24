package de.koanam.yata2.kafka2neo4j;

import de.koanam.yata2.kafka2neo4j.task.Neo4jSinkTask;
import de.koanam.yata2.kafka2neo4j.util.ConnectorConfig;
import de.koanam.yata2.kafka2neo4j.util.ProjectInfo;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.types.Password;
import org.apache.kafka.connect.connector.Connector;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neo4jSinkConnector extends SinkConnector {

    private String host;
    private String username;
    private Password password;

    @Override
    public String version() {
        return ProjectInfo.version();
    }

    @Override
    public Class<? extends Task> taskClass() {
        return Neo4jSinkTask.class;
    }

    @Override
    public void start(Map<String, String> properties) {
        AbstractConfig parsedConfig = new AbstractConfig(ConnectorConfig.CONFIG, properties);
        host = parsedConfig.getString(ConnectorConfig.NEO4J_HOST);
        username = parsedConfig.getString(ConnectorConfig.NEO4J_USERNAME);
        password = parsedConfig.getPassword(ConnectorConfig.NEO4J_PASSWORD);
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> configs = new ArrayList<>(maxTasks);
        for(int i=0; i < maxTasks; i++){
            Map<String, String> config = new HashMap<>();
            config.put(ConnectorConfig.NEO4J_HOST, host);
            config.put(ConnectorConfig.NEO4J_USERNAME, username);
            config.put(ConnectorConfig.NEO4J_PASSWORD, password.value());
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
