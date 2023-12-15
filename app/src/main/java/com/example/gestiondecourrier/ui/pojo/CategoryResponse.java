package com.example.gestiondecourrier.ui.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CategoryResponse {

    private long id;

    private String type,designation,code;
    private long number;
    private String designation_struct,code_struct, mother_structure;
    private long cpt;
    private int positionInTheViewModel;

    public CategoryResponse() {
    }

    public CategoryResponse(long id, String type, String designation, String code, long number, String designation_struct, String code_struct, String mother_structure, long cpt) {
        this.id = id;
        this.type = type;
        this.designation = designation;
        this.code = code;
        this.number = number;
        this.designation_struct = designation_struct;
        this.code_struct = code_struct;
        this.mother_structure = mother_structure;
        this.cpt = cpt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getDesignation_struct() {
        return designation_struct;
    }

    public void setDesignation_struct(String designation_struct) {
        this.designation_struct = designation_struct;
    }

    public String getCode_struct() {
        return code_struct;
    }

    public void setCode_struct(String code_struct) {
        this.code_struct = code_struct;
    }

    public String getMother_structure() {
        return mother_structure;
    }

    public void setMother_structure(String mother_structure) {
        this.mother_structure = mother_structure;
    }

    public long getCpt() {
        return cpt;
    }

    public void setCpt(long cpt) {
        this.cpt = cpt;
    }

    public int getPositionInTheViewModel() {
        return positionInTheViewModel;
    }

    public void setPositionInTheViewModel(int positionInTheViewModel) {
        this.positionInTheViewModel = positionInTheViewModel;
    }
}
