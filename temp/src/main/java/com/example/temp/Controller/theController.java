package com.example.temp.Controller;

import com.example.temp.Models.MemberCard;
import com.example.temp.DAO.MemberCardDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class theController {

    @FXML private TextField customerIDField;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private ComboBox<String> goiComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField priceField;

    @FXML private TableView<MemberCard> cardTableView;
    @FXML private TableColumn<MemberCard, String> colCustomerID;
    @FXML private TableColumn<MemberCard, String> colName;
    @FXML private TableColumn<MemberCard, String> colPhone;
    @FXML private TableColumn<MemberCard, String> colGender;
    @FXML private TableColumn<MemberCard, String> colStartDate;
    @FXML private TableColumn<MemberCard, String> colEndDate;
    @FXML private TableColumn<MemberCard, String> colGoi;
    @FXML private TableColumn<MemberCard, String> colPrice;

    private ObservableList<MemberCard> cardList;

    @FXML
    public void initialize() {
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colGoi.setCellValueFactory(new PropertyValueFactory<>("goi"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        genderComboBox.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        goiComboBox.setItems(FXCollections.observableArrayList("1 tháng", "4 tháng", "6 tháng", "12 tháng"));

        startDatePicker.setValue(LocalDate.now());
        goiComboBox.setOnAction(event -> updatePriceAndEndDate());

        try {
            cardList = FXCollections.observableArrayList(MemberCardDAO.getAllMemberCards());
            cardTableView.setItems(cardList);
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải danh sách thẻ: " + e.getMessage());
        }
    }

    private void updatePriceAndEndDate() {
        String selected = goiComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue() != null ? startDatePicker.getValue() : LocalDate.now();
        switch (selected) {
            case "1 tháng" -> {
                priceField.setText("200000");
                endDatePicker.setValue(startDate.plusMonths(1));
            }
            case "4 tháng" -> {
                priceField.setText("700000");
                endDatePicker.setValue(startDate.plusMonths(4));
            }
            case "6 tháng" -> {
                priceField.setText("1000000");
                endDatePicker.setValue(startDate.plusMonths(6));
            }
            case "12 tháng" -> {
                priceField.setText("1600000");
                endDatePicker.setValue(startDate.plusMonths(12));
            }
        }
    }

    @FXML
    private void handlePayment() {
        String customerID = customerIDField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String gender = genderComboBox.getValue();
        String goi = goiComboBox.getValue();
        String price = priceField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (customerID.isEmpty() || name.isEmpty() || phone.isEmpty() || gender == null || goi == null || price.isEmpty() || startDate == null || endDate == null) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        // Validate phone number (at least 10 digits)
        if (phone.length() < 10) {
            showAlert("Lỗi", "Số điện thoại phải có ít nhất 10 chữ số.");
            return;
        }

        // Check if customerID already exists
        if (MemberCardDAO.isCustomerIDExists(customerID)) {
            showAlert("Lỗi", "Mã khách hàng " + customerID + " đã tồn tại. Vui lòng chọn mã khác.");
            return;
        }

        MemberCard card = new MemberCard(customerID, name, phone, gender,
                startDate.toString(), endDate.toString(), goi, price);

        if (MemberCardDAO.insertMemberCard(card)) {
            cardList.add(card);
            showAlert("Thành công", "Đăng ký thẻ thành công.");
            // Clear the form after successful registration
            customerIDField.clear();
            nameField.clear();
            phoneField.clear();
            genderComboBox.setValue(null);
            goiComboBox.setValue(null);
            startDatePicker.setValue(LocalDate.now());
            endDatePicker.setValue(null);
            priceField.clear();
        } else {
            showAlert("Lỗi", "Không thể đăng ký thẻ. Kiểm tra lỗi: " + MemberCardDAO.getLastError());
        }
    }

    @FXML
    private void handleRenewCard() {
        MemberCard selected = cardTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Vui lòng chọn thẻ cần gia hạn.");
            return;
        }

        String goi = selected.getGoi();
        if (goi == null || goi.isEmpty()) {
            showAlert("Lỗi", "Gói của thẻ không hợp lệ.");
            return;
        }

        LocalDate oldEndDate;
        try {
            oldEndDate = LocalDate.parse(selected.getEndDate());
        } catch (Exception e) {
            showAlert("Lỗi", "Ngày kết thúc không hợp lệ: " + e.getMessage());
            return;
        }

        LocalDate newEndDate;
        switch (goi) {
            case "1 tháng":
                newEndDate = oldEndDate.plusMonths(1);
                break;
            case "4 tháng":
                newEndDate = oldEndDate.plusMonths(4);
                break;
            case "6 tháng":
                newEndDate = oldEndDate.plusMonths(6);
                break;
            case "12 tháng":
                newEndDate = oldEndDate.plusMonths(12);
                break;
            default:
                showAlert("Lỗi", "Không xác định được gói: " + goi);
                return;
        }

        selected.setEndDate(newEndDate.toString());
        try {
            if (MemberCardDAO.updateMemberCardEndDate(selected.getCustomerID(), newEndDate.toString())) {
                cardTableView.refresh();
                showAlert("Thành công", "Gia hạn thẻ thành công. Ngày hết hạn mới: " + newEndDate);
            } else {
                showAlert("Lỗi", "Không thể gia hạn thẻ. Kiểm tra lỗi: " + MemberCardDAO.getLastError());
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Lỗi khi gia hạn thẻ: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCard() {
        MemberCard selected = cardTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Vui lòng chọn thẻ để xóa.");
            return;
        }

        try {
            if (MemberCardDAO.deleteMemberCard(selected.getCustomerID())) {
                cardList.remove(selected);
                showAlert("Thành công", "Đã xóa thẻ hội viên.");
            } else {
                showAlert("Lỗi", "Không thể xóa thẻ. Kiểm tra lỗi: " + MemberCardDAO.getLastError());
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}