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

public class TrainingTimeController {

    @FXML private TextField inputSearch;
    @FXML private TextField fieldID; // Trường nhập mã hội viên
    @FXML private TextField fieldNote; // Trường ghi chú khi check-out
    @FXML private TableView<TrainingTime> timeTableView; // Bảng hiển thị các bản ghi thời gian
    @FXML private TableColumn<TrainingTime, Integer> colCustomerID; // Cột CustomerID
    @FXML private TableColumn<TrainingTime, String> colName, colStartDay, colEndDay, colNote; // Các cột thời gian và ghi chú
    @FXML private Label labelTotal; // Label hiển thị tổng số bản ghi

    @FXML
    public void initialize() {
        // Khởi tạo các cột trong bảng
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colStartDay.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));
        colEndDay.setCellValueFactory(new PropertyValueFactory<>("checkOutTime"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        loadTrainingTimes(); // Tải dữ liệu bảng khi khởi tạo
        inputSearch.setOnKeyReleased(e -> handleSearch());

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
        int id = Integer.parseInt(fieldID.getText());
        String note = fieldNote.getText().trim();

        if (id < 100000 || id > 999999) {
            showInfo("⚠ Vui lòng nhập mã hội viên.");
            return;
        }

        // Kiểm tra mã hội viên có tồn tại trong bảng QLHV
        if (!isCustomerIDExistsInQLHV(id)) {
            showInfo("⚠ Mã hội viên không tồn tại.");
            return;
        }

        // Kiểm tra xem hội viên có đã check-in chưa
        if (TrainingTimeDAO.hasUnfinishedCheckIn(id)) {
            showInfo("Hội viên này chưa check-out.");
            return;
        }

        String checkInTime = getNow();
        String finalNote = note.isEmpty() ? "Check-in lúc " + checkInTime : note;

        String name = MemberDAO.getCustomerNameById(id);

        // Tạo đối tượng TrainingTime và lưu vào cơ sở dữ liệu
        TrainingTime trainingTime = new TrainingTime(id,name, checkInTime, null, finalNote);
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
        int customerID = Integer.parseInt(fieldID.getText());

        if (customerID < 100000 || customerID > 999999) {
            showInfo("⚠ Vui lòng nhập mã hội viên.");
            return;
        }

        // Kiểm tra mã hội viên có tồn tại trong bảng QLHV
        if (!isCustomerIDExistsInQLHV(customerID)) {
            showInfo("⚠ Mã hội viên không tồn tại.");
            return;
        }

        String checkOutTime = getNow();
        String additionalNote = fieldNote.getText().trim();
        String finalNote = "";
        if (!additionalNote.isEmpty()) {
            finalNote += additionalNote;
        }


        // Cập nhật thời gian check-out
        if (TrainingTimeDAO.insertCheckOut(customerID, checkOutTime, finalNote)) {
            showInfo("✅ Check-out thành công.\n" );
            clearForm();
            loadTrainingTimes();
        } else {
            showInfo("❌ Không tìm thấy bản ghi để check-out.");
        }
    }

    private boolean isCustomerIDExistsInQLHV(int customerID) {
        return MemberDAO.isCustomerIDExists(customerID);
    }

    @FXML
    private void handleSearch() {
        String keyword = inputSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadTrainingTimes();
            return;
        }

        ObservableList<TrainingTime> filteredList = FXCollections.observableArrayList(
                TrainingTimeDAO.searchTrainingTimes(keyword)
        );
        timeTableView.getItems().setAll(filteredList);
        timeTableView.refresh();
        labelTotal.setText("Tổng: " + filteredList.size());
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
