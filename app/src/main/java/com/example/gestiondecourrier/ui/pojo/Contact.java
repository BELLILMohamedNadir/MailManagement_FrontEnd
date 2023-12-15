package com.example.gestiondecourrier.ui.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class Contact {

    private long id;

    private String name;

    private String firstName;

    private String structure;

    private String email;


    public Contact() {
    }

    public Contact(long id, String name, String firstName, String structure, String email) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.structure = structure;
        this.email = email;
    }

    public Contact(String name, String firstName, String structure, String email) {
        this.name = name;
        this.firstName = firstName;
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


}
