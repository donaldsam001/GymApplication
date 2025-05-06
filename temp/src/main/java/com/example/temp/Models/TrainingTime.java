package com.example.temp.Models;

public class TrainingTime {
    private int id;
    private String name;
    private String phone;
    private String checkInTime;
    private String checkOutTime;
    private String note;  // Thêm trường note

    // Constructor
    public TrainingTime(int id, String name, String phone, String checkInTime, String checkOutTime, String note) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.note = note;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getNote() {  // Phương thức getter cho note
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

