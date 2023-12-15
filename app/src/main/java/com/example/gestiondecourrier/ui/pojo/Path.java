package com.example.gestiondecourrier.ui.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Path {

    private User user;
    private String path,internalReference;

    public Path() {
    }

    public Path(User user, String path, String internalReference) {
        this.user = user;
        this.path = path;
        this.internalReference = internalReference;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

}
