package com.example.temp.DAO;

import com.example.temp.Models.PackageSale;
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

    public void addSale(int packageID, int totalPrice, String saleDate) {
        getConnection();
        String sql = "INSERT INTO package_sales (packageID, total_price, sale_date) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, packageID);
            stmt.setInt(2, totalPrice);
            stmt.setString(3, saleDate);
            stmt.executeUpdate();
            logger.info("✅ Added sale record for packageID: " + packageID);
        } catch (SQLException e) {
            logger.warning("❌ Error adding sale: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public List<PackageSalesStats> getRevenueByMonth(int year) {
        getConnection();
        List<PackageSalesStats> statsList = new ArrayList<>();
        String sql = """
            SELECT strftime('%m', sale_date) AS timeUnit,
                   COUNT(*) AS totalSales,
                   SUM(total_price) AS revenue
            FROM package_sales
            WHERE strftime('%Y', sale_date) = ?
            GROUP BY timeUnit
            ORDER BY timeUnit
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(year));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    statsList.add(new PackageSalesStats(
                            0,
                            "Tháng " + rs.getString("timeUnit"),
                            rs.getInt("totalSales"),
                            rs.getInt("revenue")
                    ));
                }
            }
        } catch (SQLException e) {
            logger.warning("❌ Error retrieving monthly revenue: " + e.getMessage());
        } finally {
            closeConnection();
        }
        return statsList;
    }

    public List<PackageSalesStats> getRevenueByQuarter(int year) {
        getConnection();
        List<PackageSalesStats> statsList = new ArrayList<>();
        String sql = """
            SELECT
                CASE
                    WHEN CAST(strftime('%m', sale_date) AS INTEGER) BETWEEN 1 AND 3 THEN 'Quý 1'
                    WHEN CAST(strftime('%m', sale_date) AS INTEGER) BETWEEN 4 AND 6 THEN 'Quý 2'
                    WHEN CAST(strftime('%m', sale_date) AS INTEGER) BETWEEN 7 AND 9 THEN 'Quý 3'
                    ELSE 'Quý 4'
                END AS timeUnit,
                COUNT(*) AS totalSales,
                SUM(total_price) AS revenue
            FROM package_sales
            WHERE strftime('%Y', sale_date) = ?
            GROUP BY timeUnit
            ORDER BY timeUnit
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(year));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    statsList.add(new PackageSalesStats(
                            0,
                            rs.getString("timeUnit"),
                            rs.getInt("totalSales"),
                            rs.getInt("revenue")
                    ));
                }
            }
        } catch (SQLException e) {
            logger.warning("❌ Error retrieving quarterly revenue: " + e.getMessage());
        } finally {
            closeConnection();
        }
        return statsList;
    }

    public List<PackageSalesStats> getRevenueByYear() {
        getConnection();
        List<PackageSalesStats> statsList = new ArrayList<>();
        String sql = """
            SELECT strftime('%Y', sale_date) AS timeUnit,
                   COUNT(*) AS totalSales,
                   SUM(total_price) AS revenue
            FROM package_sales
            GROUP BY timeUnit
            ORDER BY timeUnit
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                statsList.add(new PackageSalesStats(
                        0,
                        rs.getString("timeUnit"),
                        rs.getInt("totalSales"),
                        rs.getInt("revenue")
                ));
            }
        } catch (SQLException e) {
            logger.warning("❌ Error retrieving yearly revenue: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return statsList;
    }

    public List<PackageSalesStats> searchPackagesByID(String keyword) {
        getConnection();
        List<PackageSalesStats> statsList = new ArrayList<>();
        String sql = """
            SELECT 
                mp.id AS packageID,
                mp.name AS packageName,
                COUNT(ps.id) AS totalSales,
                IFNULL(SUM(ps.total_price), 0) AS revenue
            FROM Membership_package mp
            LEFT JOIN package_sales ps ON mp.id = ps.packageID
            WHERE CAST(mp.id AS TEXT) LIKE ? OR mp.name LIKE ?
            GROUP BY mp.id, mp.name
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    statsList.add(new PackageSalesStats(
                            rs.getInt("packageID"),
                            rs.getString("packageName"),
                            rs.getInt("totalSales"),
                            rs.getInt("revenue")
                    ));
                }
            }
        } catch (SQLException e) {
            logger.warning("❌ Error searching packages: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return statsList;
    }

    public List<PackageSalesStats> getAllStats() {
        getConnection();
        List<PackageSalesStats> statsList = new ArrayList<>();
        String sql = """
            SELECT 
                mp.id AS packageID,
                mp.name AS packageName,
                COUNT(ps.id) AS totalSales,
                IFNULL(SUM(ps.total_price), 0) AS revenue
            FROM Membership_package mp
            LEFT JOIN package_sales ps ON mp.id = ps.packageID
            GROUP BY mp.id, mp.name
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                statsList.add(new PackageSalesStats(
                        rs.getInt("packageID"),
                        rs.getString("packageName"),
                        rs.getInt("totalSales"),
                        rs.getInt("revenue")
                ));
            }
        } catch (SQLException e) {
            logger.warning("❌ Error retrieving stats: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return statsList;
    }


    public void increaseSales(int packageID) {
        getConnection();
        String sql = """
            INSERT INTO PackageSalesStats (packageID, totalSales, revenue)
            VALUES (?, 1, (SELECT price FROM Membership_package WHERE id = ?))
            ON CONFLICT(packageID) DO UPDATE SET
                totalSales = totalSales + 1,
                revenue = revenue + (SELECT price FROM Membership_package WHERE id = ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, packageID);
            stmt.setInt(2, packageID);
            stmt.setInt(3, packageID);
            stmt.executeUpdate();
            logger.info("Updated sales for packageID: " + packageID);
        } catch (SQLException e) {
            logger.warning("❌ Error updating sales: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
}
