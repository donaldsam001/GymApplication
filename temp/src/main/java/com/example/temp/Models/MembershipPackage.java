package com.example.temp.Models;

public class MembershipPackage {
    private int packageID;
    private String packageName;
    private float price;
    private String description;
    private int exp;
    private boolean status;

    public MembershipPackage(int packageID, String packageName) {
        this.packageID = packageID;
        this.packageName = packageName;
    }

    public MembershipPackage(int packageID, String packageName, float price, String description, int exp, boolean status) {
        this.packageID = packageID;
        this.packageName = packageName;
        this.price = price;
        this.description = description;
        this.exp = exp;
        this.status = status;
    }

    // Getter - Setter

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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
