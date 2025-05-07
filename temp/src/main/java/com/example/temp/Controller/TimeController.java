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
    @FXML private TableColumn<TrainingTime, String> colMemberId;
    @FXML private TableColumn<TrainingTime, String> colStartDay, colEndDay, colNote;

    @FXML private TextField fieldMemberId, fieldNote;
    @FXML private Label labelTotal;

    @FXML
    public void initialize() {
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colStartDay.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));
        colEndDay.setCellValueFactory(new PropertyValueFactory<>("checkOutTime"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        loadTrainingTimes();
    }

    private void loadTrainingTimes() {
        List<TrainingTime> allTrainingTimes = TrainingTimeDAO.getAllTrainingTimes();
        ObservableList<TrainingTime> updatedList = FXCollections.observableArrayList(allTrainingTimes);
        timeTableView.setItems(updatedList);
        labelTotal.setText("Tổng: " + updatedList.size());
    }

    @FXML
    private void handleCheckIn() {
//        String memberId = fieldMemberId.getText().trim();
//        String note = fieldNote.getText().trim();
//
//        if (memberId.isEmpty()) {
//            showInfo("Vui lòng nhập mã hội viên.");
//            return;
//        }
//
//        if (TrainingTimeDAO.hasUnfinishedCheckIn(memberId)) {
//            showInfo("Hội viên này chưa check-out.");
//            return;
//        }
//
//        String now = getCurrentTime();
//        String finalNote = note.isEmpty() ? "Check-in lúc " + now : note;
//
//        TrainingTime trainingTime = new TrainingTime(memberId, now, null, finalNote);
//
//        if (TrainingTimeDAO.insertCheckIn(trainingTime)) {
//            showInfo("✅ Check-in thành công.");
//            clearForm();
//            loadTrainingTimes();
//            timeTableView.scrollTo(0);
//        } else {
//            showInfo("❌ Check-in thất bại.");
//        }
    }

    @FXML
    private void handleCheckOut() {
//        String memberId = fieldMemberId.getText().trim();
//
//        if (memberId.isEmpty()) {
//            showInfo("Vui lòng nhập mã hội viên.");
//            return;
//        }
//
//        String now = getCurrentTime();
//        String note = "Check-out lúc " + now;
//
//        boolean updated = TrainingTimeDAO.insertCheckOut(memberId, now);
//        boolean noteUpdated = TrainingTimeDAO.updateNoteByMemberId(memberId, note);
//
//        if (!updated) {
//            showInfo("❌ Không tìm thấy bản ghi cần check-out (có thể đã check-out).");
//            return;
//        }
//
//        if (!noteUpdated) {
//            showInfo("❌ Không cập nhật được ghi chú.");
//            return;
//        }
//
//        showInfo("✅ Check-out thành công.\n" + note);
//        clearForm();
//        loadTrainingTimes();
//        timeTableView.scrollTo(0);
    }

    private void clearForm() {
        fieldMemberId.clear();
        fieldNote.clear();
    }

    private String getCurrentTime() {
        return java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
