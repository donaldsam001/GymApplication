package com.example.temp.Models;
import javafx.beans.property.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Member {
    private final IntegerProperty ID;
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty gender;
    private final IntegerProperty age;
    private String packageName;
    private String startDate;
    private String endDate;

    public Member(int customerID, String name, String phone, String gender, int age,
                  String packageName, String startDate, String endDate) {
        this.ID = new SimpleIntegerProperty(customerID);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.gender = new SimpleStringProperty(gender);
        this.age = new SimpleIntegerProperty(age);
        this.packageName = packageName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getCustomerID() { return ID.get(); }
    public String getName() { return name.get(); }
    public String getPhone() { return phone.get(); }
    public String getGender() { return gender.get(); }
    public int getAge() { return age.get(); }

    public void setCustomerID(int value) { ID.set(value); }
    public void setName(String value) { name.set(value); }
    public void setPhone(String value) { phone.set(value); }
    public void setGender(String value) { gender.set(value); }
    public void setAge(int value) { age.set(value); }

    public IntegerProperty IDProperty() { return ID; }
    public StringProperty nameProperty() { return name; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty genderProperty() { return gender; }
    public IntegerProperty ageProperty() { return age; }


    public String getPackageName() { return packageName; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
}
