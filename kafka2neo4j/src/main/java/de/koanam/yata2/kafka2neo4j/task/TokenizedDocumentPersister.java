package de.koanam.yata2.kafka2neo4j.task;

import de.koanam.yata2.kafka2neo4j.dao.TokenizedDocumentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenizedDocumentPersister {

    private static final Logger LOG = LoggerFactory.getLogger(TokenizedDocumentPersister.class);

    private TokenizedDocumentDAO tokenizedDocumentDAO;

    public TokenizedDocumentPersister(TokenizedDocumentDAO tokenizedDocumentDAO){
        this.tokenizedDocumentDAO = tokenizedDocumentDAO;
    }

    public void persist(TokenizedDocument document){
        LOG.trace("Trying to insert document with ID {}", document.getId());
        this.tokenizedDocumentDAO.createTokenizedDocument(document);
        for(String token: document.getTokens()){
            this.tokenizedDocumentDAO.createRelationship(document, token);
        }
        LOG.trace("Inserted document with ID {} successfully", document.getId());
    }



}
