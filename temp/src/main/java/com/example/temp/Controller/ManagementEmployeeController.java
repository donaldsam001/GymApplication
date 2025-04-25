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

public class ManagementEmployeeController {

    @FXML private TableView<Employee> tableView, tableUp, tableDel;
    @FXML private TableColumn<Employee, Integer> colID, colIDUp, colIDDel;
    @FXML private TableColumn<Employee, String> colName, colNameUp, colNameDel;
    @FXML private TableColumn<Employee, String> colPhone, colPhoneUp, colPhoneDel;
    @FXML private TableColumn<Employee, String> colRole, colRoleUp, colRoleDel;
    @FXML private TableColumn<Employee, Void> colDel;

    @FXML private TextField inputCode, inputName, inputPhoneNumber, inputRole, inputSearch;
    @FXML private Button handelCreate;

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

        handelCreate.setOnAction(e -> {
            createEmployee();
            loadEmployees();
        });
    }

    private void setupTableView() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        tableView.setItems(employeeList);
    }


    private void setupDeleteTable() {
        colIDDel.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameDel.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhoneDel.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRoleDel.setCellValueFactory(new PropertyValueFactory<>("role"));

        colDel.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Xóa");

            {
                deleteBtn.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    new EmployDAO().deleteEmployee(emp.getId());
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
        colRoleUp.setCellValueFactory(new PropertyValueFactory<>("role"));

        colNameUp.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhoneUp.setCellFactory(TextFieldTableCell.forTableColumn());
        colRoleUp.setCellFactory(TextFieldTableCell.forTableColumn());

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

        colRoleUp.setOnEditCommit(event -> {
            Employee e = event.getRowValue();
            e.setRole(event.getNewValue());
            updateEmployee(e);
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
            String role = inputRole.getText();
            String password = "123"; // hoặc để người dùng nhập nếu cần

            if (name.isEmpty() || phone.isEmpty() || role.isEmpty()) {
                showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", Alert.AlertType.ERROR);
                return;
            }

            Employee e = new Employee(id, name, password, phone, role);
            new EmployDAO().insertEmployee(e);
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
        String keyword = inputSearch.getText().trim();
        EmployDAO dao = new EmployDAO();
        List<Employee> result = dao.searchEmployee(keyword);
        employeeList.setAll(result);
    }

    private void clearInputs() {
        inputCode.clear();
        inputName.clear();
        inputPhoneNumber.clear();
        inputRole.clear();
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
