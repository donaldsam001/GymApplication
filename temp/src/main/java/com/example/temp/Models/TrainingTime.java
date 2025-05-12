package com.example.temp.Models;

public class TrainingTime {
    private int customerID; // Mã hội viên
    private String customerName;
    private String checkInTime; // Thời gian check-in
    private String checkOutTime; // Thời gian check-out
    private String note; // Ghi chú

    // Constructor
    public TrainingTime(int customerID, String customerName, String checkInTime, String checkOutTime, String note) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.note = note;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Getter and Setter methods
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
