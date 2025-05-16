package com.example.temp.Models;

public class PackageSale {
    private int id;
    private int packageID;
    private double totalPrice;
    private String saleDate;

    public PackageSale(int id, int packageID, double totalPrice, String saleDate) {
        this.id = id;
        this.packageID = packageID;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
    }

    // Getters
    public int getId() { return id; }
    public int getPackageID() { return packageID; }
    public double getTotalPrice() { return totalPrice; }
    public String getSaleDate() { return saleDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setPackageID(int packageID) { this.packageID = packageID; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setSaleDate(String saleDate) { this.saleDate = saleDate; }
}
