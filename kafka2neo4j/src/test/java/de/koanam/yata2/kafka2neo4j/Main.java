package de.koanam.yata2.kafka2neo4j;

import de.koanam.yata2.kafka2neo4j.dao.TokenizedDocumentDAO;
import de.koanam.yata2.kafka2neo4j.dao.TokenizedDocumentNeo4jDAO;
import de.koanam.yata2.kafka2neo4j.task.TokenizedDocument;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
        TokenizedDocumentDAO dao =  new TokenizedDocumentNeo4jDAO(driver);

        TokenizedDocument d1 = new TokenizedDocument("id1", "title1", Arrays.asList("TOKENA1", "TOKENA2"), LocalDate.now(), LocalDateTime.now());
        dao.createTokenizedDocument(d1);
        TokenizedDocument d2 = new TokenizedDocument("id2", "title2", Arrays.asList("TOKENA1", "TOKENA3"), LocalDate.now(), LocalDateTime.now());
        dao.createTokenizedDocument(d2);
        TokenizedDocument d3 = new TokenizedDocument("id3", "title3", Arrays.asList("TOKENA1", "TOKENB1"), LocalDate.now(), LocalDateTime.now());
        //dao.createTokenizedDocument(d3);

        //dao.createRelationship(d3, "TOKENA1");

    }

}
