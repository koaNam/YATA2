package de.koanam.yata2.kafka2neo4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ProjectInfo {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectInfo.class);

    private static final String VERSION_FILE = "./application.properties";
    private static String VERSION;

    static {
        try {
            Properties props = new Properties();
            try (InputStream versionFileStream = ProjectInfo.class.getResourceAsStream(VERSION_FILE)) {
                props.load(versionFileStream);
                VERSION = props.getProperty("version").trim();
            }
        } catch (Exception e) {
            VERSION = "unknown";
            LOG.error("Error while loading version, setting it to 'unknown'", e);
        }
    }

    public static String version(){
        return VERSION;
    }

}
