package com.example.temp.Models;
import java.time.LocalDate;

public class PackageSale {
    private int id;
    private int customerId;
    private int packageId;
    private int totalPrice;
    private LocalDate saleDate;
    private String type; // "new" or "renewal"

    // Constructors
    public PackageSale() {}

    public PackageSale(int customerId, int packageId, int totalPrice, LocalDate saleDate, String type) {
        this.customerId = customerId;
        this.packageId = packageId;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
        this.type = type;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}