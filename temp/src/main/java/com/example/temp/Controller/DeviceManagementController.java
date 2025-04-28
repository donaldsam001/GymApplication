package com.example.temp.Controller;

import com.example.temp.DAO.EquipmentDAO;
import com.example.temp.Models.Equipment;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.util.List;

public class DeviceManagementController {

    @FXML private TableView<Equipment> equipmentTable, repairDateTable, deleteTable;
    @FXML private TableColumn<Equipment, Integer> colId, colIDRepair, colIdDel;
    @FXML private TableColumn<Equipment, String> colName, colNameRepair, colNameDel;
    @FXML private TableColumn<Equipment, String> colDescription, colDescriptionRepair, colDescriptionDel;
    @FXML private TableColumn<Equipment, String> colMaintenanceContent;
    @FXML private TableColumn<Equipment, String> colRepairDateV;
    @FXML private TableColumn<Equipment, String> colStatus, colStatusRepair, colStatusDel;
    @FXML private TableColumn<Equipment, Void> colDel;
    @FXML private TableColumn<Equipment, LocalDate> colRepairDate;

    @FXML private TextField inputCode, inputName, inputDescription, inputSearch, inputSearchRepair, inputSearchDel;
    @FXML private Button btnAdd, btnDeleteAll;
    @FXML private Tab tabList, tabMaintaince, tabDel;
    @FXML private TabPane tabPane;

    private final ObservableList<Equipment> equipmentList = FXCollections.observableArrayList();
    private final ObservableList<Equipment> changeList = FXCollections.observableArrayList();
    private final ObservableList<Equipment> deleteList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableView();
        setupChangeTable();
        setupDeleteTable();
        loadEquipment();

        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchEquipment());
        inputSearchRepair.textProperty().addListener((obs, oldVal, newVal) -> searchEquipment());
        inputSearchDel.textProperty().addListener((obs, oldVal, newVal) -> searchEquipment());

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == tabList || newTab == tabMaintaince || newTab == tabDel) {
                loadEquipment();
            }
        });

        btnAdd.setOnAction(e -> {
            createEquipment();
            loadEquipment();
        });

        repairDateTable.setOnMouseClicked(event -> {
            Equipment selected = repairDateTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                inputCode.setText(String.valueOf(selected.getId()));
                inputName.setText(selected.getName());
                inputDescription.setText(selected.getDescription());
            }
        });
    }

    private void setupTableView() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().getStatus();
            return new ReadOnlyStringWrapper(status ? "Đang hoạt động" : "Không hoạt động");
        });
        colRepairDateV.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getRepairDate();
            return new ReadOnlyStringWrapper(date != null ? date.toString() : "");
        });

        equipmentTable.setItems(equipmentList);
    }

    private void setupDeleteTable() {
        colIdDel.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameDel.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescriptionDel.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatusDel.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().getStatus();
            return new ReadOnlyStringWrapper(status ? "Đang hoạt động" : "Không hoạt động");
        });

        colDel.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Xóa");
            {
                deleteBtn.setOnAction(e -> {
                    Equipment eq = getTableView().getItems().get(getIndex());
                    new EquipmentDAO().deleteEquipment(eq.getId());
                    loadEquipment();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        deleteTable.setItems(deleteList);
    }

    private void setupChangeTable() {
        repairDateTable.setEditable(true);
        colRepairDate.setCellValueFactory(new PropertyValueFactory<>("repairDate"));
        colIDRepair.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameRepair.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescriptionRepair.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMaintenanceContent.setCellValueFactory(new PropertyValueFactory<>("maintenanceNote"));
        colStatusRepair.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().getStatus();
            return new ReadOnlyStringWrapper(status ? "Đang hoạt động" : "Không hoạt động");
        });

        colNameRepair.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescriptionRepair.setCellFactory(TextFieldTableCell.forTableColumn());
        colMaintenanceContent.setCellFactory(TextFieldTableCell.forTableColumn());
        colStatusRepair.setCellFactory(ComboBoxTableCell.forTableColumn("Đang hoạt động", "Không hoạt động"));

        colNameRepair.setOnEditCommit(event -> {
            Equipment eq = event.getRowValue();
            eq.setName(event.getNewValue());
            updateEquipment(eq);
        });

        colDescriptionRepair.setOnEditCommit(event -> {
            Equipment eq = event.getRowValue();
            eq.setDescription(event.getNewValue());
            updateEquipment(eq);
        });

        colMaintenanceContent.setOnEditCommit(event -> {
            Equipment eq = event.getRowValue();
            eq.setMaintenanceNote(event.getNewValue());
            updateEquipment(eq);
        });

        colStatusRepair.setOnEditCommit(event -> {
            Equipment eq = event.getRowValue();
            eq.setStatus("Đang hoạt động".equals(event.getNewValue()));
            updateEquipment(eq);
        });
        colRepairDate.setCellFactory(column -> new TableCell<Equipment, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(event -> {
                    Equipment eq = getTableView().getItems().get(getIndex());
                    eq.setRepairDate(datePicker.getValue());
                    updateEquipment(eq);
                });
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    datePicker.setValue(item);
                    setGraphic(datePicker);
                }
            }
        });


        repairDateTable.setItems(changeList);
    }

    private void loadEquipment() {
        EquipmentDAO dao = new EquipmentDAO();
        List<Equipment> list = dao.getAllEquipment();
        equipmentList.setAll(list);
        changeList.setAll(list);
        deleteList.setAll(list);
    }

    private void createEquipment() {
        try {
            int id = Integer.parseInt(inputCode.getText());
            String name = inputName.getText();
            String description = inputDescription.getText();

            if (name.isEmpty() || description.isEmpty()) {
                showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", Alert.AlertType.ERROR);
                return;
            }

            EquipmentDAO dao = new EquipmentDAO() ;
            if (dao.isEquipmentIdExists(id)) {
                showAlert("Lỗi", "Mã thiết bị đã tồn tại. Vui lòng nhập mã khác.", Alert.AlertType.ERROR);
                return;
            }

            Equipment equipment = new Equipment(id, name, description, null, true, null);
            dao.insertEquipment(equipment);

            showAlert("Thành công", "Thiết bị đã được thêm!", Alert.AlertType.INFORMATION);
            clearInputs();
        } catch (NumberFormatException ex) {
            showAlert("Lỗi", "Định dạng mã thiết bị không hợp lệ.", Alert.AlertType.ERROR);
        }
    }

    private void updateEquipment(Equipment eq) {
        new EquipmentDAO().updateEquipment(eq);
        showAlert("Thành công", "Thiết bị đã được cập nhật!", Alert.AlertType.INFORMATION);
        loadEquipment();
    }

    private void searchEquipment() {
        EquipmentDAO dao = new EquipmentDAO();
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        String keyword = "";

        if (selectedTab == tabList) {
            keyword = inputSearch.getText().trim();
            equipmentList.setAll(dao.searchEquipment(keyword));
        } else if (selectedTab == tabMaintaince) {
            keyword = inputSearchRepair.getText().trim();
            changeList.setAll(dao.searchEquipment(keyword));
        } else if (selectedTab == tabDel) {
            keyword = inputSearchDel.getText().trim();
            deleteList.setAll(dao.searchEquipment(keyword));
        }
    }

    private void clearInputs() {
        inputCode.clear();
        inputName.clear();
        inputDescription.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
