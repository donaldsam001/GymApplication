package com.example.temp.Models;
import javafx.beans.property.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Member {
    private final IntegerProperty memberID;
    private final StringProperty memberName;
    private final StringProperty phone;
    private final StringProperty gender;
    private final IntegerProperty age;
    private final StringProperty packageName;
    private final StringProperty startDate;
    private final StringProperty endDate;

    public Member(int customerID, String name, String phone, String gender, int age,
                  String packageName, String startDate, String endDate) {
        this.memberID = new SimpleIntegerProperty(customerID);
        this.memberName = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.gender = new SimpleStringProperty(gender);
        this.age = new SimpleIntegerProperty(age);
        this.packageName = new SimpleStringProperty(packageName);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
    }

    // Getters (giá trị)
    public int getMemberID() { return memberID.get(); }
    public String getMemberName() { return memberName.get(); }
    public String getPhone() { return phone.get(); }
    public String getGender() { return gender.get(); }
    public int getAge() { return age.get(); }
    public String getPackageName() { return packageName.get(); }
    public String getStartDate() { return startDate.get(); }
    public String getEndDate() { return endDate.get(); }

    // Setters
    public void setMemberID(int value) { memberID.set(value); }
    public void setMemberName(String value) { memberName.set(value); }
    public void setPhone(String value) { phone.set(value); }
    public void setGender(String value) { gender.set(value); }
    public void setAge(int value) { age.set(value); }
    public void setPackageName(String value) { packageName.set(value); }
    public void setStartDate(String value) { startDate.set(value); }
    public void setEndDate(String value) { endDate.set(value); }

    // Property Getters (dùng cho TableView, binding...)
    public IntegerProperty memberIDProperty() { return memberID; }
    public StringProperty memberNameProperty() { return memberName; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty genderProperty() { return gender; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty packageNameProperty() { return packageName; }
    public StringProperty startDateProperty() { return startDate; }
    public StringProperty endDateProperty() { return endDate; }
}
