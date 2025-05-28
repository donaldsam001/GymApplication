package com.example.temp.DAO;

import com.example.temp.Models.PackageSalesStats;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackageSalesStatsDAO {
    private static final Logger LOGGER = Logger.getLogger(PackageSalesStatsDAO.class.getName());

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:service_app.db");
    }


    public void insertPackageSale(PackageSalesStats sale) {
        String sql = "INSERT INTO Package_Sales (customerID, packageID, total_price, sale_date, type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sale.getMemberID());
            pstmt.setInt(2, sale.getPackageId());
            pstmt.setDouble(3, sale.getTotalPrice()); // giá 1 gói hv
            pstmt.setString(4, sale.getSaleDate().toString());  // toString() trả về yyyy-MM-dd (OK nếu dùng LocalDate)
            pstmt.setString(5, sale.getType());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting package sale", e);
        }
    }

    public List<PackageSalesStats> getSalesByPeriod(String periodType, int year, int periodValue) {
        List<PackageSalesStats> sales = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT ps.*, mp.name as packageName " +
                        "FROM Package_Sales ps " +
                        "JOIN MembershipPackage mp ON ps.packageID = mp.id " +
                        "WHERE strftime('%Y', sale_date) = ?"
        );

        switch (periodType) {
            case "month" -> sql.append(" AND strftime('%m', sale_date) = ?");
            case "quarter" -> sql.append(" AND (CAST(strftime('%m', sale_date) AS INTEGER) BETWEEN ? AND ?)");
        }

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, String.valueOf(year));

            if (periodType.equals("month")) {
                pstmt.setString(2, String.format("%02d", periodValue));
            } else if (periodType.equals("quarter")) {
                // Mapping quý thành tháng bắt đầu - kết thúc
                int startMonth = (periodValue - 1) * 3 + 1;
                int endMonth = startMonth + 2;

                pstmt.setInt(2, startMonth);
                pstmt.setInt(3, endMonth);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PackageSalesStats sale = new PackageSalesStats();
                    sale.setPackageId(rs.getInt("packageID"));
                    sale.setPackageName(rs.getString("packageName"));
                    sale.setTotalPrice((int) rs.getDouble("total_price"));
                    sales.add(sale);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving sales by period", e);
        }

        return sales;
    }

    // thong ke tong quat
    public List<PackageSalesStats> getStatsSummary() {
        List<PackageSalesStats> stats = new ArrayList<>();
        String sql = """
            SELECT ps.packageID, mp.name as packageName, COUNT(*) as total_sales, SUM(ps.total_price) as total_revenue
            FROM Package_Sales ps
            JOIN MembershipPackage mp ON ps.packageID = mp.id
            GROUP BY ps.packageID
        """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PackageSalesStats stat = new PackageSalesStats();
                stat.setPackageId(rs.getInt("packageID"));
                stat.setPackageName(rs.getString("packageName"));
                stat.setTotalSales(rs.getInt("total_sales"));
                stat.setRevenue((int) rs.getDouble("total_revenue"));
                stats.add(stat);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving summary stats", e);
        }

        return stats;
    }
}
