package de.koanam.yata2.streamtokenizer.service;

import de.koanam.yata2.streamtokenizer.model.Document;
import de.koanam.yata2.streamtokenizer.model.JsonPOJODeserializer;
import de.koanam.yata2.streamtokenizer.model.JsonPOJOSerializer;
import de.koanam.yata2.streamtokenizer.model.TokenizedResult;
import de.koanam.yata2.streamtokenizer.util.Config;
import de.koanam.yata2.streamtokenizer.util.ServiceException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TokenizerKafkaStream implements TokenizerStream {

    private TfidfTokenizerService tokenizerService;

    private Properties properties;
    private String sourceTopic;
    private String destinationTopic;
    private int tokenCount;


    @Override
    public void init(TfidfTokenizerService tokenizer, Properties props) {
        this.tokenizerService = tokenizer;
        this.properties = new Properties();
        this.properties.put(StreamsConfig.APPLICATION_ID_CONFIG, props.get(Config.KAFKA_APPLICATION_ID));
        this.properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, props.get(Config.BOOTSTRAP_SERVERS_CONFIG));

        this.sourceTopic = (String) props.get(Config.SOURCE_TOPIC);
        this.destinationTopic = (String) props.get(Config.DESTINATION_TOPIC);
        this.tokenCount = Integer.valueOf((String)props.get(Config.TOKEN_COUNT));
    }

    @Override
    public void start() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Document> documents = builder.stream(this.sourceTopic, Consumed.with(Serdes.String(), buildConsumerSerde()));
        documents.mapValues(v -> {
            try {
                List<String> tokens = this.tokenizerService.transform(v.getContent(), this.tokenCount);
                return new TokenizedResult(v.getTitle(), tokens, v.getDate(), LocalDateTime.now());
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        }).to(this.destinationTopic, Produced.with(Serdes.String(), buildProducerSerde()));


        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.cleanUp();
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private Serde<Document> buildConsumerSerde(){
        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<Document> pageViewSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", Document.class);
        pageViewSerializer.configure(serdeProps, false);

        final Deserializer<Document> pageViewDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", Document.class);
        pageViewDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(pageViewSerializer, pageViewDeserializer);
    }

    private Serde<TokenizedResult> buildProducerSerde(){
        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<TokenizedResult> pageViewSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", TokenizedResult.class);
        pageViewSerializer.configure(serdeProps, false);

        final Deserializer<TokenizedResult> pageViewDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", TokenizedResult.class);
        pageViewDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(pageViewSerializer, pageViewDeserializer);
    }

}
