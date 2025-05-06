package com.example.temp.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Member {
    private final StringProperty customerID;
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty gender;
    private final StringProperty schedule;
    private final ObjectProperty<LocalDate> startDate;
    private final ObjectProperty<LocalDate> endDate;
    private final IntegerProperty age;

    public Member(String customerID, String name, String phone, String gender,
                  String schedule, LocalDate startDate, LocalDate endDate, int age) {
        this.customerID = new SimpleStringProperty(customerID);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.gender = new SimpleStringProperty(gender);
        this.schedule = new SimpleStringProperty(schedule);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.age = new SimpleIntegerProperty(age);
    }

    public String getCustomerID() { return customerID.get(); }
    public String getName() { return name.get(); }
    public String getPhone() { return phone.get(); }
    public String getGender() { return gender.get(); }
    public String getSchedule() { return schedule.get(); }
    public LocalDate getStartDate() { return startDate.get(); }
    public LocalDate getEndDate() { return endDate.get(); }
    public int getAge() { return age.get(); }

    public void setCustomerID(String value) { customerID.set(value); }
    public void setName(String value) { name.set(value); }
    public void setPhone(String value) { phone.set(value); }
    public void setGender(String value) { gender.set(value); }
    public void setSchedule(String value) { schedule.set(value); }
    public void setStartDate(LocalDate value) { startDate.set(value); }
    public void setEndDate(LocalDate value) { endDate.set(value); }
    public void setAge(int value) { age.set(value); }

    public StringProperty customerIDProperty() { return customerID; }
    public StringProperty nameProperty() { return name; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty scheduleProperty() { return schedule; }
    public ObjectProperty<LocalDate> startDateProperty() { return startDate; }
    public ObjectProperty<LocalDate> endDateProperty() { return endDate; }
    public IntegerProperty ageProperty() { return age; }
}