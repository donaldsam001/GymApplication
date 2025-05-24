package com.example.temp.Controller;

import com.example.temp.DAO.EmployDAO;
import com.example.temp.Models.Employee;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;
import java.util.Optional;

public class ManagementEmployeeController {

    @FXML private TableView<Employee> tableView, tableUp, tableDel;
    @FXML private TableColumn<Employee, Integer> colID, colIDUp, colIDDel;
    @FXML private TableColumn<Employee, String> colName, colNameUp, colNameDel;
    @FXML private TableColumn<Employee, String> colPhone, colPhoneUp, colPhoneDel;
    @FXML private TableColumn<Employee, Void> colDel;
    @FXML private TableColumn<Employee, String> colRole, colRoleUp, colRoleDel;


    @FXML private TextField inputCode, inputName, inputPhoneNumber, inputSearch, inputSearchUp, inputSearchDel;
    @FXML private Button handelCreate, btnDel;
    @FXML private Tab tabView, tabDel, tabUp;
    @FXML private TabPane tabPane;
    @FXML private CheckBox checkIsReceptionist;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private ObservableList<Employee> deleteList = FXCollections.observableArrayList();
    private ObservableList<Employee> changeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableView();
        setupDeleteTable();
        setupChangeTable();
        loadEmployees();

        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchEmployee());
        inputSearchUp.textProperty().addListener((obs, oldVal, newVal) -> searchEmployee());
        inputSearchDel.textProperty().addListener((obs, oldVal, newVal) -> searchEmployee());



        handelCreate.setOnAction(e -> {
            createEmployee();
            loadEmployees();
        });
    }

    private void setupTableView() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRole.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().isReceptionist();
            return new ReadOnlyStringWrapper(status ? "Tiếp tân" : "");
        });
        tableView.setItems(employeeList);
    }


    private void setupDeleteTable() {
        colIDDel.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameDel.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhoneDel.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRoleDel.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().isReceptionist();
            return new ReadOnlyStringWrapper(status ? "Tiếp tân" : "");
        });
        colDel.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Xóa");

            {
                deleteBtn.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    confirmAndDeleteEmployee(emp.getId());
                    loadEmployees();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        tableDel.setItems(deleteList);
    }


    private void setupChangeTable() {
        tableUp.setEditable(true);

        colIDUp.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameUp.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhoneUp.setCellValueFactory(new PropertyValueFactory<>("phone"));

        colNameUp.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhoneUp.setCellFactory(TextFieldTableCell.forTableColumn());

        colNameUp.setOnEditCommit(event -> {
            Employee e = event.getRowValue();
            e.setName(event.getNewValue());
            updateEmployee(e);
        });

        colPhoneUp.setOnEditCommit(event -> {
            Employee e = event.getRowValue();
            e.setPhone(event.getNewValue());
            updateEmployee(e);
        });
        colRoleUp.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().isReceptionist();
            return new ReadOnlyStringWrapper(status ? "Tiếp tân" : "");
        });

        tableUp.setItems(changeList);
    }


    private void loadEmployees() {
        EmployDAO dao = new EmployDAO();
        List<Employee> list = dao.getAllEmployees(); // nên đổi tên hàm này cho phù hợp
        employeeList.setAll(list);
        deleteList.setAll(list);
        changeList.setAll(list);
    }

    private void createEmployee() {
        try {
            int id = Integer.parseInt(inputCode.getText());
            String name = inputName.getText();
            String phone = inputPhoneNumber.getText();
            boolean isReceptionist = checkIsReceptionist.isSelected();
            String password = "";

            if (isReceptionist){
                password = generateRandomPassword(8); // hoặc 10, 12 ký tự tùy bạn
            }

            if (name.isEmpty() || phone.isEmpty() ) {
                showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", Alert.AlertType.ERROR);
                return;
            }

            if (id < 100000 || id > 999999) {
                showAlert("Lỗi", "Mã nhân viên không hợp lệ.", Alert.AlertType.ERROR);
                return;
            }


            if (!phone.matches("\\d{10}")) {
                showAlert("Lỗi", "Số điện thoại phải gồm đúng 10 chữ số.", Alert.AlertType.ERROR);
                return;
            }

            EmployDAO dao = new EmployDAO();
            if (dao.isEmployeeIdExists(id)) {
                showAlert("Lỗi", "Mã nhân viên đã tồn tại. Vui lòng nhập mã khác.", Alert.AlertType.ERROR);
                return;
            }

            Employee e = new Employee(id, name, password, phone, isReceptionist);
            dao.insertEmployee(e);
            showAlert("Thành công", "Thêm nhân viên thành công!", Alert.AlertType.INFORMATION);
            clearInputs();
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Mã nhân viên phải là số.", Alert.AlertType.ERROR);
        }
    }


    private void updateEmployee(Employee e) {
        new EmployDAO().updateEmployee(e);
        showAlert("Thành công", "Đã cập nhật thông tin nhân viên.", Alert.AlertType.INFORMATION);
        loadEmployees();
    }

    private void searchEmployee() {
        EmployDAO dao = new EmployDAO();

        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        String keyword;

        if (selectedTab == tabView) {
            keyword = inputSearch.getText().trim();
            employeeList.setAll(dao.searchEmployee(keyword));
        } else if (selectedTab == tabUp) {
            keyword = inputSearchUp.getText().trim();
            changeList.setAll(dao.searchEmployee(keyword));
        } else if (selectedTab == tabDel) {
            keyword = inputSearchDel.getText().trim();
            deleteList.setAll(dao.searchEmployee(keyword));
        }
    }


    private void clearInputs() {
        inputCode.clear();
        inputName.clear();
        inputPhoneNumber.clear();
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            password.append(chars.charAt(randomIndex));
        }
        return password.toString();
    }

    public void confirmAndDeleteEmployee(int id) {
        Employee emp = deleteList.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        String name = (emp != null) ? emp.getName() : "";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa nhân viên " + name + " (ID " + id + ")?");
        alert.setContentText("Thao tác này không thể hoàn tác.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            EmployDAO dao = new EmployDAO();
            dao.deleteEmployee(id);
            loadEmployees();
        }
    }
}
