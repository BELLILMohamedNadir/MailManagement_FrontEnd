package com.example.gestiondecourrier.ui.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Structure {

    private long id;
    private String designation,code,motherStructure;


    public Structure(long id, String designation, String code, String motherStructure) {
        this.id = id;
        this.designation = designation;
        this.code = code;
        this.motherStructure = motherStructure;
    }

    public Structure(String designation, String code, String motherStructure) {
        this.designation = designation;
        this.code = code;
        this.motherStructure = motherStructure;
    }

    protected Structure(Parcel in) {
        id = in.readLong();
        designation = in.readString();
        code = in.readString();
        motherStructure = in.readString();
    }

    public Structure(long id) {
        this.id = id;
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


}
