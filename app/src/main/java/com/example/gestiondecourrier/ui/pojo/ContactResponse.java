package com.example.gestiondecourrier.ui.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class ContactResponse {

    private long id;

    private String name;

    private String firstName;

    private String structure;

    private String email;

    private int positionInViewModel;


    public ContactResponse() {
    }

    public ContactResponse(long id, String name, String firstName, String structure, String email, int positionInViewModel) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.structure = structure;
        this.email = email;
        this.positionInViewModel = positionInViewModel;
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

    public int getPositionInViewModel() {
        return positionInViewModel;
    }

    public void setPositionInViewModel(int positionInViewModel) {
        this.positionInViewModel = positionInViewModel;
    }
}
