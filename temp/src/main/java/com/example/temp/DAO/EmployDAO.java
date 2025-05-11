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
                "id INTEGER PRIMARY KEY , " +
                "name TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "phone TEXT NOT NULL, ";
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
        String sql = "INSERT INTO Employee (id, name, password, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getPassword());
            stmt.setString(4, employee.getPhone());
            stmt.executeUpdate();
            logger.info("Inserted Employee successfully.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:service_app.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                employees.add(new Employee(id, name, password, phone));
            }
        } catch (SQLException e) {
            logger.warning(e.toString());
        }
        return employees;
    }

    public void updateEmployee(Employee employee) {
        getConnection();
        String sql = "UPDATE Employee SET name = ?, phone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPhone());

            stmt.setInt(3, employee.getId());
            stmt.executeUpdate();
            logger.info("Updated Employee successfully.");
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public void deleteEmployee(int id) {
        getConnection();
        String sql = "DELETE FROM Employee WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Deleted Employee with ID " + id);
        } catch (SQLException e) {
            logger.warning(e.toString());
        } finally {
            closeConnection();
        }
    }

    public List<Employee> searchEmployee(String keyword) {
        List<Employee> result = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE name LIKE ? OR phone LIKE ? OR id LIKE ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:service_app.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            logger.warning(e.toString());
        }
        return result;
    }

    public boolean isEmployeeIdExists(int id) {
        getConnection();
        String sql = "SELECT COUNT(*) FROM Employee WHERE id = ?";
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

    public Employee getEmployeeById(int id) {
        getConnection();
        String sql = "SELECT * FROM Employee WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
