package com.example.gestiondecourrier.ui.pojo;

public class CategoryInfo {

    private long id,cpt;
    private String code,designation;

    public CategoryInfo() {
    }

    public CategoryInfo(long id, long cpt, String code, String designation) {
        this.id = id;
        this.cpt = cpt;
        this.code = code;
        this.designation = designation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCpt() {
        return cpt;
    }

    public void setCpt(long cpt) {
        this.cpt = cpt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


}
