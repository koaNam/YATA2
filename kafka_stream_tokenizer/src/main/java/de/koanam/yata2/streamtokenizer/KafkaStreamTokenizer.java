package de.koanam.yata2.streamtokenizer;

import de.koanam.yata2.streamtokenizer.service.TfidfTokenizerGRPCService;
import de.koanam.yata2.streamtokenizer.service.TokenizerKafkaStream;
import de.koanam.yata2.streamtokenizer.service.TokenizerStream;
import de.koanam.yata2.streamtokenizer.util.Config;
import de.koanam.yata2.streamtokenizer.util.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class KafkaStreamTokenizer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamTokenizer.class);

    public static void main(String[] args) {
        Properties properties = null;
        try {
            ConfigReader configReader = new ConfigReader();
            properties = configReader.readConfig(args[0]);
        } catch (IOException e) {
            LOG.error("Error while reading config from {}", args[0]);
            System.exit(1);
        }

        String host = (String) properties.get(Config.GRPC_HOST);
        int port = Integer.valueOf((String)properties.get(Config.GRPC_PORT));
        TfidfTokenizerGRPCService service = new TfidfTokenizerGRPCService(host, port);

        TokenizerStream tokenizerStream = new TokenizerKafkaStream();
        tokenizerStream.init(service, properties);
        tokenizerStream.start();

    }

}
