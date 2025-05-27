package com.example.temp.Models;
import java.time.LocalDate;

public class PackageSale {
    private int id;
    private int customerId;
    private int packageId;
    private String packageName;
    private int totalPrice;
    private LocalDate saleDate;
    private String type;

    // Các trường thống kê (có thể không được dùng trong tất cả trường hợp)
    private int totalSales;
    private int revenue;

    // Constructors
    public PackageSale() {}

    // Dùng khi insert/update
    public PackageSale(int customerId, int packageId, String packageName, int totalPrice, LocalDate saleDate, String type) {
        this.customerId = customerId;
        this.packageId = packageId;
        this.packageName = packageName;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
        this.type = type;
    }

    // Dùng khi thống kê
    public PackageSale(int packageId, String packageName, int totalSales, int revenue) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.totalSales = totalSales;
        this.revenue = revenue;
    }
    // Dùng khi thống kê
    public PackageSale(int packageId, String packageName, int totalSales) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.totalSales = totalSales;
    }

    // Getters and setters cho cả thông thường và thống kê...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }
}
