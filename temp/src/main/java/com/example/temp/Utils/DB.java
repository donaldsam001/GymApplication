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

                // Employee table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Employee (
                        id TEXT PRIMARY KEY,
                        name TEXT NOT NULL,
                        password TEXT NOT NULL,
                        phone TEXT NOT NULL
                    );
                """);

                // Membership package table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Membership_package (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                name TEXT NOT NULL,
                                price REAL NOT NULL,
                                description TEXT,
                                exp INTEGER NOT NULL,
                                status INTEGER NOT NULL CHECK (status IN (0, 1))
                    );
                """);

                // Membership table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Membership (
                                customerID TEXT PRIMARY KEY,
                                name TEXT NOT NULL,
                                phone TEXT,
                                gender TEXT,
                                schedule TEXT,
                                startDate TEXT,
                                endDate TEXT,
                                age INTEGER
                            );
                """);

                // Equipment table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Equipment (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        description TEXT,
                        repairNote TEXT, 
                        repairDate TEXT,
                        status INTEGER NOT NULL CHECK (status IN (0, 1))
                    )
                """);

                // Membership card table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Membership_Card (
                                customerID TEXT PRIMARY KEY,
                                name TEXT NOT NULL,
                                phone TEXT,
                                gender TEXT,
                                schedule TEXT,
                                startDate TEXT,
                                endDate TEXT,
                                status INTEGER NOT NULL CHECK (status IN (0, 1))
                            );
                """);

                //History table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS History (
                        date_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        date TEXT NOT NULL,
                        time_in TEXT NOT NULL,
                        time_out TEXT NOT NULL,
                        note TEXT
                    );
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

