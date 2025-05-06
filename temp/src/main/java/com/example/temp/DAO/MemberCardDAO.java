package com.example.temp.DAO;

import com.example.temp.Models.MemberCard;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberCardDAO {

    private static final String DB_URL = "jdbc:sqlite:service_app.db";
    private static String lastError = ""; // Store the last error message

    public static boolean insertMemberCard(MemberCard card) {
        String sql = "INSERT INTO MemberCard (customerID, name, phone, gender, startDate, endDate, goi, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, card.getCustomerID());
            pstmt.setString(2, card.getName());
            pstmt.setString(3, card.getPhone());
            pstmt.setString(4, card.getGender());
            pstmt.setString(5, card.getStartDate());
            pstmt.setString(6, card.getEndDate());
            pstmt.setString(7, card.getGoi());
            pstmt.setString(8, card.getPrice());
            pstmt.executeUpdate();
            lastError = "";
            return true;
        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi thêm thẻ hội viên: " + e.getMessage() + " - SQL State: " + e.getSQLState());
            return false;
        }
    }

    public static String getLastError() {
        return lastError.isEmpty() ? "Không có lỗi cụ thể." : lastError;
    }

    public static boolean isCustomerIDExists(String customerID) {
        String sql = "SELECT COUNT(*) FROM MemberCard WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerID);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi kiểm tra customerID: " + e.getMessage());
            return false;
        }
    }

    public static List<MemberCard> getAllMemberCards() {
        List<MemberCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM MemberCard";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cards.add(new MemberCard(
                        rs.getString("customerID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("goi"),
                        rs.getString("price")
                ));
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi lấy danh sách thẻ: " + e.getMessage());
        }
        return cards;
    }

    public static boolean updateMemberCardEndDate(String customerID, String newEndDate) {
        String sql = "UPDATE MemberCard SET endDate = ? WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newEndDate);
            stmt.setString(2, customerID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi cập nhật ngày hết hạn: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteMemberCard(String customerID) {
        String sql = "DELETE FROM MemberCard WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi xóa thẻ hội viên: " + e.getMessage());
            return false;
        }
    }
}