package com.example.temp.Controller;

import com.example.temp.Models.TrainingTime;
import com.example.temp.DAO.TrainingTimeDAO;
import com.example.temp.DAO.MemberDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TimeController {

    @FXML private TextField fieldID; // Trường nhập mã hội viên
    @FXML private TextField fieldNote; // Trường ghi chú khi check-out
    @FXML private TableView<TrainingTime> timeTableView; // Bảng hiển thị các bản ghi thời gian
    @FXML private TableColumn<TrainingTime, Integer> colCustomerID; // Cột CustomerID
    @FXML private TableColumn<TrainingTime, String> colStartDay, colEndDay, colNote; // Các cột thời gian và ghi chú
    @FXML private Label labelTotal; // Label hiển thị tổng số bản ghi

    @FXML
    public void initialize() {
        // Khởi tạo các cột trong bảng
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colStartDay.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));
        colEndDay.setCellValueFactory(new PropertyValueFactory<>("checkOutTime"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        loadTrainingTimes(); // Tải dữ liệu bảng khi khởi tạo
    }

    // Phương thức để tải tất cả bản ghi từ TrainingTime
    private void loadTrainingTimes() {
        ObservableList<TrainingTime> updatedList = FXCollections.observableArrayList(TrainingTimeDAO.getAllTrainingTimes());
        timeTableView.getItems().setAll(updatedList); // Cập nhật lại bảng
        timeTableView.refresh();
        labelTotal.setText("Tổng: " + updatedList.size()); // Cập nhật tổng số bản ghi
    }

    // Phương thức xử lý check-in
    @FXML
    private void handleCheckIn() {
        String customerID = fieldID.getText().trim();
        String note = fieldNote.getText().trim();

        if (customerID.isEmpty()) {
            showInfo("⚠ Vui lòng nhập mã hội viên.");
            return;
        }

        // Kiểm tra mã hội viên có tồn tại trong bảng QLHV
        if (!isCustomerIDExistsInQLHV(customerID)) {
            showInfo("⚠ Mã hội viên không tồn tại.");
            return;
        }

        // Kiểm tra xem hội viên có đã check-in chưa
        if (TrainingTimeDAO.hasUnfinishedCheckIn(Integer.parseInt(customerID))) {
            showInfo("Hội viên này chưa check-out.");
            return;
        }

        String checkInTime = getNow();
        String finalNote = note.isEmpty() ? "Check-in lúc " + checkInTime : note;

        // Tạo đối tượng TrainingTime và lưu vào cơ sở dữ liệu
        TrainingTime trainingTime = new TrainingTime(Integer.parseInt(customerID), checkInTime, null, finalNote);
        if (TrainingTimeDAO.insertCheckIn(trainingTime)) {
            showInfo("✅ Check-in thành công.");
            clearForm();
            loadTrainingTimes();
        } else {
            showInfo("❌ Check-in thất bại.");
        }
    }

    // Phương thức xử lý check-out
    @FXML
    private void handleCheckOut() {
        String customerID = fieldID.getText().trim();

        if (customerID.isEmpty()) {
            showInfo("⚠ Vui lòng nhập mã hội viên.");
            return;
        }

        // Kiểm tra mã hội viên có tồn tại trong bảng QLHV
        if (!isCustomerIDExistsInQLHV(customerID)) {
            showInfo("⚠ Mã hội viên không tồn tại.");
            return;
        }

        String checkOutTime = getNow();
        String note = "Check-out lúc " + checkOutTime;

        // Cập nhật thời gian check-out
        if (TrainingTimeDAO.insertCheckOut(Integer.parseInt(customerID), checkOutTime)) {
            showInfo("✅ Check-out thành công.\n" + note);
            clearForm();
            loadTrainingTimes();
        } else {
            showInfo("❌ Không tìm thấy bản ghi để check-out.");
        }
    }

    // Kiểm tra mã hội viên trong bảng QLHV
    private boolean isCustomerIDExistsInQLHV(String customerID) {
        return MemberDAO.getAllMembers().stream()
                .anyMatch(member -> member.getCustomerID() == Integer.parseInt(customerID)); // Kiểm tra sự tồn tại của customerID
    }

    // Lấy thời gian hiện tại
    private String getNow() {
        return java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Phương thức để hiển thị thông báo cho người dùng
    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Làm sạch các trường nhập liệu sau khi check-in/check-out
    private void clearForm() {
        fieldID.clear();
        fieldNote.clear();
    }
}
