package com.example.temp.DAO;
import com.example.temp.Models.Member;

import com.example.temp.Utils.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    // Thêm hội viên vào cơ sở dữ liệu
    public static boolean addMember(Member member) {
        String sql = "INSERT INTO MemberDetail (customerID, name, phone, gender, age) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set các giá trị vào PreparedStatement
            stmt.setInt(1, member.getMemberID());
            stmt.setString(2, member.getMemberName());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getGender());
            stmt.setInt(5, member.getAge());

            // Thực thi câu lệnh SQL
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra nếu có dòng bị ảnh hưởng (nghĩa là thêm thành công)
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi thêm hội viên: " + e.getMessage());
            return false; // Nếu có lỗi, trả về false
        }
    }

    // Cập nhật thông tin hội viên trong cơ sở dữ liệu
    public static boolean updateMember(Member member) {
        String sql = "UPDATE MemberDetail SET name = ?, phone = ?, gender = ?, age = ? WHERE customerID = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getMemberName());
            stmt.setString(2, member.getPhone());
            stmt.setString(3, member.getGender());
            stmt.setInt(4, member.getAge());
            stmt.setInt(5, member.getMemberID());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi cập nhật hội viên: " + e.getMessage());
            return false;
        }
    }

    // Xóa hội viên khỏi cơ sở dữ liệu
    public static boolean deleteMember(int customerID) {
        String sql = "DELETE FROM MemberDetail WHERE customerID = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerID);

            int rowsAffected = stmt.executeUpdate();
            TrainingTimeDAO.deleteTrainingHistoryByCustomerID(customerID);
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi xóa hội viên: " + e.getMessage());
            return false;
        }
    }


    public static String getCustomerNameById(int customerID) {
        String sql = "SELECT name FROM MemberDetail WHERE customerID = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:service_app.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy tên hội viên: " + e.getMessage());
        }
        return null;
    }




    // Kiểm tra xem mã hội viên đã tồn tại trong cơ sở dữ liệu chưa
    public static boolean isCustomerIDExists(int customerID) {
        String sql = "SELECT 1 FROM MemberDetail WHERE customerID = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerID);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có bản ghi trả về true
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi kiểm tra mã hội viên: " + e.getMessage());
            return false;
        }
    }
    // Kiểm tra SDT đã tồn tại chưa, ngoại trừ một customerID cụ thể (dùng khi cập nhật)
    public static boolean isCustomerPhoneExists(String phone, int excludeCustomerID) {
        String sql = "SELECT 1 FROM MemberDetail WHERE phone = ? AND customerID != ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            stmt.setInt(2, excludeCustomerID);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có bản ghi => đã tồn tại
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi kiểm tra số điện thoại hội viên (bỏ qua ID): " + e.getMessage());
            return false;
        }
    }


    public static List<Member> getAllExtendedMembers() {
        List<Member> list = new ArrayList<>();
        String sql = """
        SELECT m.customerID, m.name, m.phone, m.gender, m.age,
               c.package AS packageName, c.startDate, c.endDate, c.exp
        FROM MemberDetail m
        LEFT JOIN MemberCard c ON m.customerID = c.customerID
    """;

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Member(
                        rs.getInt("customerID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("packageName"),
                        rs.getString("startDate"),
                        rs.getString("endDate")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy dữ liệu hội viên mở rộng: " + e.getMessage());
        }

        return list;
    }

    public static List<Member> searchMembers(String keyword) {
        List<Member> list = new ArrayList<>();
        String sql = """
        SELECT m.customerID, m.name, m.phone, m.gender, m.age,
               c.package AS packageName, c.startDate, c.endDate, c.exp
        FROM MemberDetail m
        LEFT JOIN MemberCard c ON m.customerID = c.customerID
        WHERE CAST(m.customerID AS TEXT) LIKE ? OR m.name LIKE ?
    """;

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Member(
                        rs.getInt("customerID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("packageName"),
                        rs.getString("startDate"),
                        rs.getString("endDate")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi tìm kiếm hội viên: " + e.getMessage());
        }

        return list;
    }


}
