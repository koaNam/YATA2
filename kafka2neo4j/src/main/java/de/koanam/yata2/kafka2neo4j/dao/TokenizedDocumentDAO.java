package de.koanam.yata2.kafka2neo4j.dao;

import de.koanam.yata2.kafka2neo4j.task.TokenizedDocument;

public interface TokenizedDocumentDAO {

    void createTokenizedDocument(TokenizedDocument tokenizedDocument);

    void createRelationship(TokenizedDocument tokenizedDocument, String token);
}
