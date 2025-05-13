package com.example.temp.DAO;

import com.example.temp.Models.Membership;
import com.example.temp.Models.TrainingTime;
import com.example.temp.Utils.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainingTimeDAO {

    // Thêm bản ghi check-in vào cơ sở dữ liệu
    public static boolean insertCheckIn(TrainingTime trainingTime) {
        String sql = "INSERT INTO TrainingTime (customerID, customerName, checkInTime, note) VALUES (?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trainingTime.getCustomerID());  // customerID là int
            stmt.setString(2, trainingTime.getCustomerName());
            stmt.setString(3, trainingTime.getCheckInTime());
            stmt.setString(4, trainingTime.getNote());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có bản ghi được thêm
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi thêm check-in: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertCheckOut(int customerID, String checkOutTime, String additionalNote) {
        String selectSql = "SELECT id, note FROM TrainingTime WHERE customerID = ? AND checkOutTime IS NULL ORDER BY id DESC LIMIT 1";
        String updateSql = "UPDATE TrainingTime SET checkOutTime = ?, note = ? WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, customerID);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String currentNote = rs.getString("note");
                String combinedNote = (currentNote != null ? currentNote + " | " : "") + additionalNote;

                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, checkOutTime);
                    updateStmt.setString(2, combinedNote);
                    updateStmt.setInt(3, id);
                    return updateStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi cập nhật check-out: " + e.getMessage());
        }
        return false;
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
                        rs.getString("customerName"),
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

    public static boolean deleteTrainingHistoryByCustomerID(int customerID) {
        String sql = "DELETE FROM TrainingTime WHERE customerID = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có dòng bị xóa
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi xóa lịch sử check-in/out: " + e.getMessage());
            return false;
        }
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

    public static List<TrainingTime> searchTrainingTimes(String keyword) {
        List<TrainingTime> results = new ArrayList<>();
        String query = "SELECT * FROM TrainingTime WHERE customerName LIKE ? OR customerID LIKE ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String likeKeyword = "%" + keyword + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new TrainingTime(
                        rs.getInt("customerID"),
                        rs.getString("customerName"),
                        rs.getString("checkInTime"),
                        rs.getString("checkOutTime"),
                        rs.getString("note")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

}
