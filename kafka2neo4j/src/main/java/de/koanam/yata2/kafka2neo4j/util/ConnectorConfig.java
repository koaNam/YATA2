package de.koanam.yata2.kafka2neo4j.util;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;

public class ConnectorConfig {

    public static final String NEO4J_HOST = "neo4j.host";
    public static final String NEO4J_USERNAME= "neo4j.username";
    public static final String NEO4J_PASSWORD = "neo4j.password";

    public static final ConfigDef CONFIG = new ConfigDef()
            .define(NEO4J_HOST, Type.STRING, ConfigDef.Importance.HIGH, "The Neo4j host")
            .define(NEO4J_USERNAME, Type.STRING, ConfigDef.Importance.HIGH, "The Neo4j username")
            .define(NEO4J_PASSWORD, Type.PASSWORD, ConfigDef.Importance.HIGH, "The Neo4j password");

}
