package com.example.gestiondecourrier.ui.pojo;

import android.os.Parcel;

public class Category {

    private long id;

    private String type,designation,code;

    private long number;

    private Structure structure;

    private long cpt;

    public Category() {
    }

    public Category(long id) {
        this.id = id;
    }

    public Category(long id, String type, String designation, String code, long number, Structure struct, long cpt) {
        this.id = id;
        this.type = type;
        this.designation = designation;
        this.code = code;
        this.number = number;
        this.structure = struct;
        this.cpt=cpt;
    }

    public Category(String designation, String code) {
        this.designation = designation;
        this.code = code;
    }

    public Category(String type, String designation, String code, long number, Structure struct, long cpt) {
        this.type = type;
        this.designation = designation;
        this.code = code;
        this.number = number;
        this.structure = struct;
        this.cpt=cpt;
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

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public long getCpt() {
        return cpt;
    }

    public void setCpt(long cpt) {
        this.cpt = cpt;
    }
}
