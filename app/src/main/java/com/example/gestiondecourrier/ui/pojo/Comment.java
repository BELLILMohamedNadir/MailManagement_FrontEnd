package com.example.gestiondecourrier.ui.pojo;

import java.util.Date;

public class Comment {

    private long id;
    private String comment;
    private String date;

    private Mail pdf;
    private User user;

    public Comment() {
    }

    public Comment(long id, String comment, String date, Mail pdf, User user) {
        this.id = id;
        this.comment = comment;
        this.date=date;
        this.pdf = pdf;
        this.user = user;
    }

    public Comment(String comment, String date, Mail pdf, User user) {
        this.comment = comment;

        this.date=date;
        this.pdf = pdf;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Mail getPdf() {
        return pdf;
    }

    public void setPdf(Mail pdf) {
        this.pdf = pdf;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
