package com.example.temp.Models;

public class MembershipPackage {
    private int id;
    private String name;
    private float price;
    private String description;
    private int exp;
    private boolean status;

    public MembershipPackage() {}

    public MembershipPackage(int id, String name, float price, String description, int exp, boolean status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.exp = exp;
        this.status = status;
    }

    // Getter - Setter

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
