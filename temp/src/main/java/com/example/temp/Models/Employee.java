package com.example.temp.Models;


import javafx.beans.property.*;

public class Employee {
    private final IntegerProperty employeeID;
    private final StringProperty employeeName;
    private final StringProperty password;
    private final StringProperty phone;
    private final BooleanProperty isReceptionist;

    public Employee() {
        this.employeeID = new SimpleIntegerProperty();
        this.employeeName = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.isReceptionist = new SimpleBooleanProperty();
    }

    public Employee(int id, String name, String password, String phone) {
        this.employeeID = new SimpleIntegerProperty(id);
        this.employeeName = new SimpleStringProperty(name);
        this.password = new SimpleStringProperty(password);
        this.phone = new SimpleStringProperty(phone);
        this.isReceptionist = new SimpleBooleanProperty(false);
    }

    public Employee(int id, String name, String password, String phone, boolean isReceptionist) {
        this.employeeID = new SimpleIntegerProperty(id);
        this.employeeName = new SimpleStringProperty(name);
        this.password = new SimpleStringProperty(password);
        this.phone = new SimpleStringProperty(phone);
        this.isReceptionist = new SimpleBooleanProperty(isReceptionist);
    }

    // Getters
    public int getEmployeeID() { return employeeID.get(); }
    public String getEmployeeName() { return employeeName.get(); }
    public String getPassword() { return password.get(); }
    public String getPhone() { return phone.get(); }
    public boolean isReceptionist() { return isReceptionist.get(); }

    // Setters
    public void setEmployeeID(int id) { employeeID.set(id); }
    public void setEmployeeName(String name) { employeeName.set(name); }
    public void setPassword(String password) { this.password.set(password); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setReceptionist(boolean receptionist) { isReceptionist.set(receptionist); }

    // Property Getters (for JavaFX binding)
    public IntegerProperty employeeIDProperty() { return employeeID; }
    public StringProperty employeeNameProperty() { return employeeName; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty phoneProperty() { return phone; }
    public BooleanProperty isReceptionistProperty() { return isReceptionist; }

    @Override
    public String toString() {
        return employeeName.get();
    }
}