package de.koanam.yata2.kafka2neo4j.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.koanam.yata2.kafka2neo4j.task.TokenizedDocument;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import java.util.HashMap;
import java.util.Map;

public class TokenizedDocumentNeo4jDAO implements TokenizedDocumentDAO {

    private static final String CREATE_QUERY = "CREATE (d:Document) set d = $document";
    private static final String CREATE_RELATIONSHIP = "MATCH (source:Document{title:$title}), (dest:Document) " +
            " WHERE dest <> source AND " +
            " $token in dest.tokens " +
            " MERGE (source)-[r:RELATES_TO{token: $token}]-(dest) " +
            " return source,dest";

    private Driver driver;
    private ObjectMapper jsonMapper;

    public TokenizedDocumentNeo4jDAO(Driver driver){
        this.driver = driver;
        this.jsonMapper =  new ObjectMapper();
    }

    @Override
    public void createTokenizedDocument(TokenizedDocument tokenizedDocument) {
        try(Session session = driver.session()){
            Map<String, Object> params = new HashMap<>();
            params.put("document", this.jsonMapper.convertValue(tokenizedDocument, HashMap.class));
            session.writeTransaction(t -> t.run(CREATE_QUERY, params));
        }
    }

    @Override
    public void createRelationship(TokenizedDocument tokenizedDocument, String token) {
        try(Session session = driver.session()){
            Map<String, Object> params = new HashMap<>();
            params.put("title", tokenizedDocument.getTitle());
            params.put("token", token);
            session.writeTransaction(t -> t.run(CREATE_RELATIONSHIP, params));
        }
    }
}
