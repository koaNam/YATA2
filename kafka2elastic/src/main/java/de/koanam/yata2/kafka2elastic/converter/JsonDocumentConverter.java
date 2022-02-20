package de.koanam.yata2.kafka2elastic.converter;

import com.google.common.hash.Hashing;
import de.koanam.yata2.kafka2elastic.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;

public class JsonDocumentConverter implements DocumentConverter {

    private static final Logger LOG = LoggerFactory.getLogger(JsonDocumentConverter.class);


    @Override
    public Document parseDocument(HashMap<String, Object> hashMap) {
        LOG.trace("Parsing document");

        Document document = new Document();
        document.setId((Long) hashMap.get("id"));
        document.setTitle((String) hashMap.get("title"));
        document.setPublication((String) hashMap.get("publication"));
        document.setAuthor((String) hashMap.get("author"));
        String date = (String) hashMap.get("date");
        document.setDate(LocalDate.parse(date));
        document.setContent((String) hashMap.get("content"));
        return document;
    }

    @Override
    public String calculateId(Document document) {
        String id = document.getTitle() + document.getAuthor() + document.getDate();
        String sha256hex = Hashing.sha256().hashString(id, StandardCharsets.UTF_8).toString();
        return sha256hex;
    }
}
