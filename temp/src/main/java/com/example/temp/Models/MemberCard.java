package com.example.temp.Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemberCard {
    private final SimpleIntegerProperty customerID;
    private final SimpleIntegerProperty packageID;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty endDate;
    private final SimpleStringProperty packageName;
    private final SimpleStringProperty customerName;
    private final SimpleIntegerProperty exp;

    public MemberCard(int customerID, int packageID,
                      String startDate, String endDate,
                      String packageName, String customerName, int exp) {
        this.customerID = new SimpleIntegerProperty(customerID);
        this.packageID = new SimpleIntegerProperty(packageID);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.packageName = new SimpleStringProperty(packageName);
        this.customerName = new SimpleStringProperty(customerName);
        this.exp = new SimpleIntegerProperty(exp);
    }

    // Getters
    public int getCustomerID() {
        return customerID.get();
    }

    public int getPackageID() {
        return packageID.get();
    }

    public String getStartDate() {
        return startDate.get();
    }

    public String getEndDate() {
        return endDate.get();
    }

    public String getPackageName() {
        return packageName.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public int getExp() {
        return exp.get();
    }

    // Setters
    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public void setPackageID(int packageID) {
        this.packageID.set(packageID);
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public void setPackageName(String packageName) {
        this.packageName.set(packageName);
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public void setExp(int exp) {
        this.exp.set(exp);
    }

    // Property methods for data binding
    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }

    public SimpleIntegerProperty packageIDProperty() {
        return packageID;
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public SimpleStringProperty packageNameProperty() {
        return packageName;
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public SimpleIntegerProperty expProperty() {
        return exp;
    }

    // Optional: toString override for debugging
    @Override
    public String toString() {
        return "MemberCard{" +
                "customerID=" + getCustomerID() +
                ", packageID=" + getPackageID() +
                ", startDate='" + getStartDate() + '\'' +
                ", endDate='" + getEndDate() + '\'' +
                ", packageName='" + getPackageName() + '\'' +
                ", customerName='" + getCustomerName() + '\'' +
                ", exp=" + getExp() +
                '}';
    }
}
