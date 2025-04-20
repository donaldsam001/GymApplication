package com.example.temp.Controller;

import com.example.temp.DAO.EquipmentDAO;
import com.example.temp.Models.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DeviceManagementController {

    @FXML
    private Button handelAdd;

    @FXML
    private TextField inputCode;

    @FXML
    private TextField inputDescription;

    @FXML
    private TextField inputName;

    @FXML
    private TableView<Equipment> equipmentTable;

    @FXML
    private TableColumn<Equipment, String> colID;

    @FXML
    private TableColumn<Equipment, String> colName;

    @FXML
    private TableColumn<Equipment, String> colDescription;

    @FXML
    private TableColumn<Equipment, String> colStatus;

    private ObservableList<Equipment> equipmentList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        handelAdd.setOnAction(event -> handleAdd());
        setupTableView();
        loadEquipmentData();
    }

    private void handleAdd() {
        String code = inputCode.getText();
        String name = inputName.getText();
        String description = inputDescription.getText();

        if (code.isEmpty() || name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Mã và tên thiết bị không được để trống.");
            return;
        }

        try {
            int id = Integer.parseInt(code);
            Equipment equipment = new Equipment(id, name, description, null, "1");

            EquipmentDAO.insertEquipment(equipment);

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thiết bị đã được thêm.");
            clearFields();
            loadEquipmentData();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Mã thiết bị phải là số.");
        }
    }

    private void clearFields() {
        inputCode.clear();
        inputName.clear();
        inputDescription.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupTableView() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        equipmentTable.setItems(equipmentList);
    }

    private void loadEquipmentData() {
        equipmentList.setAll(EquipmentDAO.getAllEquipment());
    }

    @FXML
    void handleDel(ActionEvent event) {

    }







}
