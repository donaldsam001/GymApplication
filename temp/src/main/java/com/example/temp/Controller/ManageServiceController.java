package com.example.temp.Controller;

import com.example.temp.DAO.EquipmentDAO;
import com.example.temp.DAO.MembershipPackageDAO;
import com.example.temp.Models.Equipment;
import com.example.temp.Models.MembershipPackage;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;

public class ManageServiceController {

    @FXML
    private TableView<MembershipPackage> listPackage, listPackageChange, listPackageDel;
    @FXML
    private TableColumn<MembershipPackage, Integer> colId, colIdChange, colIdDel;
    @FXML
    private TableColumn<MembershipPackage, String> colName, colNameChange, colNameDel;
    @FXML
    private TableColumn<MembershipPackage, String> colDescription, colDescriptionChange, colDescriptionDel;
    @FXML
    private TableColumn<MembershipPackage, Integer> colExpDate, colExpDateChange, colExpDateDel;
    @FXML
    private TableColumn<MembershipPackage, Float> colPrice, colPriceChange, colPriceDel;
    @FXML
    private TableColumn<MembershipPackage, String> colStatus, colStatusChange, colStatusDel;

    @FXML
    private TableColumn<MembershipPackage, Void> colDel,colChange;

    @FXML
    private TextField inputCode, inputName, inputDescription, inputExpDate, inputPrice, inputSearch, inputSearchChange, inputSearchDel;

    @FXML
    private Button handelCreate, btnDeleteAll;
    @FXML
    private Tab tabViewPackages, tabViewPackagesDel, tabViewPackagesChange;
    @FXML
    private TabPane tabPane;

    private ObservableList<MembershipPackage> packageList = FXCollections.observableArrayList();
    private ObservableList<MembershipPackage> deleteList = FXCollections.observableArrayList();
    private ObservableList<MembershipPackage> changeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableView();
        setupDeleteTable();
        setupChangeTable();
        loadMembershipPackages();

        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchPackages());
        inputSearchChange.textProperty().addListener((obs, oldVal, newVal) -> searchPackages());
        inputSearchDel.textProperty().addListener((obs, oldVal, newVal) -> searchPackages());


        handelCreate.setOnAction(e -> {
            createMembershipPackage();
            loadMembershipPackages();
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == tabViewPackages || newTab == tabViewPackagesDel || newTab == tabViewPackagesChange) {
                loadMembershipPackages();
            }
        });

        listPackageChange.setOnMouseClicked(event -> {
            MembershipPackage selected = listPackageChange.getSelectionModel().getSelectedItem();
            if (selected != null) {
                inputCode.setText(String.valueOf(selected.getId()));
                inputName.setText(selected.getName());
                inputDescription.setText(selected.getDescription());
                inputExpDate.setText(String.valueOf(selected.getExp()));
                inputPrice.setText(String.valueOf(selected.getPrice()));
            }
        });

    }

    private void setupTableView() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        listPackage.setItems(packageList);
    }

    private void setupDeleteTable() {
        colIdDel.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameDel.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescriptionDel.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpDateDel.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colPriceDel.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatusDel.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDel.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Xóa");
            {
                deleteBtn.setOnAction(event -> {
                    MembershipPackage membershipPackage = getTableView().getItems().get(getIndex());
                    MembershipPackageDAO membershipPackageDAO = new MembershipPackageDAO();
                    membershipPackageDAO.deleteMembershipPackage(membershipPackage.getId());
                    loadMembershipPackages();
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

        listPackageDel.setItems(deleteList);
    }

    private void setupChangeTable() {
        listPackageChange.setEditable(true);

        colIdChange.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameChange.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescriptionChange.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpDateChange.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colPriceChange.setCellValueFactory(new PropertyValueFactory<>("price"));

        colNameChange.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescriptionChange.setCellFactory(TextFieldTableCell.forTableColumn());
        colExpDateChange.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colPriceChange.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        colNameChange.setOnEditCommit(event -> {
            MembershipPackage mp = event.getRowValue();
            mp.setName(event.getNewValue());
            updateMembershipPackage(mp);
        });

        colDescriptionChange.setOnEditCommit(event -> {
            MembershipPackage mp = event.getRowValue();
            mp.setDescription(event.getNewValue());
            updateMembershipPackage(mp);
        });

        colExpDateChange.setOnEditCommit(event -> {
            MembershipPackage mp = event.getRowValue();
            mp.setExp(event.getNewValue());
            updateMembershipPackage(mp);
        });

        colPriceChange.setOnEditCommit(event -> {
            MembershipPackage mp = event.getRowValue();
            mp.setPrice(event.getNewValue());
            updateMembershipPackage(mp);
        });

        colStatusChange.setCellFactory(ComboBoxTableCell.forTableColumn("Đang hoạt động", "Không hoạt động"));
        colStatusChange.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().getStatus();
            String label = status ? "Đang hoạt động" : "Không hoạt động";
            return new ReadOnlyStringWrapper(label);
        });
        colStatusChange.setOnEditCommit(event -> {
            MembershipPackage mp = event.getRowValue();
            mp.setStatus("Đang hoạt động".equals(event.getNewValue()));
            updateMembershipPackage(mp);
        });

        listPackageChange.setItems(changeList);
    }


    private void loadMembershipPackages() {
        MembershipPackageDAO dao = new MembershipPackageDAO();
        List<MembershipPackage> packages = dao.getData();

        packageList.setAll(packages);
        deleteList.setAll(packages);
        changeList.setAll(packages);
    }

    private void createMembershipPackage() {
        try {
            int id = Integer.parseInt(inputCode.getText());
            String name = inputName.getText();
            String description = inputDescription.getText();
            int exp = Integer.parseInt(inputExpDate.getText());
            float price = Float.parseFloat(inputPrice.getText());

            if (name.isEmpty() || description.isEmpty()) {
                showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", Alert.AlertType.ERROR);
                return;
            }

            MembershipPackage membershipPackage = new MembershipPackage(id, name, price, description, exp, true);
            MembershipPackageDAO dao = new MembershipPackageDAO();
            dao.insertMembershipPackage(membershipPackage);

            showAlert("Thành công", "Đã tạo gói hội viên thành công!", Alert.AlertType.INFORMATION);
            clearInputs();
        } catch (NumberFormatException ex) {
            showAlert("Lỗi", "Định dạng số không hợp lệ.", Alert.AlertType.ERROR);
        }
    }

    private void updateMembershipPackage(MembershipPackage updated) {
        MembershipPackageDAO dao = new MembershipPackageDAO();
        dao.updateMembershipPackage(updated);
        showAlert("Thành công", "Đã cập nhật gói hội viên!", Alert.AlertType.INFORMATION);
        loadMembershipPackages();
    }


    private void searchPackages() {
        MembershipPackageDAO dao = new MembershipPackageDAO();

        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        String keyword;

        if (selectedTab == tabViewPackages) {
            keyword = inputSearch.getText().trim();
            packageList.setAll(dao.searchPackages(keyword));
        } else if (selectedTab == tabViewPackagesChange) {
            keyword = inputSearchChange.getText().trim();
            changeList.setAll(dao.searchPackages(keyword));
        } else if (selectedTab == tabViewPackagesDel) {
            keyword = inputSearchDel.getText().trim();
            deleteList.setAll(dao.searchPackages(keyword));
        }
    }


    private void clearInputs() {
        inputCode.clear();
        inputName.clear();
        inputDescription.clear();
        inputExpDate.clear();
        inputPrice.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    public void DeleteAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setContentText("Bạn có chắc chắn muốn xóa tất cả gói hội viên?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                MembershipPackageDAO dao = new MembershipPackageDAO();
                dao.deleteAllPackages();
                showAlert("Thành công", "Đã xóa toàn bộ gói hội viên.", Alert.AlertType.INFORMATION);
                loadMembershipPackages();
            }
        });
    }


}
