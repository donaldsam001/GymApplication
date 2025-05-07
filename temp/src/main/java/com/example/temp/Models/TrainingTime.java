package com.example.temp.Models;

public class TrainingTime {
    private int id;
    private String checkInTime;
    private String checkOutTime;
    private String note;  // Thêm trường note

    // Constructor
    public TrainingTime(int id, String checkInTime, String checkOutTime, String note) {
        this.id = id;
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

