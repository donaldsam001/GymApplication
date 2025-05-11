package com.example.temp.Controller;

import com.example.temp.DAO.MembershipPackageDAO;
import com.example.temp.Models.MembershipPackage;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.text.NumberFormat;
import java.util.Locale;

import java.util.List;

public class ManagementPackageController {

    @FXML
    private TableView<MembershipPackage> listPackage, listPackageChange, listPackageDel;
    @FXML
    private TableColumn<MembershipPackage, Integer> colID, colIdChange, colIdDel;
    @FXML
    private TableColumn<MembershipPackage, String> colName, colNameChange, colNameDel;
    @FXML
    private TableColumn<MembershipPackage, String> colDescription, colDescriptionChange, colDescriptionDel;
    @FXML
    private TableColumn<MembershipPackage, Integer> colExpDate, colExpDateChange, colExpDateDel;
    @FXML
    private TableColumn<MembershipPackage, Integer> colPrice, colPriceChange, colPriceDel;
    @FXML
    private TableColumn<MembershipPackage, String> colStatus, colStatusChange, colStatusDel;

    @FXML
    private TableColumn<MembershipPackage, Void> colDel;

    @FXML
    private TextField inputCode, inputName, inputDescription, inputExpDate, inputPrice, inputSearch, inputSearchChange, inputSearchDel;

    @FXML
    private Button handelCreate;
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
                inputCode.setText(String.valueOf(selected.getPackageID()));
                inputName.setText(selected.getPackageName());
                inputDescription.setText(selected.getDescription());
                inputExpDate.setText(String.valueOf(selected.getExp()));
                inputPrice.setText(String.valueOf(selected.getPrice()));
            }
        });

    }

    private void setupTableView() {
        colID.setCellValueFactory(new PropertyValueFactory<>("packageID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    setText(currencyFormat.format(item)); // Ví dụ: 1000000 -> 1.000.000 ₫
                }
            }
        });

        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colStatus.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().getStatus();
            return new ReadOnlyStringWrapper(status ? "Đang hoạt động" : "Không hoạt động");
        });
        listPackage.setItems(packageList);
    }

    private void setupDeleteTable() {
        colIdDel.setCellValueFactory(new PropertyValueFactory<>("packageID"));
        colNameDel.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colDescriptionDel.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpDateDel.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colPriceDel.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPriceDel.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    setText(currencyFormat.format(item));
                }
            }
        });

        colStatusDel.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().getStatus();
            return new ReadOnlyStringWrapper(status ? "Đang hoạt động" : "Không hoạt động");
        });
        colDel.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Xóa");
            {
                deleteBtn.setOnAction(event -> {
                    MembershipPackage membershipPackage = getTableView().getItems().get(getIndex());
                    MembershipPackageDAO membershipPackageDAO = new MembershipPackageDAO();
                    membershipPackageDAO.deleteMembershipPackage(membershipPackage.getPackageID());
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

        colIdChange.setCellValueFactory(new PropertyValueFactory<>("packageID"));
        colNameChange.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colDescriptionChange.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpDateChange.setCellValueFactory(new PropertyValueFactory<>("exp"));
        colPriceChange.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Hiển thị VNĐ khi không edit, nhưng giữ khả năng edit số
        colPriceChange.setCellFactory(column -> new TextFieldTableCell<>(new IntegerStringConverter()) {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (isEditing()) {
                    setText(item.toString());
                } else {
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    setText(currencyFormat.format(item));
                }
            }
        });

        // Các cột cho phép chỉnh sửa
        colNameChange.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescriptionChange.setCellFactory(TextFieldTableCell.forTableColumn());
        colExpDateChange.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Cập nhật khi chỉnh sửa xong
        colNameChange.setOnEditCommit(event -> {
            MembershipPackage mp = event.getRowValue();
            mp.setPackageName(event.getNewValue());
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

        // Cột trạng thái với combobox
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
            int price = Integer.parseInt(inputPrice.getText());

            if (name.isEmpty() || description.isEmpty()) {
                showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", Alert.AlertType.ERROR);
                return;
            }

            if (id < 100000 || id > 999999) {
                showAlert("Lỗi", "Mã gói hội viên không hợp lệ.", Alert.AlertType.ERROR);
                return;
            }

            if (exp <= 0) {
                showAlert("Lỗi", "Thời hạn từ 1 tháng trở lên.", Alert.AlertType.ERROR);
                return;
            }

            MembershipPackageDAO dao = new MembershipPackageDAO();
            if (dao.isPackageExists(id)) {
                showAlert("Lỗi", "Mã gói hội viên đã tồn tại. Vui lòng nhập mã khác.", Alert.AlertType.ERROR);
                return;
            }
            MembershipPackage membershipPackage = new MembershipPackage(id, name, price, description, exp, true);
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
}
