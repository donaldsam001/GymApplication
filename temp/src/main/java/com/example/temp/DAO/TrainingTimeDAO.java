package com.example.temp.DAO;

import com.example.temp.Models.TrainingTime;
import com.example.temp.Utils.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainingTimeDAO {

    // Thêm bản ghi check-in vào cơ sở dữ liệu
    public static boolean insertCheckIn(TrainingTime trainingTime) {
        String sql = "INSERT INTO TrainingTime (customerID, checkInTime, note) VALUES (?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trainingTime.getCustomerID());  // customerID là int
            stmt.setString(2, trainingTime.getCheckInTime());
            stmt.setString(3, trainingTime.getNote());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có bản ghi được thêm
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi thêm check-in: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật check-out time cho hội viên
    public static boolean insertCheckOut(int customerID, String checkOutTime) {
        String sqlSelect = "SELECT MAX(customerID) AS lastId FROM TrainingTime WHERE customerID = ? AND checkOutTime IS NULL";
        String sqlUpdate = "UPDATE TrainingTime SET checkOutTime = ? WHERE customerID = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setInt(1, customerID);  // customerID là int
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                int lastId = rs.getInt("lastId");
                if (lastId == 0) {
                    System.out.println("❌ Không có bản ghi check-in chưa được check-out.");
                    return false;
                }

                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setString(1, checkOutTime);
                    stmtUpdate.setInt(2, lastId);
                    int rows = stmtUpdate.executeUpdate();
                    return rows > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi check-out: " + e.getMessage());
            return false;
        }
    }

    // Kiểm tra xem hội viên có đang có check-in mà chưa check-out không
    public static boolean hasUnfinishedCheckIn(int customerID) {
        String sql = "SELECT 1 FROM TrainingTime WHERE customerID = ? AND checkOutTime IS NULL";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerID);  // customerID là int
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có bản ghi chưa check-out, trả về true
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi kiểm tra check-in chưa hoàn tất: " + e.getMessage());
            return false;
        }
    }

    // Lấy tất cả bản ghi TrainingTime từ cơ sở dữ liệu
    public static List<TrainingTime> getAllTrainingTimes() {
        List<TrainingTime> list = new ArrayList<>();
        String sql = "SELECT * FROM TrainingTime ORDER BY customerID DESC";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new TrainingTime(
                        rs.getInt("customerID"),  // customerID là int
                        rs.getString("checkInTime"),
                        rs.getString("checkOutTime"),
                        rs.getString("note")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy danh sách TrainingTime: " + e.getMessage());
        }
        return list;
    }

    // Lấy ID tiếp theo cho bản ghi mới
    public static int getNextId() {
        String sql = "SELECT MAX(customerID) + 1 AS nextId FROM TrainingTime";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("nextId");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy ID tiếp theo: " + e.getMessage());
        }
        return 1; // Nếu không có bản ghi nào, trả về 1
    }
}
