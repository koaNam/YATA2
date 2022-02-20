package de.koanam.yata2.streamtokenizer.model;

import java.time.LocalDate;

public class Document {
    private Long id;
    private String title;
    private String publication;
    private String author;
    private LocalDate date;
    private String content;

    public Document() {
    }

    public Document(Long id, String title, String publication, String author, String date, String content) {
        this.id = id;
        this.title = title;
        this.publication = publication;
        this.author = author;
        this.setDate(date);
        this.content = content;
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

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
