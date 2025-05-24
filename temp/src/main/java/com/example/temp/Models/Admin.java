package com.example.temp.Models;

public class Admin extends Employee {

    private String email;

    public Admin() {
        super();
    }

    public Admin(int id, String name, String password, String phone, String email) {
        super(id, name, password, phone);
        this.email = email;
    }


    // Getter & Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
