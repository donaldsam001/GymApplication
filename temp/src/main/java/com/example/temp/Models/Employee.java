package com.example.temp.Models;


public class Employee {
    private int id;
    private String name;
    private String password;
    private String phone;
    private boolean isReceptionist;
    public Employee() {}

    public Employee(int id, String name, String password, String phone ) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }
    public Employee(int id, String name, String password, String phone , boolean isReceptionist) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.isReceptionist = isReceptionist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isReceptionist() {
        return isReceptionist;
    }

    public void setReceptionist(boolean receptionist) {
        isReceptionist = receptionist;
    }
}
