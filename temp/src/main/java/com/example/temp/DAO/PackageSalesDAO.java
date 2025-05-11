package com.example.temp.DAO;

import com.example.temp.Models.PackageSalesStats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageSalesDAO {
    private static final String DB_URL = "jdbc:sqlite:service_app.db";

    public static void increaseSales(int packageID) {
        String sql = """
            INSERT INTO PackageSalesStats (packageID, totalSales)
            VALUES (?, 1)
            ON CONFLICT(packageID) DO UPDATE SET totalSales = totalSales + 1;
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, packageID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật thống kê bán gói: " + e.getMessage());
        }
    }

    public static List<PackageSalesStats> getAllStats() {
        List<PackageSalesStats> list = new ArrayList<>();
        String sql = """
            SELECT mp.id, mp.name, IFNULL(ps.totalSales, 0) AS totalSales
            FROM Membership_package mp
            LEFT JOIN PackageSalesStats ps ON mp.id = ps.packageID
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PackageSalesStats(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("totalSales")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn thống kê: " + e.getMessage());
        }
        return list;
    }
}
