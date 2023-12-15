package com.example.gestiondecourrier.ui.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User {


    private long id;
    private Structure structure;
    private String name,firstName, fun,email,beginDate,endDate,status,password,role,job;
    private boolean notification;

    public User() {
    }

    public User(String password) {
        this.password = password;
    }

    public User(Structure structure, String name, String firstName, String fun, String endDate) {
        this.structure = structure;
        this.name = name;
        this.firstName = firstName;
        this.fun = fun;
        this.endDate = endDate;
    }

    public User(long id, Structure structure, String name, String firstName, String fun, String email, String beginDate, String endDate, String status, String password, String role, String job, boolean notification) {
        this.id = id;
        this.structure = structure;
        this.name = name;
        this.firstName = firstName;
        this.fun = fun;
        this.email = email;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.status = status;
        this.password = password;
        this.role = role;
        this.job = job;
        this.notification = notification;
    }

    public User(Structure structure, String name, String firstName, String fun, String email, String beginDate, String endDate, String status, String password, String role, String job, boolean notification) {
        this.structure = structure;
        this.name = name;
        this.firstName = firstName;
        this.fun = fun;
        this.email = email;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.status = status;
        this.password = password;
        this.role = role;
        this.job = job;
        this.notification = notification;
    }

    public User(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
}
