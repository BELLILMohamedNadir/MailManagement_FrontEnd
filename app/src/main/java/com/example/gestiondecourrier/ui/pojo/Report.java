package com.example.gestiondecourrier.ui.pojo;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Report {

    private long id;
    private byte[] bytes;
    private Date date;
    private String dateToTrait;
    private String dateToShow;
    private String type;
    private boolean approved;
    private int positionInTheViewModel;

    public Report() {
    }

    public Report(long id, byte[] bytes, Date date, String dateToTrait, String dateToShow, String type, boolean approved) {
        this.id = id;
        this.bytes = bytes;
        this.date = date;
        this.dateToTrait = dateToTrait;
        this.dateToShow = dateToShow;
        this.type = type;
        this.approved = approved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getDateToShow() {
        return dateToShow;
    }

    public void setDateToShow(String dateToShow) {
        this.dateToShow = dateToShow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateToTrait() {
        return dateToTrait;
    }

    public void setDateToTrait(String dateToTrait) {
        this.dateToTrait = dateToTrait;
    }

    public int getPositionInTheViewModel() {
        return positionInTheViewModel;
    }

    public void setPositionInTheViewModel(int positionInTheViewModel) {
        this.positionInTheViewModel = positionInTheViewModel;
    }
}
