package com.example.temp.Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MembershipCard extends MembershipPackage {
    private final SimpleIntegerProperty memberID;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty endDate;
    private final SimpleStringProperty memberName;

    public MembershipCard(int customerID, String memberName,
                          int packageID, String packageName,
                          String startDate, String endDate, int exp) {

        // Gọi constructor của lớp cha MembershipPackage
        super(packageID, packageName, exp);

        this.memberID = new SimpleIntegerProperty(customerID);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.memberName = new SimpleStringProperty(memberName);
    }

    // Getters
    public int getMemberID() {
        return memberID.get();
    }


    public String getStartDate() {
        return startDate.get();
    }

    public String getEndDate() {
        return endDate.get();
    }

    public String getMemberName() {
        return memberName.get();
    }

    // Setters
    public void setMemberID(int customerID) {
        this.memberID.set(customerID);
    }


    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public void setMemberName(String memberName) {
        this.memberName.set(memberName);
    }

    // Property methods for JavaFX binding
    public SimpleIntegerProperty memberIDProperty() {
        return memberID;
    }


    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public SimpleStringProperty memberNameProperty() {
        return memberName;
    }

}
