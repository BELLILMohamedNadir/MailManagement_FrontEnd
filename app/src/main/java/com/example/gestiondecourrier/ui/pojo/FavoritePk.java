package com.example.gestiondecourrier.ui.pojo;

public class FavoritePk {

    private long mailId,userId;

    public FavoritePk() {
    }

    public FavoritePk(long mailId, long userId) {
        this.mailId = mailId;
        this.userId = userId;
    }

    public long getMailId() {
        return mailId;
    }

    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
