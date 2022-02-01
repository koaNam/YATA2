package de.koanam.yata2.kafka2elastic;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Document {

    private Long id;
    private String title;
    private String publication;
    private String author;
    private LocalDate date;
    private String content;
    private LocalDateTime timestamp;

    public Document() {
    }

    public Document(Long id, String title, String publication, String author, LocalDate date, String content, LocalDateTime timestamp) {
        this.id = id;
        this.title = title;
        this.publication = publication;
        this.author = author;
        this.date = date;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publication='" + publication + '\'' +
                ", author='" + author + '\'' +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
