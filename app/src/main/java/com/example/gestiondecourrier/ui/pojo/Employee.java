package com.example.gestiondecourrier.ui.pojo;

public class Employee {
    private long id;

    private int recuperation;

    private String name;

    private String firstName;

    private String registrationKey;

    private Structure structure;

    private String email;

    public Employee() {
    }

    public Employee(long id) {
        this.id = id;
    }

    public Employee(long id, int recuperation, String name, String firstName, String registrationKey, Structure structure, String email) {
        this.id = id;
        this.recuperation = recuperation;
        this.name = name;
        this.firstName = firstName;
        this.registrationKey = registrationKey;
        this.structure = structure;
        this.email = email;
    }

    public Employee(int recuperation, String name, String firstName, String registrationKey, Structure structure, String email) {
        this.recuperation=recuperation;
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

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
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
}
