package com.example.temp.DAO;

import com.example.temp.Models.PackageSalesStats;

import java.sql.*;
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
            CREATE TABLE IF NOT EXISTS PackageSalesStats (
                packageID INTEGER PRIMARY KEY,
                totalSales INTEGER DEFAULT 0,
                revenue INTEGER DEFAULT 0,
                FOREIGN KEY (packageID) REFERENCES Membership_package(id)
            );
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            logger.info("PackageSalesStats table created or already exists.");
        } catch (SQLException e) {
            logger.warning("❌ Table creation error: " + e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.warning("❌ Close connection error: " + e.getMessage());
        }
    }

    public void increaseSales(int packageID) {
        getConnection();
        String sql = """
            INSERT INTO PackageSalesStats (packageID, totalSales, revenue)
            VALUES (?, 1, (SELECT price FROM Membership_package WHERE id = ?))
            ON CONFLICT(packageID) DO UPDATE SET
                totalSales = totalSales + 1,
                revenue = revenue + (SELECT price FROM Membership_package WHERE id = ?);
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, packageID);
            stmt.setInt(2, packageID);
            stmt.setInt(3, packageID);
            stmt.executeUpdate();
            logger.info("Updated sales successfully for packageID: " + packageID);
        } catch (SQLException e) {
            logger.warning("❌ Sales update error: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public List<PackageSalesStats> getAllStats() {
        getConnection();
        List<PackageSalesStats> list = new ArrayList<>();
        String sql = """
            SELECT 
                mp.id, 
                mp.name, 
                IFNULL(ps.totalSales, 0) AS totalSales,
                (IFNULL(ps.totalSales, 0) * mp.price) AS revenue
            FROM Membership_package mp
            LEFT JOIN PackageSalesStats ps ON mp.id = ps.packageID
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new PackageSalesStats(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("totalSales"),
                        rs.getInt("revenue")
                ));
            }

        } catch (SQLException e) {
            logger.warning("❌ Query error: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return list;
    }

    public List<PackageSalesStats> searchPackagesByID(String keyword) {
        getConnection(); // dùng kết nối nội bộ
        List<PackageSalesStats> list = new ArrayList<>();
        String sql = """
        SELECT 
            mp.id, 
            mp.name, 
            IFNULL(ps.totalSales, 0) AS totalSales,
            (IFNULL(ps.totalSales, 0) * mp.price) AS revenue
        FROM Membership_package mp
        LEFT JOIN PackageSalesStats ps ON mp.id = ps.packageID
        WHERE CAST(mp.id AS TEXT) LIKE ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new PackageSalesStats(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("totalSales"),
                        rs.getInt("revenue")
                ));
            }
        } catch (SQLException e) {
            logger.warning("❌ Search by ID error: " + e.getMessage());
        } finally {
            closeConnection();
        }
        return list;
    }


}
