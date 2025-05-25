package com.example.temp.Models;

public class PackageSalesStats {
    private int packageId;
    private int totalSales;
    private int revenue;

    public PackageSalesStats() {
    }

    public PackageSalesStats(int packageId, int totalSales, int revenue) {
        this.packageId = packageId;
        this.totalSales = totalSales;
        this.revenue = revenue;
    }

    // Getters and Setters
    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }
    public int getTotalSales() { return totalSales; }
    public void setTotalSales(int totalSales) { this.totalSales = totalSales; }
    public int getRevenue() { return revenue; }
    public void setRevenue(int revenue) { this.revenue = revenue; }
}