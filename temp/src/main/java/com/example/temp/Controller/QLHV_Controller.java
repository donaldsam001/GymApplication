package com.example.temp.Controller;

import com.example.temp.DAO.MemberDAO;
import com.example.temp.Models.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class QLHV_Controller {
    @FXML private TextField tfCustomerID;
    @FXML private TextField tfName;
    @FXML private TextField tfPhone;
    @FXML private ComboBox<String> cbGender;
    @FXML private TextField tfAge;

    @FXML private TableView<Member> tableView;
    @FXML private TableColumn<Member, Integer> colCustomerID;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, String> colPhone;
    @FXML private TableColumn<Member, String> colGender;
    @FXML private TableColumn<Member, Integer> colAge;

    private ObservableList<Member> memberList;

    @FXML
    public void initialize() {
        colCustomerID.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colPhone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        colGender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        colAge.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
        cbGender.getItems().addAll("Nam", "Nữ");
        loadMembers();
        tableView.setOnMouseClicked(this::handleTableClick);
    }

    @FXML
    private void handleAdd() {
        Member member = getFormData();
        if (member == null) return;

        int id = member.getCustomerID();

        // Kiểm tra mã hội viên đã tồn tại trong cơ sở dữ liệu
        if (MemberDAO.isCustomerIDExists(id)) {
            showAlert("⚠ Mã hội viên này đã tồn tại.");
            return;
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
        Member selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("⚠ Vui lòng chọn hội viên để cập nhật!");
            return;
        }

        Member member = getFormData();
        if (member == null) return;

        MemberDAO.updateMember(member);
        showAlert("✅ Cập nhật hội viên thành công!");
        loadMembers();
        clearForm();
    }

    @FXML
    private void handleDelete() {
        Member selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            MemberDAO.deleteMember(selected.getCustomerID());
            showAlert("🗑 Đã xóa hội viên!");
            loadMembers();
            clearForm();
        } else {
            showAlert("⚠ Vui lòng chọn hội viên để xóa!");
        }
    }

    @FXML
    private void handleExportList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu danh sách hội viên");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            exportToCSV(file);
        }
    }

    private void exportToCSV(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("ID, Họ tên, SĐT, Giới tính, Tuổi\n");
            for (Member m : memberList) {
                writer.write(String.format("%s,%s,%s,%s,%d\n",
                        m.getCustomerID(), m.getName(), m.getPhone(), m.getGender(), m.getAge()));
            }
            showAlert("✅ Đã xuất danh sách ra file CSV!");
        } catch (IOException e) {
            showAlert("❌ Lỗi khi xuất file: " + e.getMessage());
        }
    }

    private void loadMembers() {
        List<Member> members = MemberDAO.getAllMembers();
        memberList = FXCollections.observableArrayList(members);
        tableView.setItems(memberList);
    }

    private Member getFormData() {
        String idText = tfCustomerID.getText();
        String name = tfName.getText();
        String phone = tfPhone.getText();
        String gender = cbGender.getValue();
        String ageText = tfAge.getText();

        // Kiểm tra rỗng
        if (idText.isEmpty() || name.isEmpty() || phone.isEmpty() || gender == null || ageText.isEmpty()) {
            showAlert("⚠ Vui lòng nhập đầy đủ thông tin!");
            return null;
        }

        try {
            int id = Integer.parseInt(idText);
            if (id < 100000 || id > 999999) {
                showAlert("⚠ Mã hội viên phải gồm đúng 6 chữ số!");
                return null;
            }

            if (!phone.matches("\\d{10}")) {
                showAlert("⚠ Số điện thoại phải gồm đúng 10 chữ số!");
                return null;
            }

            int age = Integer.parseInt(ageText);
            if (age <= 0) {
                showAlert("⚠ Tuổi phải lớn hơn 0!");
                return null;
            }

            return new Member(id, name, phone, gender, age);
        } catch (NumberFormatException e) {
            showAlert("⚠ Mã hội viên và tuổi phải là số!");
            return null;
        }
    }


    private void handleTableClick(MouseEvent event) {
        Member selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tfCustomerID.setText(String.valueOf(selected.getCustomerID()));
            tfName.setText(selected.getName());
            tfPhone.setText(selected.getPhone());
            cbGender.setValue(selected.getGender());
            tfAge.setText(String.valueOf(selected.getAge()));
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

    // Kiểm tra sự tồn tại của mã hội viên trong cơ sở dữ liệu
    private boolean isCustomerIDExistsInQLHV(int customerID) {
        return MemberDAO.getAllMembers().stream()
                .anyMatch(member -> member.getCustomerID() == customerID);
    }
}
