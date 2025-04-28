package com.example.temp.DAO;

import com.example.temp.Models.Equipment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EquipmentDAO {
    private Connection connection;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:service_app.db");
                logger.info("Connected to database");
                createTable();
            }
        } catch (SQLException e) {
            logger.warning(e.toString());
        }
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS Equipment (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT,
                    repairDate TEXT,
                    repairNote TEXT,
                    status INTEGER NOT NULL CHECK (status IN (0, 1))
                )
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            logger.info("Table created or already exists.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        }
    }

    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.warning(e.toString());
        }
    }

    public void insertEquipment(Equipment equipment) {
        getConnection();
        String sql = "INSERT INTO Equipment (id, name, description, repairDate, repairNote, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, equipment.getId());
            stmt.setString(2, equipment.getName());
            stmt.setString(3, equipment.getDescription());
            stmt.setString(4, equipment.getRepairDate() != null ? equipment.getRepairDate().toString() : null);
            stmt.setString(5, equipment.getMaintenanceNote());
            stmt.setBoolean(6, equipment.getStatus());
            stmt.executeUpdate();
            logger.info("Inserted Equipment successfully.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public List<Equipment> getAllEquipment() {
        getConnection();
        List<Equipment> list = new ArrayList<>();
        String sql = "SELECT * FROM Equipment";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Equipment eq = new Equipment(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("repairDate") != null ? LocalDate.parse(rs.getString("repairDate")) : null,
                        rs.getBoolean("status"),
                        rs.getString("repairNote")
                );
                list.add(eq);
            }
        } catch (SQLException e) {
            logger.warning("Get all equipment failed: " + e.getMessage());
        } finally {
            closeConnection();
        }
        return list;
    }

    public void updateEquipment(Equipment equipment) {
        getConnection();
        String sql = "UPDATE Equipment SET name = ?, description = ?, repairDate = ?, repairNote = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getDescription());
            stmt.setString(3, equipment.getRepairDate() != null ? equipment.getRepairDate().toString() : null);
            stmt.setString(4, equipment.getMaintenanceNote());
            stmt.setBoolean(5, equipment.getStatus());
            stmt.setInt(6, equipment.getId());
            stmt.executeUpdate();
            logger.info("Updated Equipment successfully.");
        } catch (SQLException e) {
            logger.warning("Update failed: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void deleteEquipment(int id) {
        getConnection();
        String sql = "DELETE FROM Equipment WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Deleted Equipment with ID: " + id);
        } catch (SQLException e) {
            logger.warning("Delete failed: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public List<Equipment> searchEquipment(String keyword) {
        getConnection();
        List<Equipment> list = new ArrayList<>();
        String sql = "SELECT * FROM Equipment WHERE name LIKE ? OR id LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Equipment eq = new Equipment(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("repairDate") != null ? LocalDate.parse(rs.getString("repairDate")) : null,
                        rs.getBoolean("status"),
                        rs.getString("repairNote")
                );
                list.add(eq);
            }
        } catch (SQLException e) {
            logger.warning("Search failed: " + e.getMessage());
        } finally {
            closeConnection();
        }
        return list;
    }

    public boolean isEquipmentIdExists(int id) {
        getConnection();
        String sql = "SELECT COUNT(*) FROM Equipment WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
        return false;
    }
}
