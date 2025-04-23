package com.example.temp.DAO;

import com.example.temp.Models.Equipment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EquipmentDAO {
    private static final String DB_URL = "jdbc:sqlite:service_app.db";
    private static final Logger logger = Logger.getLogger(EquipmentDAO.class.getName());

    public EquipmentDAO() {
        createTableIfNotExists();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS Equipment (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT,
                    repairDate TEXT,
                    repairNote TEXT,
                    status TEXT NOT NULL
                )
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.info("Equipment table ensured.");
        } catch (SQLException e) {
            logger.warning("Failed to create table: " + e.getMessage());
        }
    }

    public void insertEquipment(Equipment equipment) {
        String sql = "INSERT INTO Equipment (name, description, repairDate, repairNote, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getDescription());
            stmt.setString(3, equipment.getRepairDate() != null ? equipment.getRepairDate().toString() : null);
            stmt.setString(4, equipment.getMaintenanceNote());
            stmt.setString(5, equipment.getStatus());

            stmt.executeUpdate();
            logger.info("Equipment inserted successfully.");
        } catch (SQLException e) {
            logger.warning("Insert failed: " + e.getMessage());
        }
    }

    public List<Equipment> getAllEquipment() {
        List<Equipment> list = new ArrayList<>();
        String sql = "SELECT * FROM Equipment";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Equipment eq = new Equipment(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("repairDate") != null ? LocalDate.parse(rs.getString("repairDate")) : null,
                        rs.getString("status"),
                        rs.getString("repairNote")
                );
                list.add(eq);
            }
        } catch (SQLException e) {
            logger.warning("Get all equipment failed: " + e.getMessage());
        }

        return list;
    }

    public void updateEquipment(Equipment equipment) {
        String sql = "UPDATE Equipment SET name = ?, description = ?, repairDate = ?, repairNote = ?, status = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getDescription());
            stmt.setString(3, equipment.getRepairDate() != null ? equipment.getRepairDate().toString() : null);
            stmt.setString(4, equipment.getMaintenanceNote());
            stmt.setString(5, equipment.getStatus());
            stmt.setInt(6, equipment.getId());

            stmt.executeUpdate();
            logger.info("Equipment updated successfully.");
        } catch (SQLException e) {
            logger.warning("Update failed: " + e.getMessage());
        }
    }

    public void deleteEquipment(int id) {
        String sql = "DELETE FROM Equipment WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Equipment with ID " + id + " deleted.");
        } catch (SQLException e) {
            logger.warning("Delete failed: " + e.getMessage());
        }
    }

    public void scheduleMaintenance(int id, LocalDate date, String note) {
        String sql = "UPDATE Equipment SET repairDate = ?, repairNote = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, date.toString());
            stmt.setString(2, note);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            logger.info("Maintenance scheduled for equipment ID: " + id);
        } catch (SQLException e) {
            logger.warning("Schedule maintenance failed: " + e.getMessage());
        }
    }

    public void updateRepairDate(int id, LocalDate repairDate) {
        String sql = "UPDATE equipment SET repairDate = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, repairDate != null ? repairDate.toString() : null);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
