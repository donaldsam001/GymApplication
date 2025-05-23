package com.example.temp.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.File;
import java.sql.SQLException;


public class DB {
    private static final String DB_URL = "jdbc:sqlite:service_app.db";

    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Admin table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Admin (
                        id INTEGER PRIMARY KEY,
                        name TEXT NOT NULL,
                        password TEXT NOT NULL,
                        phone TEXT NOT NULL, 
                        email TEXT NOT NULL
                    );
                """);

                // Employee table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Employee (
                        id INTEGER PRIMARY KEY,
                        name TEXT NOT NULL,
                        password TEXT ,
                        phone TEXT NOT NULL, 
                        isReceptionist INTEGER NOT NULL CHECK (isReceptionist IN (0, 1))
                                                                        );
                """);

                // Membership package table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Membership_package (
                                id INTEGER PRIMARY KEY ,
                                name TEXT NOT NULL,
                                price INTEGER NOT NULL,
                                description TEXT,
                                exp INTEGER NOT NULL,
                                status INTEGER NOT NULL CHECK (status IN (0, 1))
                    );
                """);


                // Equipment table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Equipment (
                        id INTEGER PRIMARY KEY ,
                        name TEXT NOT NULL,
                        description TEXT,
                        repairNote TEXT, 
                        repairDate TEXT,
                        status INTEGER NOT NULL CHECK (status IN (0, 1))
                    )
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS MemberDetail (
                        customerID INTEGER PRIMARY KEY,
                        name TEXT NOT NULL,
                        phone TEXT NOT NULL,
                        gender TEXT NOT NULL,
                        age INTEGER
                    );
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS MemberCard (
                                customerID INTEGER PRIMARY KEY,
                                packageID INTEGER NOT NULL,
                                name TEXT,
                                package TEXT,
                                startDate TEXT,
                                endDate TEXT,
                                exp  INTEGER NOT NULL,
                                FOREIGN KEY (packageID) REFERENCES MembershipPackage(packageID)
                    );
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS TrainingTime (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        customerID INTEGER,
                        customerName TEXT,
                        checkInTime TEXT NOT NULL,
                        checkOutTime TEXT,
                        note TEXT
                    );
                """);


                                // Bảng chi tiết từng lượt bán - dùng cho thống kê theo thời gian
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS package_sales (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        packageID INTEGER NOT NULL,
                        total_price REAL NOT NULL,
                        sale_date TEXT NOT NULL,
                        FOREIGN KEY (packageID) REFERENCES Membership_package(id)
                    );
                """);

                // (Tùy chọn) Bảng thống kê tổng theo package - có thể cập nhật định kỳ
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS PackageSalesStats (
                        packageID INTEGER PRIMARY KEY,
                        totalSales INTEGER DEFAULT 0,
                        revenue INTEGER DEFAULT 0,
                        FOREIGN KEY (packageID) REFERENCES Membership_package(id)
                    );
                """);

                // Tạo admin mặc định nếu chưa tồn tại
                stmt.execute("""
                    INSERT OR IGNORE INTO Admin (id, name, password, phone, email)
                    VALUES (999999, 'Default Admin', '999999', '0987654321', 'admin@example.com');
                """);

                System.out.println("Database and all tables created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static final String DB_PATH = "service_app.db"; // Tên file DB, có thể là đường dẫn đầy đủ

    public static void deleteDatabase() {
        File dbFile = new File(DB_PATH);
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                System.out.println("Database deleted successfully.");
            } else {
                System.out.println("Failed to delete the database.");
            }
        } else {
            System.out.println("Database file does not exist.");
        }
    }
}

