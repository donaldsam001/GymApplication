package com.example.temp.Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemberCard extends MembershipPackage {
    private final SimpleIntegerProperty customerID;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty endDate;
    private final SimpleStringProperty customerName;

    public MemberCard(int customerID, String customerName,
                      int packageID, String packageName,
                      String startDate, String endDate, int exp) {

        // Gọi constructor của lớp cha MembershipPackage
        super(packageID, packageName, exp);

        this.customerID = new SimpleIntegerProperty(customerID);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.customerName = new SimpleStringProperty(customerName);
    }

    // Getters
    public int getCustomerID() {
        return customerID.get();
    }


    public String getStartDate() {
        return startDate.get();
    }

    public String getEndDate() {
        return endDate.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    // Setters
    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }


    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    // Property methods for JavaFX binding
    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }


    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

}
