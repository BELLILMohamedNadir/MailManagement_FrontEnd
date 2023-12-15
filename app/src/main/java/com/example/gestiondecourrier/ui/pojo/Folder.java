package com.example.gestiondecourrier.ui.pojo;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Folder{
    String path;
    String name;
    byte[] bytes;
    int positionInTheViewModel;

    public Folder() {
    }

    public Folder(String path, String name, byte[] bytes, int positionInTheViewModel) {
        this.path = path;
        this.name = name;
        this.bytes = bytes;
        this.positionInTheViewModel = positionInTheViewModel;
    }

    public Folder(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getPositionInTheViewModel() {
        return positionInTheViewModel;
    }
}
