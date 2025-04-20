package com.example.temp.Models;


public class TrainingTime {
    private String customerID;
    private String name;
    private String phone;
    private String startDay;
    private String endDay;
    private String status;

    public TrainingTime(String customerID, String name, String phone, String startDay, String endDay, String status) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.startDay = startDay;
        this.endDay = endDay;
        this.status = status;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
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

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
