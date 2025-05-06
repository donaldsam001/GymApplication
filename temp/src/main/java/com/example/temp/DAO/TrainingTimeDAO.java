package com.example.temp.DAO;

import com.example.temp.Models.TrainingTime;
import com.example.temp.Utils.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainingTimeDAO {

    public static List<TrainingTime> getAllTrainingTimes() {
        List<TrainingTime> list = new ArrayList<>();
        String sql = "SELECT * FROM TrainingTime ORDER BY id DESC";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new TrainingTime(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
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

    public static void debugUnfinishedCheckIn(String phone) {
        String sql = "SELECT * FROM TrainingTime WHERE phone = ? AND checkOutTime IS NULL";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("🔐 CHƯA CHECKOUT: " +
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("checkInTime"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi debug: " + e.getMessage());
        }
    }

    public static boolean insertCheckIn(TrainingTime trainingTime) {
        String sql = "INSERT INTO TrainingTime (id, name, phone, checkInTime, note) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trainingTime.getId());
            stmt.setString(2, trainingTime.getName());
            stmt.setString(3, trainingTime.getPhone());
            stmt.setString(4, trainingTime.getCheckInTime());
            stmt.setString(5, trainingTime.getNote());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi check-in: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertCheckOut(String phone, String checkOutTime) {
        String sqlSelect = "SELECT MAX(id) AS id FROM TrainingTime WHERE phone = ? AND checkOutTime IS NULL";
        String sqlUpdate = "UPDATE TrainingTime SET checkOutTime = ? WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setString(1, phone);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                if (rs.wasNull()) {
                    System.out.println("❌ Không có bản ghi nào để check-out.");
                    return false;
                }

                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setString(1, checkOutTime);
                    stmtUpdate.setInt(2, id);
                    int rows = stmtUpdate.executeUpdate();

                    if (rows > 0) {
                        System.out.println("✅ Đã check-out cho ID: " + id);
                        return true;
                    } else {
                        System.out.println("❌ Không cập nhật được check-out.");
                        return false;
                    }
                }
            } else {
                System.out.println("❌ Không có bản ghi nào để check-out.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi check-out: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateNoteByPhone(String phone, String note) {
        String sqlSelect = "SELECT MAX(id) AS id FROM TrainingTime WHERE phone = ?";
        String sqlUpdate = "UPDATE TrainingTime SET note = ? WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setString(1, phone);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                if (rs.wasNull()) {
                    System.out.println("❌ Không có bản ghi nào để cập nhật ghi chú.");
                    return false;
                }

                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setString(1, note);
                    stmtUpdate.setInt(2, id);
                    int rows = stmtUpdate.executeUpdate();
                    return rows > 0;
                }
            } else {
                System.out.println("❌ Không có bản ghi nào để cập nhật ghi chú.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi cập nhật ghi chú: " + e.getMessage());
            return false;
        }
    }

    public static boolean hasUnfinishedCheckIn(String phone) {
        String sql = "SELECT 1 FROM TrainingTime WHERE phone = ? AND checkOutTime IS NULL";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi kiểm tra check-in chưa hoàn tất: " + e.getMessage());
            return false;
        }
    }

    public static int getNextId() {
        String sql = "SELECT MAX(id) + 1 AS nextId FROM TrainingTime";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("nextId");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy ID tiếp theo: " + e.getMessage());
        }
        return 1;
    }
}