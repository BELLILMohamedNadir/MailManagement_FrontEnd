package com.example.gestiondecourrier.ui.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeResponse {

    private long id;

    private int recuperation;

    private String name;

    private String firstName;

    private String registrationKey;

    private String structure;

    private String email;

    private int positionInTheViewModel;

    public EmployeeResponse() {
    }

    public EmployeeResponse(long id, String name, String firstName, String registrationKey, String structure, String email) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.registrationKey = registrationKey;
        this.structure = structure;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getRegistrationKey() {
        return registrationKey;
    }

    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRecuperation() {
        return recuperation;
    }

    public void setRecuperation(int recuperation) {
        this.recuperation = recuperation;
    }

    public int getPositionInTheViewModel() {
        return positionInTheViewModel;
    }

    public void setPositionInTheViewModel(int positionInTheViewModel) {
        this.positionInTheViewModel = positionInTheViewModel;
    }
}
