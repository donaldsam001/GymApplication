package com.example.temp.DAO;

import com.example.temp.Models.PackageSale;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackageSalesDAO {
    private static final Logger LOGGER = Logger.getLogger(PackageSalesDAO.class.getName());

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:service_app.db");
    }

    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Package_Sales (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                customerID INTEGER NOT NULL,
                packageID INTEGER NOT NULL,
                total_price REAL NOT NULL,
                sale_date TEXT NOT NULL,
                type TEXT NOT NULL CHECK (type IN ('new', 'renewal')),
                FOREIGN KEY (customerID) REFERENCES MemberDetail(customerID),
                FOREIGN KEY (packageID) REFERENCES MembershipPackage(id)
            );
        """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating Package_Sales table", e);
        }
    }

    public void insertPackageSale(PackageSale sale) {
        String sql = "INSERT INTO Package_Sales (customerID, packageID, total_price, sale_date, type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sale.getCustomerId());
            pstmt.setInt(2, sale.getPackageId());
            pstmt.setDouble(3, sale.getTotalPrice());
            pstmt.setString(4, sale.getSaleDate().toString());  // toString() trả về yyyy-MM-dd (OK nếu dùng LocalDate)
            pstmt.setString(5, sale.getType());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting package sale", e);
        }
    }

    public List<PackageSale> getSalesByPeriod(String periodType, int year, int periodValue) {
        List<PackageSale> sales = new ArrayList<>();
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
                    PackageSale sale = new PackageSale();
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

    public List<PackageSale> getStatsSummary() {
        List<PackageSale> stats = new ArrayList<>();
        String sql = """
            SELECT ps.packageID, mp.name as packageName, COUNT(*) as total_sales, SUM(ps.total_price) as total_revenue
            FROM Package_Sales ps
            JOIN MembershipPackage mp ON ps.packageID = mp.id
            GROUP BY ps.packageID
        """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PackageSale stat = new PackageSale();
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
    public void updateOrInsertStats(int packageId, int price) {
        String selectSQL = "SELECT * FROM PackageSalesStats WHERE packageID = ?";
        String insertSQL = "INSERT INTO PackageSalesStats (packageID, totalSales, revenue) VALUES (?, ?, ?)";
        String updateSQL = "UPDATE PackageSalesStats SET totalSales = totalSales + 1, revenue = revenue + ? WHERE packageID = ?";

        try (Connection connection = getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {
            selectStmt.setInt(1, packageId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // Đã tồn tại → cập nhật
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
                    updateStmt.setInt(1, price);
                    updateStmt.setInt(2, packageId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Chưa có → thêm mới
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
                    insertStmt.setInt(1, packageId);
                    insertStmt.setInt(2, 1);
                    insertStmt.setInt(3, price);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
