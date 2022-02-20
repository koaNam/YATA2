package de.koanam.yata2.streamtokenizer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigReader.class);

    public Properties readConfig(String config) throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(config)) {
            Properties prop = new Properties();

            prop.load(input);
            LOG.info("Read {} properties", prop.size());

            return prop;
        }
    }

}
