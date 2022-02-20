package de.koanam.yata2.streamtokenizer.model;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TokenizedResult {

    private String id;
    private String title;
    private List<String> tokens;
    private LocalDate date;
    private LocalDateTime timestamp;

    public TokenizedResult() {
    }

    public TokenizedResult(String title, List<String> tokens, String date, LocalDateTime timestamp) {
        this.title = title;
        this.tokens = tokens;
        this.setDate(date);
        this.timestamp = timestamp;
    }

    public String getId() {
        return this.calculateId();
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
        return date.toString();
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = LocalDateTime.parse(timestamp);
    }

    public String calculateId() {
        String id = this.getTitle() + null + this.getDate();
        String sha256hex = Hashing.sha256().hashString(id, StandardCharsets.UTF_8).toString();
        return sha256hex;
    }


}
