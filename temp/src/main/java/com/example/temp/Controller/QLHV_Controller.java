
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
import java.time.LocalDate;
import java.util.List;

public class QLHV_Controller {
    @FXML private TextField tfCustomerID;
    @FXML private TextField tfName;
    @FXML private TextField tfPhone;
    @FXML private ComboBox<String> cbGender;
    @FXML private ComboBox<String> cbSchedule;
    @FXML private TextField tfAge;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;

    @FXML private TableView<Member> tableView;
    @FXML private TableColumn<Member, String> colCustomerID;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, String> colPhone;
    @FXML private TableColumn<Member, String> colGender;
    @FXML private TableColumn<Member, String> colSchedule;
    @FXML private TableColumn<Member, LocalDate> colStartDate;
    @FXML private TableColumn<Member, LocalDate> colEndDate;
    @FXML private TableColumn<Member, Integer> colAge;

    private ObservableList<Member> memberList;

    @FXML
    public void initialize() {
        colCustomerID.setCellValueFactory(cellData -> cellData.getValue().customerIDProperty());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colPhone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        colGender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        colSchedule.setCellValueFactory(cellData -> cellData.getValue().scheduleProperty());
        colStartDate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        colEndDate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        colAge.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());

        cbGender.getItems().addAll("Nam", "Nữ");
        cbSchedule.getItems().addAll("Sáng", "Chiều", "Tối");

        loadMembers();
        tableView.setOnMouseClicked(this::handleTableClick);
    }

    @FXML
    private void handleAdd() {
        Member member = getFormData();
        if (member == null) return;

        MemberDAO.addMember(member);
        showAlert("✅ Thêm hội viên thành công!");
        loadMembers();
        clearForm();
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
            writer.write("ID, Họ tên, SĐT, Giới tính, Lịch tập, Ngày bắt đầu, Ngày kết thúc, Tuổi\n");
            for (Member m : memberList) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%d\n",
                        m.getCustomerID(), m.getName(), m.getPhone(), m.getGender(), m.getSchedule(),
                        m.getStartDate(), m.getEndDate(), m.getAge()));
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
        try {
            String id = tfCustomerID.getText();
            String name = tfName.getText();
            String phone = tfPhone.getText();
            String gender = cbGender.getValue();
            String schedule = cbSchedule.getValue();
            LocalDate start = dpStartDate.getValue();
            LocalDate end = dpEndDate.getValue();
            int age = Integer.parseInt(tfAge.getText());

            if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || gender == null || schedule == null || start == null || end == null) {
                showAlert("⚠ Vui lòng nhập đầy đủ thông tin!");
                return null;
            }

            return new Member(id, name, phone, gender, schedule, start, end, age);
        } catch (NumberFormatException e) {
            showAlert("⚠ Tuổi phải là số!");
            return null;
        }
    }

    private void handleTableClick(MouseEvent event) {
        Member selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tfCustomerID.setText(selected.getCustomerID());
            tfName.setText(selected.getName());
            tfPhone.setText(selected.getPhone());
            cbGender.setValue(selected.getGender());
            cbSchedule.setValue(selected.getSchedule());
            dpStartDate.setValue(selected.getStartDate());
            dpEndDate.setValue(selected.getEndDate());
            tfAge.setText(String.valueOf(selected.getAge()));
        }
    }

    private void clearForm() {
        tfCustomerID.clear();
        tfName.clear();
        tfPhone.clear();
        cbGender.setValue(null);
        cbSchedule.setValue(null);
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);
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