package com.example.temp.Controller;

import com.example.temp.DAO.MemberDAO;
import com.example.temp.DAO.MemberCardDAO;
import com.example.temp.DAO.MembershipPackageDAO;
import com.example.temp.DAO.PackageSalesStatsDAO;
import com.example.temp.Models.MembershipCard;
import com.example.temp.Models.MembershipPackage;
import com.example.temp.Models.PackageSalesStats;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class MembershipCardController {

    @FXML private TextField customerIDField;
    @FXML private ComboBox<MembershipPackage> goiComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<MembershipCard> cardTableView;
    @FXML private TableColumn<MembershipCard, Integer> colCustomerID;
    @FXML private TableColumn<MembershipCard, String> colName;
    @FXML private TableColumn<MembershipCard, String> colStartDate;
    @FXML private TableColumn<MembershipCard, String> colEndDate;
    @FXML private TableColumn<MembershipCard, String> colPackage;
    @FXML private TextField packageIDField;
    @FXML private TextField expField;
    @FXML private TextField inputSearch;

    private ObservableList<MembershipCard> cardList;

    @FXML
    public void initialize() {
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName")); // sửa lại
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colPackage.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPackageName()));
        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchByCustomerID());

        startDatePicker.setValue(LocalDate.now());
        goiComboBox.setOnAction(event -> {
            updateEndDate();
            displayPackageInfo(); // Cập nhật thông tin gói
        });


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

        // Dùng đúng constructor
        MembershipCard card = new MembershipCard(
                id,
                name,
                selectedPackage.getPackageID(),
                selectedPackage.getPackageName(),
                startDate.toString(),
                endDate.toString(),
                selectedPackage.getExp()
        );

        if (MemberCardDAO.insertMemberCard(card)) {
            cardList.add(card);
            showAlert("Thành công", "Đăng ký thẻ thành công.");

            PackageSalesStatsDAO salesDAO = new PackageSalesStatsDAO();
            PackageSalesStats sale = new PackageSalesStats();
            sale.setCustomerId(id);
            sale.setPackageId(selectedPackage.getPackageID());
            sale.setTotalPrice(selectedPackage.getPrice());
            sale.setSaleDate(LocalDate.now());
            sale.setType("new");
            salesDAO.insertPackageSale(sale);
//            salesDAO.updateOrInsertStats(selectedPackage.getPackageID(), selectedPackage.getPrice());

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
        MembershipCard selected = cardTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Vui lòng chọn thẻ cần gia hạn.");
            return;
        }

        int packageID = selected.getPackageID();
        MembershipPackageDAO dao = new MembershipPackageDAO();
        List<MembershipPackage> packages = dao.getActivePackages();
        MembershipPackage matchedPackage = packages.stream()
                .filter(p -> p.getPackageID() == packageID)
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
                PackageSalesStatsDAO packageSalesDAO = new PackageSalesStatsDAO();
                PackageSalesStats sale = new PackageSalesStats();
                sale.setCustomerId(selected.getCustomerID());
                sale.setPackageId(packageID);
                sale.setTotalPrice(matchedPackage.getPrice());
                sale.setSaleDate(LocalDate.now());
                sale.setType("renewal");

                packageSalesDAO.insertPackageSale(sale);
//                packageSalesDAO.updateOrInsertStats(packageID, matchedPackage.getPrice());

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
        MembershipCard selected = cardTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "⚠ Vui lòng chọn thẻ hội viên để xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa thẻ");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa thẻ của hội viên \"" + selected.getCustomerName() + "\" (ID: " + selected.getCustomerID() + ")?");
        alert.setContentText("Thao tác này không thể hoàn tác.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (MemberCardDAO.deleteMemberCard(selected.getCustomerID())) {
                    cardList.remove(selected); // Cập nhật lại danh sách
                    showAlert("Thành công", "🗑 Đã xóa thẻ hội viên.");
                } else {
                    showAlert("Lỗi", "Không thể xóa thẻ. Kiểm tra lỗi: " + MemberCardDAO.getLastError());
                }
            } catch (Exception e) {
                showAlert("Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
            }
        }
    }



    private void searchByCustomerID() {
        String keyword = inputSearch.getText().trim();
        if (keyword.isEmpty()) {
            cardList.setAll(MemberCardDAO.getAllMemberCards());
        } else {
            cardList.setAll(MemberCardDAO.searchByCustomerID(keyword));
        }
        cardTableView.setItems(cardList);
    }


    private void displayPackageInfo() {
        MembershipPackage selected = goiComboBox.getValue();
        if (selected == null) return;
        packageIDField.setText(String.valueOf(selected.getPackageID()));
        expField.setText(String.valueOf(selected.getExp()));
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
