package com.example.temp.Controller;

import com.example.temp.DAO.MemberDAO;
import com.example.temp.Models.Membership;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;

public class ManagementMembershipController {
    @FXML private TextField tfCustomerID;
    @FXML private TextField tfName;
    @FXML private TextField tfPhone;
    @FXML private ComboBox<String> cbGender;
    @FXML private TextField tfAge;
    @FXML private TextField inputSearch;

    @FXML private TableColumn<Membership, Integer> colCustomerID;
    @FXML private TableColumn<Membership, String> colName;
    @FXML private TableColumn<Membership, String> colPhone;
    @FXML private TableColumn<Membership, String> colGender;
    @FXML private TableColumn<Membership, Integer> colAge;


    @FXML private TableView<Membership> tableView;
    private ObservableList<Membership> memberList;

    @FXML private TableColumn<Membership, String> colPackage;
    @FXML private TableColumn<Membership, String> colStartDate;
    @FXML private TableColumn<Membership, String> colEndDate;



    @FXML
    public void initialize() {
        colCustomerID.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colPhone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        colGender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        colAge.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
        cbGender.getItems().addAll("Nam", "Nữ");
        colPackage.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPackageName()));
        colStartDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStartDate()));
        colEndDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEndDate()));
        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchCustomer());


        loadMembers();
        tableView.setOnMouseClicked(this::handleTableClick);
    }

    @FXML
    private void handleAdd() {
        Membership member = getFormData();
        if (member == null) return;
        int id = member.getCustomerID();
        // Kiểm tra mã hội viên đã tồn tại trong cơ sở dữ liệu
        if (MemberDAO.isCustomerIDExists(id)) {
            showAlert("⚠ Mã hội viên này đã tồn tại.");
            return ;
        }
        // Thêm vào CSDL
        if (MemberDAO.addMember(member)) {
            showAlert("✅ Thêm hội viên thành công!");
            loadMembers();
            clearForm();
        } else {
            showAlert("❌ Thêm hội viên thất bại.");
        }
    }


    @FXML
    private void handleUpdate() {
        Membership selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("⚠ Vui lòng chọn hội viên để cập nhật!");
            return;
        }

        Membership member = getFormData();
        if (member == null) return;

        MemberDAO.updateMember(member);
        showAlert("✅ Cập nhật hội viên thành công!");
        loadMembers();
        clearForm();
    }

    @FXML
    private void handleDelete() {
        Membership selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("⚠ Vui lòng chọn hội viên để xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa hội viên \"" + selected.getName() + "\" (ID: " + selected.getCustomerID() + ")?");
        alert.setContentText("Thao tác này sẽ xóa cả thẻ và lịch sử check-in/out của hội viên.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MemberDAO.deleteMember(selected.getCustomerID());
            showAlert("🗑 Đã xóa toàn bộ dữ liệu của hội viên!");
            loadMembers();
            clearForm();
        }
    }


    @FXML
    private void handleExportList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu danh sách hội viên");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            exportToExcel(file);
        }
    }

    private void exportToExcel(File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách hội viên");

            // Tạo header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Header
            String[] headers = {"Mã hội viên", "Họ tên", "SĐT", "Giới tính", "Tuổi", "Tên gói", "Ngày bắt đầu", "Ngày kết thúc"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Ghi dữ liệu hội viên
            int rowNum = 1;
            for (Membership m : memberList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(m.getCustomerID());
                row.createCell(1).setCellValue(m.getName());
                row.createCell(2).setCellValue(m.getPhone());
                row.createCell(3).setCellValue(m.getGender());
                row.createCell(4).setCellValue(m.getAge());
                row.createCell(5).setCellValue(m.getPackageName() != null ? m.getPackageName() : "");
                row.createCell(6).setCellValue(m.getStartDate() != null ? m.getStartDate().toString() : "");
                row.createCell(7).setCellValue(m.getEndDate() != null ? m.getEndDate().toString() : "");
            }

            // Tự động căn chỉnh độ rộng cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi ra file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

            showAlert("✅ Đã xuất danh sách đầy đủ ra file Excel!");
        } catch (IOException e) {
            showAlert("❌ Lỗi khi xuất file Excel: " + e.getMessage());
        }
    }


    private void loadMembers() {
        List<Membership> members = MemberDAO.getAllExtendedMembers();
        memberList = FXCollections.observableArrayList(members);
        tableView.setItems(memberList);
    }

    private Membership getFormData() {
        String idText = tfCustomerID.getText();
        String nameText = tfName.getText();
        String phoneText = tfPhone.getText();
        String genderText = cbGender.getValue();
        String ageText = tfAge.getText();

        // Kiểm tra rỗng
        if (idText.isEmpty() || nameText.isEmpty() || phoneText.isEmpty() || genderText == null || ageText.isEmpty()) {
            showAlert("⚠ Vui lòng nhập đầy đủ thông tin!");
            return null;
        }

        try {
            int id = Integer.parseInt(idText);

            // Kiểm tra sdt hội viên đã tồn tại trong cơ sở dữ liệu
            if (MemberDAO.isCustomerPhoneExists(phoneText, id)) {
                showAlert("⚠ Sdt hội viên này đã tồn tại.");
                return null;
            }
            if (id < 100000 || id > 999999) {
                showAlert("⚠ Mã hội viên phải gồm đúng 6 chữ số!");
                return null;
            }

            if (!phoneText.matches("\\d{10}")) {
                showAlert("⚠ Số điện thoại phải gồm đúng 10 chữ số!");
                return null;
            }
            if (phoneText.charAt(0) != '0') {
                showAlert("⚠ Số điện thoại phải bắt đầu bằng chữ số 0!");
                return null;
            }

            int age= Integer.parseInt(ageText);
            if (age <= 12) {
                showAlert("⚠ Tuổi phải lớn hơn 12!");
                return null;
            }

            return new Membership(id, nameText, phoneText, genderText, age, "", "", "");
        } catch (NumberFormatException e) {
            showAlert("⚠ Mã hội viên và tuổi phải là số!");
            return null;
        }
    }

    private void searchCustomer() {
        String keyword = inputSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadMembers(); // nếu không nhập gì thì load toàn bộ
        } else {
            List<Membership> results = MemberDAO.searchMembers(keyword);
            tableView.setItems(FXCollections.observableArrayList(results));
        }
    }

    private void handleTableClick(MouseEvent event) {
        Membership selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tfCustomerID.setText(String.valueOf(selected.getCustomerID()));
            tfName.setText(selected.getName());
            tfPhone.setText(selected.getPhone());
            cbGender.setValue(selected.getGender());
            tfAge.setText(String.valueOf(selected.getAge()));

            tfCustomerID.setDisable(true); // ✅ Không cho chỉnh sửa khi cập nhật
        }
    }

    private void clearForm() {
        tfCustomerID.clear();
        tfName.clear();
        tfPhone.clear();
        cbGender.setValue(null);
        tfAge.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
