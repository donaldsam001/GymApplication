package com.example.temp.DAO;

import com.example.temp.Models.MemberCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberCardDAO {

    private static final String DB_URL = "jdbc:sqlite:service_app.db";
    private static String lastError = "";

    // Thêm thẻ hội viên
    public static boolean insertMemberCard(MemberCard card) {
        String sql = """
            INSERT INTO MemberCard (customerID, packageID, name, package, startDate, endDate, exp)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, card.getCustomerID());
            pstmt.setInt(2, card.getPackageID()); // packageID (từ superclass MembershipPackage)
            pstmt.setString(3, card.getCustomerName());
            pstmt.setString(4, card.getPackageName()); // package name (từ MembershipPackage)
            pstmt.setString(5, card.getStartDate());
            pstmt.setString(6, card.getEndDate());
            pstmt.setInt(7, card.getExp()); // from superclass if có dùng

            pstmt.executeUpdate();
            lastError = "";
            return true;

        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi thêm thẻ hội viên: " + e.getMessage());
            return false;
        }
    }

    // Lấy thông tin lỗi
    public static String getLastError() {
        return lastError.isEmpty() ? "Không có lỗi cụ thể." : lastError;
    }

    // Kiểm tra tồn tại theo customerID
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

    // Lấy danh sách thẻ hội viên
    public static List<MemberCard> getAllMemberCards() {
        List<MemberCard> cards = new ArrayList<>();
        String sql = """
            SELECT mc.customerID, mc.startDate, mc.endDate, mc.name AS customerName,
                   mp.id AS packageID, mp.name AS packageName
            FROM MemberCard mc
            JOIN Membership_package mp ON mc.packageID = mp.id
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MemberCard card = new MemberCard(
                        rs.getInt("customerID"),
                        rs.getString("customerName"),
                        rs.getInt("packageID"),
                        rs.getString("packageName"),
                        rs.getString("startDate"),
                        rs.getString("endDate")
                );
                cards.add(card);
            }

        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("❌ Lỗi khi lấy danh sách thẻ hội viên: " + e.getMessage());
        }
        return cards;
    }

    // Cập nhật ngày hết hạn của thẻ
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

    // Xóa thẻ hội viên theo customerID
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
