package com.example.gestiondecourrier.ui.pojo;

public class Gmail {

    private User user;
    private String recipient,subject, body;

    public Gmail() {
    }

    public Gmail(User user, String recipient, String subject, String body) {
        this.user = user;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
