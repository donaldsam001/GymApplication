package com.example.temp.DAO;



import com.example.temp.Models.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class MemberDAO {

    private static final String DB_URL = "jdbc:sqlite:service_app.db";

    public static void insertMember(Member member) {
        String sql = "INSERT INTO MemberDetail (id, name, phone, gender, schedule, start_date, end_date, age) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getCustomerID());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getGender());
            pstmt.setString(5, member.getSchedule());
            pstmt.setString(6, member.getStartDate() != null ? member.getStartDate().toString() : null);
            pstmt.setString(7, member.getEndDate() != null ? member.getEndDate().toString() : null);
            pstmt.setInt(8, member.getAge());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMember(Member member) {
        String sql = "UPDATE MemberDetail SET name = ?, phone = ?, gender = ?, schedule = ?, " +
                "start_date = ?, end_date = ?, age = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getPhone());
            pstmt.setString(3, member.getGender());
            pstmt.setString(4, member.getSchedule());
            pstmt.setString(5, member.getStartDate() != null ? member.getStartDate().toString() : null);
            pstmt.setString(6, member.getEndDate() != null ? member.getEndDate().toString() : null);
            pstmt.setInt(7, member.getAge());
            pstmt.setString(8, member.getCustomerID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMember(String id) {
        String sql = "DELETE FROM MemberDetail WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Member> getAllMembers() {
        ObservableList<Member> members = FXCollections.observableArrayList();
        String sql = "SELECT * FROM MemberDetail";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                members.add(new Member(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("schedule"),
                        rs.getString("start_date") != null ? LocalDate.parse(rs.getString("start_date")) : null,
                        rs.getString("end_date") != null ? LocalDate.parse(rs.getString("end_date")) : null,
                        rs.getInt("age")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }
}
