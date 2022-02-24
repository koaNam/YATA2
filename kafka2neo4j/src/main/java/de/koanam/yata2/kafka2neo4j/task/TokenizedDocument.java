package de.koanam.yata2.kafka2neo4j.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TokenizedDocument {

    private String id;
    private String title;
    private List<String> tokens;
    private LocalDate date;
    private LocalDateTime timestamp;

    public TokenizedDocument() {
    }

    public TokenizedDocument(String id, String title, List<String> tokens, LocalDate date, LocalDateTime timestamp) {
        this.id = id;
        this.title = title;
        this.tokens = tokens;
        this.date = date;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public String getDate() {
        return this.date.toString();
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public String getTimestamp() {
        return this.timestamp.toString();
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = LocalDateTime.parse(timestamp);
    }
}
