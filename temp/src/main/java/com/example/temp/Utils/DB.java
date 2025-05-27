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
                    CREATE TABLE IF NOT EXISTS MembershipPackage (
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
                        FOREIGN KEY (packageID) REFERENCES MembershipPackage(id)
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


                // Ghi nhận từng lượt bán gói (đăng ký hoặc gia hạn)
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Package_Sales (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        customerID INTEGER NOT NULL,
                        packageID INTEGER NOT NULL,
                        total_price REAL NOT NULL,
                        sale_date TEXT NOT NULL,
                        type TEXT NOT NULL CHECK (type IN ('new', 'renewal')),
                        FOREIGN KEY (customerID) REFERENCES MemberDetail(customerID),
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

    public static void addDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
// Thêm nhân viên
                stmt.execute("""
                    INSERT INTO Employee (id, name, password, phone, isReceptionist) VALUES
                    (200001, 'Nguyễn Thị Lan', '200001@123', '0912345678', 1),
                    (200002, 'Trần Văn Minh', '200002@123', '0987654321', 1),
                    (200003, 'Lê Hoàng', NULL, '0901122334', 0),
                    (200004, 'Phạm Thu Hà', NULL, '0933445566', 0),
                    (200005, 'Đỗ Văn Tùng', NULL, '0977889900', 0),
                    (200006, 'Ngô Thị Mai', NULL, '0966554433', 0),
                    (200007, 'Bùi Quang Huy', NULL, '0944221133', 0),
                    (200008, 'Vũ Hồng Nhung', NULL, '0922334455', 0)
                """);
                // Thêm gói hội viên
                stmt.execute("""
                    INSERT INTO MembershipPackage (id, name, price, description, exp, status) VALUES
                    (100000, 'VIP1', 200000, 'Gói cơ bản 1 tháng, dành cho người mới bắt đầu', 1,  1),
                    (200000, 'VIP2', 500000, 'Gói tập 3 tháng, tiết kiệm và linh hoạt', 3,  1),
                    (300000, 'VIP3', 1000000, 'Gói tập 6 tháng, kèm ưu đãi huấn luyện viên', 6,  1),
                    (400000, 'VIP year', 2000000, 'Gói tập 12 tháng, được tặng 2 buổi PT', 12, 1),
                    (500000, 'VIP year plus', 2500000, 'Gói 12 tháng,Trọn gói cao cấp tập luyện, xông hơi, tủ locker riêng, ưu tiên mọi dịch vụ.', 12,  1)
                """);

                stmt.execute("""
                      INSERT INTO MemberDetail (customerID, name, phone, gender, age) VALUES
                      (100001, 'Võ Hà', '828769590', 'Nam', 60),
                      (100002, 'Hồ Xuân', '794399073', 'Nam', 32),
                      (100003, 'Hồ Khánh', '422413970', 'Nữ', 32),
                      (100004, 'Nguyễn Ngân', '907709661', 'Nam', 56),
                      (100005, 'Nguyễn Khánh', '487763537', 'Nam', 54),
                      (100006, 'Phạm Khánh', '564731586', 'Nam', 59),
                      (100007, 'Võ Lan', '377339205', 'Nam', 19),
                      (100008, 'Trần Quang', '438433749', 'Nam', 54),
                      (100009, 'Phan Oanh', '418946496', 'Nữ', 25),
                      (100010, 'Võ Bình', '694044498', 'Nữ', 51),
                      (100011, 'Huỳnh Ngân', '321046857', 'Nữ', 55),
                      (100012, 'Hoàng Quang', '18584331', 'Nữ', 51),
                      (100013, 'Phan Quang', '919054881', 'Nam', 38),
                      (100014, 'Lê Lan', '846419768', 'Nữ', 19),
                      (100015, 'Phạm Ngân', '616470051', 'Nữ', 52),
                      (100016, 'Phan An', '842036634', 'Nam', 53),
                      (100017, 'Hồ Hà', '467600658', 'Nữ', 38),
                      (100018, 'Hồ Quang', '856056496', 'Nữ', 58),
                      (100019, 'Phan An', '684624929', 'Nam', 25),
                      (100020, 'Nguyễn Oanh', '429642660', 'Nữ', 37)
                """);

            //  thêm thiết bị
                stmt.execute("""
                    INSERT INTO Equipment (id, name, description, repairNote, repairDate, status) VALUES
                    (100001, 'Treadmill', 'Máy chạy bộ điện tử với nhiều chế độ.', NULL, NULL, 1),
                    (100002, 'Dumbbell Set', 'Bộ tạ tay từ 2kg đến 30kg.', NULL, NULL, 1),
                    (100003, 'Bench Press', 'Ghế đẩy tạ với khung an toàn.', NULL, NULL, 1),
                    (100004, 'Elliptical Trainer', 'Máy tập toàn thân, giảm chấn lên khớp gối.', NULL, NULL, 1),
                    (100005, 'Stationary Bike', 'Xe đạp tập với cảm biến nhịp tim.', NULL, NULL, 1),
                    (100006, 'Cable Crossover', 'Thiết bị kéo cáp đa năng cho phần thân trên.', NULL, NULL, 1),
                    (100007, 'Rowing Machine', 'Máy chèo thuyền cho bài tập toàn thân.', NULL, NULL, 1),
                    (100008, 'Leg Press Machine', 'Máy ép chân hỗ trợ phát triển cơ đùi.', NULL, NULL, 1),
                    (100009, 'Smith Machine', 'Khung tập tạ có rãnh trượt an toàn.', NULL, NULL, 1),
                    (100010, 'Pull-up Bar', 'Thanh xà đơn cố định trên tường.', NULL, NULL, 1),
                    (100011, 'Lat Pulldown Machine', 'Máy kéo xô hỗ trợ phát triển cơ lưng.', NULL, NULL, 1),
                    (100012, 'Chest Press Machine', 'Máy đẩy ngực giúp phát triển cơ ngực và tay.', NULL, NULL, 1),
                    (100013, 'Bosu Ball', 'Bóng Bosu dùng để tập thăng bằng và core.', NULL, NULL, 1),
                    (100014, 'Medicine Ball', 'Bóng tạ cao su dùng để tập lực và phản xạ.', NULL, NULL, 1),
                    (100015, 'TRX Suspension Trainer', 'Dây treo luyện tập sức mạnh và sự linh hoạt.', NULL, NULL, 1),
                    (100016, 'Hip Abduction Machine', 'Máy tập cơ hông, cải thiện vùng đùi ngoài.', NULL, NULL, 1),
                    (100017, 'Leg Curl Machine', 'Máy gập chân hỗ trợ phát triển cơ bắp chân sau.', NULL, NULL, 1),
                    (100018, 'Battle Ropes', 'Dây thừng tập cardio và sức mạnh tay vai.', NULL, NULL, 1),
                    (100019, 'Power Rack', 'Giá đỡ tập squat, deadlift, bench press đa năng.', NULL, NULL, 1),
                    (100020, 'Kettlebell Set', 'Bộ tạ ấm từ 4kg đến 24kg cho bài tập toàn thân.', NULL, NULL, 1)
                """);

                System.out.println("Add successfully.");
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

