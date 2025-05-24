package com.example.temp.DAO;

import com.example.temp.Models.Admin;
import java.sql.*;
import java.util.logging.Logger;

public class AdminDAO {

    private Connection connection;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:service_app.db");
                logger.info("Connected to database");
            }
        } catch (SQLException e) {
            logger.warning(e.toString());
        }
    }

    public Admin getAdminInf(int id) {
        getConnection();
        String sql = "SELECT * FROM Admin WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
