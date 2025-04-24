package com.example.temp.Controller;

import java.time.format.DateTimeFormatter;
import com.example.temp.DAO.EquipmentDAO;
import com.example.temp.Models.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.ArrayList;

public class DeviceManagementController {

    @FXML
    private Button btnDel;

    private LocalDate repairDate;
    private String repairNote; // nếu cần

    @FXML
    private TableView<Equipment> repairDateTable;

    @FXML
    private TableColumn<Equipment, String> colDescriptionRepair;


    @FXML
    private TableColumn<Equipment, String> colIDRepair;

    @FXML
    private TableColumn<Equipment, String> colMaintenanceContent;


    @FXML
    private TableColumn<Equipment, String> colNameRepair;

    @FXML
    private TableColumn<Equipment, Void> colRepairDate;

    private ObservableList<Equipment> equipmentRepairList = FXCollections.observableArrayList();


//    -----------------------------------------



    @FXML
    private TableView<Equipment> deleteTable;

    @FXML
    private TableColumn<Equipment, Integer> delColID;

    @FXML
    private TableColumn<Equipment, String> delColName;

    @FXML
    private TableColumn<Equipment, String> delColDescription;

    @FXML
    private TableColumn<Equipment, Void> delColAction;

    @FXML
    private ObservableList<Equipment> deleteList = FXCollections.observableArrayList();


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
    private TableColumn<Equipment, LocalDate> colRepairDateV;

    @FXML
    private TableColumn<Equipment, String> colStatus;

    private ObservableList<Equipment> equipmentList = FXCollections.observableArrayList();

    @FXML
    void initialize() {

        handelAdd.setOnAction(event -> handleAdd());
        setupTableView();
        loadEquipmentData();
        setupDeleteTable();
        loadDeleteTable();
        setupRepairTable();      // << Thêm dòng này
        loadRepairTable();

        equipmentTable.setRowFactory(tv -> {
            TableRow<Equipment> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    selectedEquipment = row.getItem();
                    inputCode.setText(String.valueOf(selectedEquipment.getId()));
                    inputName.setText(selectedEquipment.getName());
                    inputDescription.setText(selectedEquipment.getDescription());
                    handelAdd.setText("Cập nhật thiết bị");
                }
            });
            return row;
        });
    }

//    LÊN LỊCH BẢO TRÌ
    private void setupRepairTable() {
        colIDRepair.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameRepair.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescriptionRepair.setCellValueFactory(new PropertyValueFactory<>("description"));
//        colRepairDate.setCellValueFactory(new PropertyValueFactory<>("repairDate"));

        colRepairDate.setCellFactory(param -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(event -> {
                    Equipment equipment = getTableView().getItems().get(getIndex());
                    LocalDate selectedDate = datePicker.getValue();

                    if (equipment != null && selectedDate != null) {
                        // Cập nhật ngày bảo trì trong model
                        equipment.setRepairDate(selectedDate);

                        // Cập nhật xuống DB
                        EquipmentDAO equipmentDAO = new EquipmentDAO();
                        equipmentDAO.updateRepairDate(equipment.getId(), selectedDate);

                        // Refresh lại bảng
                        loadRepairTable();
                        loadEquipmentData(); // cập nhật luôn cả bảng chính
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Equipment equipment = getTableView().getItems().get(getIndex());
                    datePicker.setValue(equipment.getRepairDate());
                    setGraphic(datePicker);
                }
            }
        });


        colMaintenanceContent.setCellValueFactory(new PropertyValueFactory<>("repairNote"));

        // Initialize the table with an empty list, populated later
        repairDateTable.setItems(equipmentRepairList);
    }

    private void loadRepairTable() {
        EquipmentDAO equipmentDAO = new EquipmentDAO();
        equipmentRepairList.setAll(equipmentDAO.getAllEquipment());
    }

// THÊM THIẾT BỊ
    private Equipment selectedEquipment = null;

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
            Equipment equipment = new Equipment(id, name, description, null, "1", null);
            if (selectedEquipment == null) {
                EquipmentDAO equipmentDAO = new EquipmentDAO();
                equipmentDAO.insertEquipment(equipment);

                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thiết bị đã được thêm.");
            } else {
                equipment.setId(selectedEquipment.getId());
                EquipmentDAO equipmentDAO = new EquipmentDAO();
                equipmentDAO.insertEquipment(equipment);

                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thiết bị đã được cập nhật.");
                selectedEquipment = null;
                handelAdd.setText("Thêm thiết bị");
            }

            clearFields();
            loadEquipmentData();
            loadDeleteTable();

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
        colRepairDateV.setCellValueFactory(new PropertyValueFactory<>("repairDate"));

        colRepairDateV.setCellFactory(column -> new TableCell<Equipment, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        equipmentTable.setItems(equipmentList);
    }

    private void loadEquipmentData() {
        EquipmentDAO equipmentDAO = new EquipmentDAO();
        equipmentList.setAll(equipmentDAO.getAllEquipment());
    }

//    XÓA THIEEST BỊ
    private void setupDeleteTable() {
        delColID.setCellValueFactory(new PropertyValueFactory<>("id"));
        delColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        delColDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        delColAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Xóa");
            {
                deleteBtn.setOnAction(event -> {
                    Equipment equipment = getTableView().getItems().get(getIndex());
                    EquipmentDAO equipmentDAO = new EquipmentDAO();
                    equipmentDAO.deleteEquipment(equipment.getId());
                    loadDeleteTable();
                    loadEquipmentData(); // update the view tab too
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });

        deleteTable.setItems(deleteList);
    }

    private void loadDeleteTable() {
        EquipmentDAO equipmentDAO = new EquipmentDAO();
        deleteList.setAll(equipmentDAO.getAllEquipment());
    }

    @FXML
    void handleDel(ActionEvent event) {
        for (Equipment equipment : new ArrayList<>(deleteList)) {
            EquipmentDAO equipmentDAO = new EquipmentDAO();
            equipmentDAO.deleteEquipment(equipment.getId());
        }
        loadDeleteTable();
        loadEquipmentData();
        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tất cả thiết bị đã được xóa.");
    }

}
