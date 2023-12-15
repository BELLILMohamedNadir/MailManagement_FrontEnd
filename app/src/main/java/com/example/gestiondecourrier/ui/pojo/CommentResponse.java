package com.example.gestiondecourrier.ui.pojo;

public class CommentResponse {

    private long id;
    private String comment,name,firstName,date;
    private byte[] bytes;
    private long userId;

    public CommentResponse() {
    }

    public CommentResponse(String comment) {
        this.comment = comment;
    }

    public CommentResponse(long id, String comment, String name, String firstName, String date, byte[] bytes, long user8id) {
        this.id = id;
        this.comment = comment;
        this.name = name;
        this.firstName = firstName;
        this.date = date;
        this.bytes = bytes;
        this.userId = user8id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
