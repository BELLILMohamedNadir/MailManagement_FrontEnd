package com.example.gestiondecourrier.ui.pojo;

public class Attendance {
    private long id;
    private Employee employee;
    private String date,stat;
    private int recuperation;
    private boolean attendance;

    public Attendance() {
    }

    public Attendance(long id, Employee employee, String date, String stat,int recuperation, boolean attendance) {
        this.id = id;
        this.employee = employee;
        this.date = date;
        this.stat = stat;
        this.recuperation=recuperation;
        this.attendance = attendance;
    }

    public Attendance(Employee employee, String date, String stat,int recuperation, boolean attendance) {
        this.employee = employee;
        this.date = date;
        this.stat = stat;
        this.recuperation=recuperation;
        this.attendance = attendance;
    }

    public Attendance(Employee employee, String date, String stat, boolean attendance) {
        this.employee = employee;
        this.date = date;
        this.stat = stat;
        this.attendance = attendance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }

    public int getRecuperation() {
        return recuperation;
    }

    public void setRecuperation(int recuperation) {
        this.recuperation = recuperation;
    }
}
