package com.example.gestiondecourrier.ui.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StructureResponse {

    private long id;
    private String designation,code,motherStructure;
    private int positionInViewModel;


    public StructureResponse(long id, String designation, String code, String motherStructure, int positionInViewModel) {
        this.id = id;
        this.designation = designation;
        this.code = code;
        this.motherStructure = motherStructure;
        this.positionInViewModel = positionInViewModel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMotherStructure() {
        return motherStructure;
    }

    public void setMotherStructure(String motherStructure) {
        this.motherStructure = motherStructure;
    }

    public int getPositionInViewModel() {
        return positionInViewModel;
    }

    public void setPositionInViewModel(int positionInViewModel) {
        this.positionInViewModel = positionInViewModel;
    }
}
