package com.example.temp.Models;

import javafx.beans.property.SimpleStringProperty;

public class MemberCard {
    private final SimpleStringProperty customerID;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty endDate;
    private final SimpleStringProperty goi;
    private final SimpleStringProperty price;

    public MemberCard(String customerID, String name, String phone, String gender,
                      String startDate, String endDate, String goi, String price) {
        this.customerID = new SimpleStringProperty(customerID);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.gender = new SimpleStringProperty(gender);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.goi = new SimpleStringProperty(goi);
        this.price = new SimpleStringProperty(price);
    }

    public String getCustomerID() {
        return customerID.get();
    }

    public void setCustomerID(String customerID) {
        this.customerID.set(customerID);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public String getGoi() {
        return goi.get();
    }

    public void setGoi(String goi) {
        this.goi.set(goi);
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public SimpleStringProperty customerIDProperty() {
        return customerID;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public SimpleStringProperty goiProperty() {
        return goi;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }
}
