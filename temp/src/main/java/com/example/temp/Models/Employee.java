package com.example.temp.Models;


public class Employee {
    private int id;
    private String name;
    private String password;
    private String role;
    private String phone;
    public Employee() {}

    public Employee(int id, String name, String password, String phone , String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.phone = phone;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
