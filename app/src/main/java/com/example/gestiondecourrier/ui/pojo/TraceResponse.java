package com.example.gestiondecourrier.ui.pojo;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class TraceResponse {

    private long id;
    private byte[] bytes;
    private Date time,updateTime;
    private String timeToShow,updateTimeToShow,timeToTrait,updateTimeToTrait;
    private String action,reference,name,firstName,job,email;
    private int positionInTheViewModel;

    public TraceResponse() {
    }

    public TraceResponse(long id, byte[] bytes, Date time, Date updateTime, String timeToShow, String updateTimeToShow, String timeToTrait, String updateTimeToTrait, String action, String reference, String name, String firstName, String job, String email) {
        this.id = id;
        this.bytes = bytes;
        this.time = time;
        this.updateTime = updateTime;
        this.timeToShow = timeToShow;
        this.updateTimeToShow = updateTimeToShow;
        this.timeToTrait = timeToTrait;
        this.updateTimeToTrait = updateTimeToTrait;
        this.action = action;
        this.reference = reference;
        this.name = name;
        this.firstName = firstName;
        this.job = job;
        this.email = email;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimeToShow() {
        return timeToShow;
    }

    public void setTimeToShow(String timeToShow) {
        this.timeToShow = timeToShow;
    }

    public String getUpdateTimeToShow() {
        return updateTimeToShow;
    }

    public void setUpdateTimeToShow(String updateTimeToShow) {
        this.updateTimeToShow = updateTimeToShow;
    }

    public String getTimeToTrait() {
        return timeToTrait;
    }

    public void setTimeToTrait(String timeToTrait) {
        this.timeToTrait = timeToTrait;
    }

    public String getUpdateTimeToTrait() {
        return updateTimeToTrait;
    }

    public void setUpdateTimeToTrait(String updateTimeToTrait) {
        this.updateTimeToTrait = updateTimeToTrait;
    }

    public int getPositionInTheViewModel() {
        return positionInTheViewModel;
    }

    public void setPositionInTheViewModel(int positionInTheViewModel) {
        this.positionInTheViewModel = positionInTheViewModel;
    }
}
