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
                    );
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
                        FOREIGN KEY (packageID) REFERENCES MembershipPackage(id)
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
                      (100001, 'Võ Hà', '0828769590', 'Nam', 60),
                        (100002, 'Hồ Xuân', '0794399073', 'Nam', 32),
                        (100003, 'Hồ Khánh', '0422413970', 'Nữ', 32),
                        (100004, 'Nguyễn Ngân', '0907709661', 'Nam', 56),
                        (100005, 'Nguyễn Khánh', '0487763537', 'Nam', 54),
                        (100006, 'Phạm Khánh', '0564731586', 'Nam', 59),
                        (100007, 'Võ Lan', '0377339205', 'Nam', 19),
                        (100008, 'Trần Quang', '0438433749', 'Nam', 54),
                        (100009, 'Phan Oanh', '0418946496', 'Nữ', 25),
                        (100010, 'Võ Bình', '0694044498', 'Nữ', 51),
                        (100011, 'Huỳnh Ngân', '0321046857', 'Nữ', 55),
                        (100012, 'Hoàng Quang', '018584331', 'Nữ', 51),
                        (100013, 'Phan Quang', '0919054881', 'Nam', 38),
                        (100014, 'Lê Lan', '0846419768', 'Nữ', 19),
                        (100015, 'Phạm Ngân', '0616470051', 'Nữ', 52),
                        (100016, 'Phan An', '0842036634', 'Nam', 53),
                        (100017, 'Hồ Hà', '0467600658', 'Nữ', 38),
                        (100018, 'Hồ Quang', '0856056496', 'Nữ', 58),
                        (100019, 'Phan An', '0684624929', 'Nam', 25),
                        (100020, 'Nguyễn Oanh', '0429642660', 'Nữ', 37)
                """);

                // Thêm checkIn/out
                stmt.execute("""
                    INSERT INTO TrainingTime  (customerID, customerName, checkInTime, checkOutTime, note) VALUES
                    ( 100019, 'Phan An', '2025-02-26 06:00:00', '2025-02-26 07:00:00', 'chuyên nghiệp siêu tốc linh hoạt' ),
                    ( 100016, 'Phan An', '2025-02-11 17:00:00', '2025-02-11 18:00:00', 'thoải mái chuyên nghiệp siêu tốc' ),
                    ( 100002, 'Hồ Xuân', '2025-05-09 13:30:00', '2025-05-09 14:30:00', 'thoải mái độc quyền hiệu quả' ),
                    ( 100012, 'Hoàng Quang', '2025-05-15 10:00:00', '2025-05-15 11:00:00', 'tiết kiệm siêu tốc thoải mái' ),
                    ( 100019, 'Phan An', '2025-02-11 11:30:00', '2025-02-11 12:30:00', 'thoải mái siêu tốc hiệu quả' ),
                    ( 100002, 'Hồ Xuân', '2025-01-08 09:00:00', '2025-01-08 10:00:00', 'độc quyền cao cấp chuyên nghiệp' ),
                    ( 100004, 'Nguyễn Ngân', '2025-01-04 07:00:00', '2025-01-04 08:00:00', 'hiệu quả linh hoạt cao cấp' ),
                    ( 100002, 'Hồ Xuân', '2025-05-13 19:30:00', '2025-05-13 20:30:00', 'cao cấp siêu tốc độc quyền' ),
                    ( 100006, 'Phạm Khánh', '2025-02-27 12:30:00', '2025-02-27 13:30:00', 'hiệu quả tiết kiệm siêu tốc' ),
                    ( 100019, 'Phan An', '2025-03-15 08:00:00', '2025-03-15 09:00:00', 'chuyên nghiệp cao cấp thoải mái' ),
                    ( 100009, 'Phan Oanh', '2025-03-11 14:00:00', '2025-03-11 15:00:00', 'hiệu quả chuyên nghiệp độc quyền' ),
                    ( 100009, 'Phan Oanh', '2025-05-13 16:30:00', '2025-05-13 17:30:00', 'độc quyền siêu tốc cao cấp' ),
                    ( 100015, 'Phạm Ngân', '2025-02-19 10:30:00', '2025-02-19 11:30:00', 'tiết kiệm siêu tốc hiệu quả' ),
                    ( 100001, 'Võ Hà', '2025-02-27 12:30:00', '2025-02-27 13:30:00', 'chuyên nghiệp linh hoạt hiệu quả' ),
                    ( 100005, 'Nguyễn Khánh', '2025-01-01 18:30:00', '2025-01-01 19:30:00', 'thoải mái hiệu quả siêu tốc' ),
                    ( 100002, 'Hồ Xuân', '2025-04-10 08:00:00', '2025-04-10 09:00:00', 'hiệu quả tiết kiệm cao cấp' ),
                    ( 100018, 'Hồ Quang', '2025-04-12 14:00:00', '2025-04-12 15:00:00', 'độc quyền linh hoạt cao cấp' ),
                    ( 100008, 'Trần Quang', '2025-03-23 13:00:00', '2025-03-23 14:00:00', 'độc quyền thoải mái linh hoạt' ),
                    ( 100011, 'Huỳnh Ngân', '2025-02-12 16:00:00', '2025-02-12 17:00:00', 'cao cấp độc quyền tiết kiệm' ),
                    ( 100005, 'Nguyễn Khánh', '2025-03-04 10:30:00', '2025-03-04 11:30:00', 'độc quyền linh hoạt siêu tốc' )
                """);

                // Thêm thẻ hv
                stmt.execute("""
                INSERT INTO MemberCard (customerID, packageID, name, package, startDate, endDate, exp) VALUES
                (100001, 500000, 'Võ Hà', 'VIP year plus', '2025-05-05', '2027-04-25', 24),
                (100002, 300000, 'Hồ Xuân', 'VIP3', '2025-04-19', '2025-10-16', 6),
                (100003, 300000, 'Hồ Khánh', 'VIP3', '2025-01-16', '2025-07-15', 6),
                (100004, 500000, 'Nguyễn Ngân', 'VIP year plus', '2025-01-16', '2027-01-06', 24),
                (100005, 500000, 'Nguyễn Khánh', 'VIP year plus', '2025-05-21', '2027-05-11', 24),
                (100006, 200000, 'Phạm Khánh', 'VIP2', '2025-05-13', '2025-08-11', 3),
                (100007, 400000, 'Võ Lan', 'VIP year', '2025-04-24', '2026-04-19', 12),
                (100008, 400000, 'Trần Quang', 'VIP year', '2025-02-01', '2026-01-27', 12),
                (100009, 500000, 'Phan Oanh', 'VIP year plus', '2025-03-07', '2027-02-25', 24),
                (100010, 100000, 'Võ Bình', 'VIP1', '2025-04-07', '2025-05-07', 1),
                (100011, 300000, 'Huỳnh Ngân', 'VIP3', '2025-02-08', '2025-08-07', 6),
                (100012, 400000, 'Hoàng Quang', 'VIP year', '2025-04-05', '2026-03-31', 12),
                (100013, 300000, 'Phan Quang', 'VIP3', '2025-04-08', '2025-10-05', 6),
                (100014, 100000, 'Lê Lan', 'VIP1', '2025-03-22', '2025-04-21', 1),
                (100015, 300000, 'Phạm Ngân', 'VIP3', '2025-02-21', '2025-08-20', 6),
                (100016, 300000, 'Phan An', 'VIP3', '2025-01-10', '2025-07-09', 6),
                (100017, 100000, 'Hồ Hà', 'VIP1', '2025-02-06', '2025-03-08', 1),
                (100018, 400000, 'Hồ Quang', 'VIP year', '2025-04-16', '2026-04-11', 12),
                (100019, 400000, 'Phan An', 'VIP year', '2025-01-19', '2026-01-14', 12),
                (100020, 500000, 'Nguyễn Oanh', 'VIP year plus', '2025-04-01', '2027-03-22', 24)
            """);
                // Thêm thống kê
                stmt.execute("""
                        INSERT INTO Package_Sales (customerID, packageID, total_price, sale_date, type) VALUES
                        (100001, 500000, 2500000, '2025-05-05', 'new'),
                        (100002, 300000, 1000000, '2025-04-19', 'new'),
                        (100003, 300000, 1000000, '2025-01-16', 'new'),
                        (100004, 500000, 2500000, '2025-01-16', 'new'),
                        (100005, 500000, 2500000, '2025-05-21', 'new'),
                        (100006, 200000,  500000, '2025-05-13', 'new'),
                        (100007, 400000, 2000000, '2025-04-24', 'new'),
                        (100008, 400000, 2000000, '2025-02-01', 'new'),
                        (100009, 500000, 2500000, '2025-03-07', 'new'),
                        (100010, 100000,  200000, '2025-04-07', 'new'),
                        (100011, 300000, 1000000, '2025-02-08', 'new'),
                        (100012, 400000, 2000000, '2025-04-05', 'new'),
                        (100013, 300000, 1000000, '2025-04-08', 'new'),
                        (100014, 100000,  200000, '2025-03-22', 'new'),
                        (100015, 300000, 1000000, '2025-02-21', 'new'),
                        (100016, 300000, 1000000, '2025-01-10', 'new'),
                        (100017, 100000,  200000, '2025-02-06', 'new'),
                        (100018, 400000, 2000000, '2025-04-16', 'new'),
                        (100019, 400000, 2000000, '2025-01-19', 'new'),
                        (100020, 500000, 2500000, '2025-04-01', 'new'),
                        (100001, 500000, 2500000, '2027-04-25', 'renewal'),
                        (100003, 300000, 1000000, '2025-07-15', 'renewal'),
                        (100006, 200000,  500000, '2025-08-11', 'renewal'),
                        (100010, 100000,  200000, '2025-05-07', 'renewal'),
                        (100014, 100000,  200000, '2025-04-21', 'renewal');
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

