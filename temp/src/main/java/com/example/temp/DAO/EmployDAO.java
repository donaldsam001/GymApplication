package com.example.temp.DAO;



import com.example.temp.Models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EmployDAO {

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
        String query = "CREATE TABLE IF NOT EXISTS Employee (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "role TEXT NOT NULL)" ;

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

    public void insertEmployee(Employee employee) {
        getConnection();
        String sql = "INSERT INTO Employee (id, name, password, phone, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getPassword());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getRole());
            stmt.executeUpdate();
            logger.info("Inserted MembershipPackage successfully.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

//    public void updateMembershipPackage(MembershipPackage memPackage) {
//        getConnection();
//        String sql = "UPDATE Membership_package SET name = ?, price = ?, description = ?, exp = ?, status = ? WHERE id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, memPackage.getName());
//            stmt.setFloat(2, memPackage.getPrice());
//            stmt.setString(3, memPackage.getDescription());
//            stmt.setInt(4, memPackage.getExp());
//            stmt.setBoolean(5, memPackage.getStatus());
//            stmt.setInt(6, memPackage.getId());
//
//            stmt.executeUpdate();
//            logger.info("Updated MembershipPackage successfully.");
//        } catch (SQLException e) {
//            logger.warning(e.toString());
//        } finally {
//            closeConnection();
//        }
//    }
//
//    public void deleteMembershipPackage(int id) {
//        getConnection();
//        String sql = "DELETE FROM Membership_package WHERE id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, id);
//            stmt.executeUpdate();
//            logger.info("Deleted MembershipPackage with id: " + id);
//        } catch (SQLException e) {
//            logger.warning(e.toString());
//        } finally {
//            closeConnection();
//        }
//    }
//
public List<Employee> getAllPackages() {
    List<Employee> packages = new ArrayList<>();
    String url = "jdbc:sqlite:service_app.db";
    String sql = "SELECT * FROM Employee";

    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String password = rs.getString("password");
            String phone =rs.getString("phone");
            String role =        rs.getString("role");
            packages.add(new Employee( id, name,  password,  phone , role));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return packages;
}
}
