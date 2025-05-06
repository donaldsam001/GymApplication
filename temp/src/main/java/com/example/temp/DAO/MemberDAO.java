package com.example.temp.DAO;

import com.example.temp.Models.Member;
import com.example.temp.Utils.SQLiteConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM MemberDetail";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(new Member(
                        rs.getString("customerID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("schedule"),
                        LocalDate.parse(rs.getString("startDate"), DATE_FORMATTER),
                        LocalDate.parse(rs.getString("endDate"), DATE_FORMATTER),
                        rs.getInt("age")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching members: " + e.getMessage());
        }
        return members;
    }

    public static void addMember(Member member) {
        String sql = "INSERT INTO MemberDetail (customerID, name, phone, gender, schedule, startDate, endDate, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getCustomerID());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getGender());
            pstmt.setString(5, member.getSchedule());
            pstmt.setString(6, member.getStartDate().format(DATE_FORMATTER));
            pstmt.setString(7, member.getEndDate().format(DATE_FORMATTER));
            pstmt.setInt(8, member.getAge());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding member: " + e.getMessage());
        }
    }

    public static void updateMember(Member member) {
        String sql = "UPDATE MemberDetail SET name=?, phone=?, gender=?, schedule=?, startDate=?, endDate=?, age=? WHERE customerID=?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getPhone());
            pstmt.setString(3, member.getGender());
            pstmt.setString(4, member.getSchedule());
            pstmt.setString(5, member.getStartDate().format(DATE_FORMATTER));
            pstmt.setString(6, member.getEndDate().format(DATE_FORMATTER));
            pstmt.setInt(7, member.getAge());
            pstmt.setString(8, member.getCustomerID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
        }
    }

    public static void deleteMember(String customerID) {
        String sql = "DELETE FROM MemberDetail WHERE customerID = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
        }
    }
}