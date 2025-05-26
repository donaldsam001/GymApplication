package com.example.temp.DAO;

import com.example.temp.Models.PackageSale;
import com.example.temp.Models.PackageSalesStats;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PackageSalesDAO {
    private Connection connection;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:service_app.db");
                logger.info("Connected to database.");
                createTable();
            }
        } catch (SQLException e) {
            logger.warning("❌ Connection error: " + e.getMessage());
        }
    }

    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS package_sales (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                packageID INTEGER NOT NULL,
                total_price INTEGER NOT NULL,
                sale_date TEXT NOT NULL,
                FOREIGN KEY (packageID) REFERENCES Membership_package(id)
            );
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            logger.info("package_sales table created or already exists.");
        } catch (SQLException e) {
            logger.warning("❌ Table creation error: " + e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.warning("❌ Close connection error: " + e.getMessage());
        }
    }

    public void recordSale(PackageSale sale) {
        getConnection();
        String sql = "INSERT INTO Package_Sales (customerID, packageID, total_price, sale_date, type) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sale.getCustomerId());
            stmt.setInt(2, sale.getPackageId());
            stmt.setInt(3, sale.getTotalPrice());
            stmt.setString(4, sale.getSaleDate().toString());
            stmt.setString(5, sale.getType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();
    }

    public List<PackageSalesStats> getAllStats() {
        getConnection();
        List<PackageSalesStats> statsList = new ArrayList<>();
        String sql = "SELECT packageID, COUNT(*) AS totalSales, SUM(total_price) AS revenue FROM Package_Sales GROUP BY packageID";

        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PackageSalesStats stats = new PackageSalesStats();
                stats.setPackageId(rs.getInt("packageID"));
                stats.setTotalSales(rs.getInt("totalSales"));
                stats.setRevenue(rs.getInt("revenue"));
                statsList.add(stats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statsList;
    }

    public List<PackageSale> getSalesByPeriod(String periodType, int year, int value) {
        getConnection();
        String dateCondition = switch (periodType.toLowerCase()) {
            case "month" -> "strftime('%Y-%m', sale_date) = '" + year + "-" + String.format("%02d", value) + "'";
            case "quarter" -> {
                String months = switch (value) {
                    case 1 -> "('01','02','03')";
                    case 2 -> "('04','05','06')";
                    case 3 -> "('07','08','09')";
                    case 4 -> "('10','11','12')";
                    default -> throw new IllegalArgumentException("Invalid quarter");
                };
                yield "strftime('%Y', sale_date) = '" + year + "' AND strftime('%m', sale_date) IN " + months;
            }
            case "year" -> "strftime('%Y', sale_date) = '" + year + "'";
            default -> throw new IllegalArgumentException("Invalid periodType");
        };

        List<PackageSale> result = new ArrayList<>();
        String sql = " SELECT ps.*, mp.name AS packageName FROM Package_Sales ps JOIN Membership_package mp ON ps.packageID = mp.id WHERE " + dateCondition;

        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PackageSale sale = new PackageSale();
                sale.setId(rs.getInt("id"));
                sale.setCustomerId(rs.getInt("customerID"));
                sale.setPackageId(rs.getInt("packageID"));
                sale.setTotalPrice(rs.getInt("total_price"));
                sale.setSaleDate(LocalDate.parse(rs.getString("sale_date")));
                sale.setType(rs.getString("type"));
                sale.setPackageName(rs.getString("packageName"));  // Lấy tên gói

                result.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateOrInsertStats(int packageId, int price) {
        getConnection();
        String selectSQL = "SELECT * FROM PackageSalesStats WHERE packageID = ?";
        String insertSQL = "INSERT INTO PackageSalesStats (packageID, totalSales, revenue) VALUES (?, ?, ?)";
        String updateSQL = "UPDATE PackageSalesStats SET totalSales = totalSales + 1, revenue = revenue + ? WHERE packageID = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {
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
