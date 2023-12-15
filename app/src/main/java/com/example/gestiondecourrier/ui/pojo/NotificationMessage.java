package com.example.gestiondecourrier.ui.pojo;

public class NotificationMessage {

    private String recipient,title,body;

    public NotificationMessage() {
    }

    public NotificationMessage(String recipient, String title, String body) {
        this.recipient = recipient;
        this.title = title;
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
