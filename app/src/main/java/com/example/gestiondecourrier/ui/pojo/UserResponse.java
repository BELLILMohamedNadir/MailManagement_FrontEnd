package com.example.gestiondecourrier.ui.pojo;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class UserResponse {

    private long id;
    private long structure_id;
    private String structureDesignation,structureCode,name,firstName,fun,email,beginDate,endDate,status,role,job, firebaseToken;
    private boolean notification;

    private byte[] bytes;
    private int positionInTheViewModel;

    public UserResponse() {
    }

    public UserResponse(long id, long structure_id, String structureDesignation, String structureCode, String name, String firstName, String fun, String email, String beginDate, String endDate, String status, String role, String job, String firebaseToken, boolean notification, byte[] bytes) {
        this.id = id;
        this.structure_id = structure_id;
        this.structureDesignation = structureDesignation;
        this.structureCode = structureCode;
        this.name = name;
        this.firstName = firstName;
        this.fun = fun;
        this.email = email;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.status = status;
        this.role = role;
        this.job = job;
        this.firebaseToken = firebaseToken;
        this.notification = notification;
        this.bytes = bytes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStructure_id() {
        return structure_id;
    }

    public void setStructure_id(long structure_id) {
        this.structure_id = structure_id;
    }

    public String getStructureDesignation() {
        return structureDesignation;
    }

    public void setStructureDesignation(String structureDesignation) {
        this.structureDesignation = structureDesignation;
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

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getStructureCode() {
        return structureCode;
    }

    public void setStructureCode(String structureCode) {
        this.structureCode = structureCode;
    }

    public int getPositionInTheViewModel() {
        return positionInTheViewModel;
    }

    public void setPositionInTheViewModel(int positionInTheViewModel) {
        this.positionInTheViewModel = positionInTheViewModel;
    }
}
