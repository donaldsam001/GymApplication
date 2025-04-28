package com.example.temp.DAO;

import com.example.temp.Models.MembershipPackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MembershipPackageDAO {

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
        String query = "CREATE TABLE IF NOT EXISTS Membership_package (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "description TEXT, " +
                "exp INTEGER NOT NULL, " +
                "status INTEGER NOT NULL CHECK (status IN (0, 1))" +
                ")";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    public void insertMembershipPackage(MembershipPackage memPackage) {
        getConnection();
        String sql = "INSERT INTO Membership_package (id, name, price, description, exp, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memPackage.getId());
            stmt.setString(2, memPackage.getName());
            stmt.setFloat(3, memPackage.getPrice());
            stmt.setString(4, memPackage.getDescription());
            stmt.setInt(5, memPackage.getExp());
            stmt.setBoolean(6, memPackage.getStatus());
            stmt.executeUpdate();
            logger.info("Inserted MembershipPackage successfully.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public List<MembershipPackage> getData() {
        getConnection();
        String query = "select * from Membership_package";
        List<MembershipPackage> List = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                float price = rs.getFloat("price");
                String description = rs.getString("description");
                int exp = rs.getInt("exp");
                boolean status = rs.getBoolean("status");
                List.add(new MembershipPackage(id, name, price, description, exp, status));
            }

            closeConnection();
        }catch(SQLException e) {
            logger.info(e.toString());
        }

        return List;
    }

    public void updateMembershipPackage(MembershipPackage memPackage) {
        getConnection();
        String sql = "UPDATE Membership_package SET name = ?, price = ?, description = ?, exp = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, memPackage.getName());
            stmt.setFloat(2, memPackage.getPrice());
            stmt.setString(3, memPackage.getDescription());
            stmt.setInt(4, memPackage.getExp());
            stmt.setBoolean(5, memPackage.getStatus());
            stmt.setInt(6, memPackage.getId());

            stmt.executeUpdate();
            logger.info("Updated MembershipPackage successfully.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public void deleteMembershipPackage(int id) {
        getConnection();
        String sql = "DELETE FROM Membership_package WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Deleted MembershipPackage with id: " + id);
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public void deleteAllPackages() {
        getConnection();
        String sql = "DELETE FROM Membership_package";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            logger.info("All membership packages deleted.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public List<MembershipPackage> searchPackages(String keyword) {
        getConnection();
        List<MembershipPackage> list = new ArrayList<>();
        String sql = "SELECT * FROM Membership_package WHERE name LIKE ? OR id LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                float price = rs.getFloat("price");
                String description = rs.getString("description");
                int exp = rs.getInt("exp");
                boolean status = rs.getBoolean("status");

                list.add(new MembershipPackage(id, name, price, description, exp, status));
            }
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
        return list;
    }

    public boolean isPackageExists(int id) {
        getConnection();
        String sql = "SELECT COUNT(*) FROM Membership_package WHERE id = ?";
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
