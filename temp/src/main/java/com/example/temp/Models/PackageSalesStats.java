package com.example.temp.Models;

public class PackageSalesStats {
    private int packageID;
    private String packageName;
    private int totalSales;

    public PackageSalesStats(int packageID, String packageName, int totalSales) {
        this.packageID = packageID;
        this.packageName = packageName;
        this.totalSales = totalSales;
    }

    public int getPackageID() { return packageID; }
    public String getPackageName() { return packageName; }
    public int getTotalSales() { return totalSales; }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }
}
