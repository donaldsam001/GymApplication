package com.example.temp.DAO;

import com.example.temp.Models.MemberCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberCardDAO {

    private static final String DB_URL = "jdbc:sqlite:service_app.db";
    private static String lastError = "";

    public static boolean insertMemberCard(MemberCard card) {
        String sql = "INSERT INTO MemberCard (customerID, packageID, startDate, endDate, name, name, exp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, card.getCustomerID());
            pstmt.setInt(2, card.getPackageID());
            pstmt.setString(3, card.getStartDate());
            pstmt.setString(4, card.getEndDate());
            pstmt.setString(5, card.getPackageName());
            pstmt.setString(6, card.getCustomerName());
            pstmt.setInt(7, card.getExp());

            pstmt.executeUpdate();
            lastError = "";
            return true;

        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi thêm thẻ hội viên: " + e.getMessage());
            return false;
        }
    }

    public static String getLastError() {
        return lastError.isEmpty() ? "Không có lỗi cụ thể." : lastError;
    }

    public static boolean isCustomerIDExists(int customerID) {
        String sql = "SELECT COUNT(*) FROM MemberCard WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerID);
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
                MemberCard card = new MemberCard(
                        rs.getInt("customerID"),
                        rs.getInt("packageID"),
                        rs.getString("name"),
                        rs.getString("package"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("exp")
                );
                cards.add(card);
            }

        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi lấy danh sách thẻ hội viên: " + e.getMessage());
        }
        return cards;
    }

    public static boolean updateMemberCardEndDate(int customerID, String newEndDate) {
        String sql = "UPDATE MemberCard SET endDate = ? WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newEndDate);
            pstmt.setInt(2, customerID);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi cập nhật ngày hết hạn: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteMemberCard(int customerID) {
        String sql = "DELETE FROM MemberCard WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerID);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi xóa thẻ hội viên: " + e.getMessage());
            return false;
        }
    }
}
