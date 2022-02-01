package de.koanam.yata2.kafka2elastic.converter;

import de.koanam.yata2.kafka2elastic.Document;

import java.util.HashMap;

public interface DocumentConverter {

    Document parseDocument(HashMap<String, Object> hashMap);

    String calculateId(Document document);

}
