package com.example.temp.Models;

public class PackageSalesStats {
    private int packageID;
    private String packageName;
    private int totalSales;
    private int revenue;


    public PackageSalesStats(int packageID, String packageName, int totalSales, int revenue) {
        this.packageID = packageID;
        this.packageName = packageName;
        this.totalSales = totalSales;
        this.revenue = revenue;
    }

    public int getPackageID() { return packageID; }
    public String getPackageName() { return packageName; }
    public int getTotalSales() { return totalSales; }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }
}
