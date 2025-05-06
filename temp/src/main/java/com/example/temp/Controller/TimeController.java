package com.example.temp.Controller;

import com.example.temp.Models.TrainingTime;
import com.example.temp.DAO.TrainingTimeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TimeController {

    @FXML private TableView<TrainingTime> timeTableView;
    @FXML private TableColumn<TrainingTime, Integer> colCustomerID;
    @FXML private TableColumn<TrainingTime, String> colName, colPhone, colStartDay, colEndDay, colNote;

    @FXML private TextField fieldID, fieldName, fieldPhone, fieldNote;
    @FXML private Label labelTotal;

    @FXML
    public void initialize() {
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colStartDay.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));
        colEndDay.setCellValueFactory(new PropertyValueFactory<>("checkOutTime"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        loadTrainingTimes();
    }

    private void loadTrainingTimes() {
        List<TrainingTime> allTrainingTimes = TrainingTimeDAO.getAllTrainingTimes();
        ObservableList<TrainingTime> updatedList = FXCollections.observableArrayList(allTrainingTimes);
        timeTableView.getItems().setAll(updatedList);
        timeTableView.refresh();
        labelTotal.setText("Tổng: " + updatedList.size());
    }

    @FXML
    private void handleCheckIn() {
        String name = fieldName.getText().trim();
        String phone = fieldPhone.getText().trim();
        String note = fieldNote.getText().trim();
        int id;

        if (name.isEmpty() || phone.isEmpty()) {
            showInfo("Vui lòng nhập đủ Tên và SĐT.");
            return;
        }

        if (TrainingTimeDAO.hasUnfinishedCheckIn(phone)) {
            showInfo("Hội viên này chưa check-out.");
            return;
        }

        if (fieldID.getText().trim().isEmpty()) {
            id = TrainingTimeDAO.getNextId();
        } else {
            try {
                id = Integer.parseInt(fieldID.getText().trim());
            } catch (NumberFormatException e) {
                showInfo("ID không hợp lệ. Vui lòng nhập số.");
                return;
            }
        }

        String now = getNow();
        String finalNote = note.isEmpty() ? "Check-in lúc " + now : note;

        TrainingTime time = new TrainingTime(id, name, phone, now, null, finalNote);

        if (TrainingTimeDAO.insertCheckIn(time)) {
            showInfo("Check-in thành công.");
            clearForm();
            loadTrainingTimes();
            timeTableView.scrollTo(0);
        } else {
            showInfo("Check-in thất bại.");
        }
    }

    @FXML
    private void handleCheckOut() {
        String phone = fieldPhone.getText().trim();

        if (phone.isEmpty()) {
            showInfo("Vui lòng nhập SĐT.");
            return;
        }

        String now = getNow();
        String note = "Check-out lúc " + now;

        boolean updated = TrainingTimeDAO.insertCheckOut(phone, now);
        boolean noteSaved = TrainingTimeDAO.updateNoteByPhone(phone, note);

        if (!updated) {
            showInfo("❌ Không tìm thấy bản ghi cần check-out (có thể đã check-out).");
            return;
        }

        if (!noteSaved) {
            showInfo("❌ Không cập nhật được ghi chú (có thể bản ghi đã bị thay đổi).");
            return;
        }

        showInfo("✅ Check-out thành công.\n" + note);
        clearForm();
        loadTrainingTimes();
        timeTableView.scrollTo(0);
    }

    private void clearForm() {
        fieldID.clear();
        fieldName.clear();
        fieldPhone.clear();
        fieldNote.clear();
    }

    private String getNow() {
        return java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
