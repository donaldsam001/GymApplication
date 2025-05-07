package com.example.temp.Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemberCard {
    private final SimpleIntegerProperty customerID;
    private final SimpleIntegerProperty packageID;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty endDate;
    private final SimpleStringProperty goi;
    private final SimpleStringProperty price;

    public MemberCard(int customerID, int packageID,
                      String startDate, String endDate, String goi, String price) {
        this.customerID = new SimpleIntegerProperty(customerID);
        this.packageID = new SimpleIntegerProperty(packageID);

        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.goi = new SimpleStringProperty(goi);
        this.price = new SimpleStringProperty(price);
    }

    public int getCustomerID() {
        return customerID.get();
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public int getPackageID() {
        return packageID.get();
    }

    public void setPackageID(int packageID) {
        this.packageID.set(packageID);
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

    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }

    public SimpleIntegerProperty nameProperty() {
        return packageID;
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
