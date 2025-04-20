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
        String query = "CREATE TABLE IF NOT EXISTS Equipment (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "repairDate TEXT, " +
                "status TEXT NOT NULL)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.executeUpdate();
            logger.info("Equipment table created or already exists.");
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

    public  void insertEquipment(Equipment equipment) {
        getConnection();
        String sql = "INSERT INTO Equipment (name, description, repairDate, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getDescription());
            stmt.setString(3, equipment.getRepairDate() != null ? equipment.getRepairDate().toString() : null);
            stmt.setString(4, equipment.getStatus());
            stmt.executeUpdate();
            logger.info("Inserted Equipment successfully.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public  List<Equipment> getAllEquipment() {
        getConnection();
        List<Equipment> list = new ArrayList<>();
        String sql = "SELECT * FROM Equipment";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Equipment eq = new Equipment(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("repairDate") != null ? LocalDate.parse(rs.getString("repairDate")) : null,
                        rs.getString("status")
                );
                list.add(eq);
            }

        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }

        return list;
    }

    public void updateEquipment(Equipment equipment) {
        getConnection();
        String sql = "UPDATE Equipment SET name = ?, description = ?, repairDate = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getDescription());
            stmt.setString(3, equipment.getRepairDate() != null ? equipment.getRepairDate().toString() : null);
            stmt.setString(4, equipment.getStatus());
            stmt.setInt(5, equipment.getId());
            stmt.executeUpdate();
            logger.info("Updated Equipment successfully.");
        } catch (SQLException e) {
            logger.warning(e.toString());
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
            logger.info("Deleted Equipment with id: " + id);
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }
}
