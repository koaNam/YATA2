package de.koanam.yata2.kafka2elastic.util;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;;

public class ConnectorConfig {

    public static final String ELASTIC_HOST = "elastic.host";
    public static final String ELASTIC_USERNAME= "elastic.username";
    public static final String ELASTIC_PASSWORD = "elastic.password";
    public static final String ELASTIC_INDEX = "elastic.index";

    public static final ConfigDef CONFIG = new ConfigDef()
            .define(ELASTIC_HOST, Type.STRING, ConfigDef.Importance.HIGH, "The ElasticSearch host")
            .define(ELASTIC_USERNAME, Type.STRING, ConfigDef.Importance.HIGH, "The ElasticSearch username")
            .define(ELASTIC_PASSWORD, Type.PASSWORD, ConfigDef.Importance.HIGH, "The ElasticSearch password")
            .define(ELASTIC_INDEX, Type.STRING, ConfigDef.Importance.HIGH, "The ElasticSearch index");

}
