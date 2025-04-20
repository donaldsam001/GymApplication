
package com.example.temp.Models;


public class Customer {
    private String customerID;
    private String name;
    private String phone;
    private String startDate;
    private String endDate;
    private String status;

    public Customer(String customerID, String name, String phone, String startDate, String endDate, String status) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
