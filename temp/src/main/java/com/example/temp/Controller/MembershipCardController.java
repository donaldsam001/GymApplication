package com.example.temp.Controller;

import com.example.temp.DAO.MemberDAO;
import com.example.temp.DAO.MemberCardDAO;
import com.example.temp.DAO.MembershipPackageDAO;
import com.example.temp.Models.MemberCard;
import com.example.temp.Models.MembershipPackage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class MembershipCardController {

    @FXML private TextField customerIDField;
    @FXML private ComboBox<MembershipPackage> goiComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<MemberCard> cardTableView;
    @FXML private TableColumn<MemberCard, Integer> colCustomerID;
    @FXML private TableColumn<MemberCard, String> colName;
    @FXML private TableColumn<MemberCard, String> colStartDate;
    @FXML private TableColumn<MemberCard, String> colEndDate;
    @FXML private TableColumn<MemberCard, String> colPackage;

    private ObservableList<MemberCard> cardList;

    @FXML
    public void initialize() {
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colPackage.setCellValueFactory(new PropertyValueFactory<>("package"));

        startDatePicker.setValue(LocalDate.now());
        goiComboBox.setOnAction(event -> updateEndDate());

        try {
            List<MembershipPackage> packages = new MembershipPackageDAO().getActivePackages();
            goiComboBox.setItems(FXCollections.observableArrayList(packages));
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải danh sách gói hội viên: " + e.getMessage());
        }

        try {
            cardList = FXCollections.observableArrayList(MemberCardDAO.getAllMemberCards());
            cardTableView.setItems(cardList);
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải danh sách thẻ: " + e.getMessage());
        }
    }

    private void updateEndDate() {
        MembershipPackage selected = goiComboBox.getValue();
        if (selected == null) return;
        LocalDate startDate = startDatePicker.getValue() != null ? startDatePicker.getValue() : LocalDate.now();
        endDatePicker.setValue(startDate.plusMonths(selected.getExp()));
    }

    @FXML
    private void handlePayment() {
        int id = Integer.parseInt(customerIDField.getText());
        MembershipPackage selectedPackage = goiComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (id < 100000 || id > 999999 || selectedPackage == null || startDate == null || endDate == null) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (!MemberDAO.isCustomerIDExists(id)) {
            showAlert("Lỗi", "Mã hội viên không hợp lệ.");
            return;
        }

        if (MemberCardDAO.isCustomerIDExists(id)) {
            showAlert("Lỗi", "Mã khách hàng " + id + " đã tồn tại. Vui lòng chọn mã khác.");
            return;
        }

        String name = MemberDAO.getCustomerNameById(id);

        MemberCard card = new MemberCard(
                id,
                selectedPackage.getId(),
                startDate.toString(),
                endDate.toString(),
                selectedPackage.getName(),
                name,
                selectedPackage.getExp()
        );

        if (MemberCardDAO.insertMemberCard(card)) {
            cardList.add(card);
            showAlert("Thành công", "Đăng ký thẻ thành công.");
            customerIDField.clear();
            goiComboBox.setValue(null);
            startDatePicker.setValue(LocalDate.now());
            endDatePicker.setValue(null);
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

        int packageID = selected.getPackageID();
        MembershipPackageDAO dao = new MembershipPackageDAO();
        List<MembershipPackage> packages = dao.getActivePackages();
        MembershipPackage matchedPackage = packages.stream()
                .filter(p -> p.getId() == packageID)
                .findFirst()
                .orElse(null);

        if (matchedPackage == null) {
            showAlert("Lỗi", "Không tìm thấy gói hội viên tương ứng để gia hạn.");
            return;
        }

        LocalDate oldEndDate;
        try {
            oldEndDate = LocalDate.parse(selected.getEndDate());
        } catch (Exception e) {
            showAlert("Lỗi", "Ngày kết thúc không hợp lệ: " + e.getMessage());
            return;
        }

        LocalDate newEndDate = oldEndDate.plusMonths(matchedPackage.getExp());
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
