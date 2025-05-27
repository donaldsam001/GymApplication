package com.example.temp.Models;
import java.time.LocalDate;

public class PackageSalesStats {
    private int PackageSalesStatsID;
    private int memberID;
    private int packageId;
    private String packageName;
    private int totalPrice; // giá gói
    private LocalDate saleDate; // ngày thanh toán
    private String type; // loại giao dịch ( đăng kí, gia hạn)

    // Các trường thống kê (có thể không được dùng trong tất cả trường hợp)
    private int totalSales; // tổng lượt bán
    private int revenue; // doanh thu

    // Constructors
    public PackageSalesStats() {}

    // Dùng khi insert/update
    public PackageSalesStats(int customerId, int packageId, String packageName, int totalPrice, LocalDate saleDate, String type) {
        this.memberID = customerId;
        this.packageId = packageId;
        this.packageName = packageName;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
        this.type = type;
    }

    // Dùng khi thống kê
    public PackageSalesStats(int packageId, String packageName, int totalSales, int revenue) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.totalSales = totalSales;
        this.revenue = revenue;
    }
    // Dùng khi thống kê
    public PackageSalesStats(int packageId, String packageName, int totalSales) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.totalSales = totalSales;
    }

    // Getters and setters cho cả thông thường và thống kê...

    public int getPackageSalesStatsID() {
        return PackageSalesStatsID;
    }

    public void setPackageSalesStatsID(int id) {
        this.PackageSalesStatsID = id;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
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
