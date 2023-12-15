package com.example.gestiondecourrier.ui.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GmailResponse {

    private long id;
    private String recipient,subject, body;
    private List<String> fileName;
    private List<byte[]> bytes;
    private int positionInTheViewModel;

    public GmailResponse() {
    }

    public GmailResponse(long id, String recipient, String subject, String body, List<String> fileName, List<byte[]> bytes) {
        this.id = id;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<String> getFileName() {
        return fileName;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    public List<byte[]> getBytes() {
        return bytes;
    }

    public void setBytes(List<byte[]> bytes) {
        this.bytes = bytes;
    }

    public int getPositionInTheViewModel() {
        return positionInTheViewModel;
    }

    public void setPositionInTheViewModel(int positionInTheViewModel) {
        this.positionInTheViewModel = positionInTheViewModel;
    }
}
