/**
 * Sample Skeleton for 'management-employee.fxml' Controller Class
 */

package com.example.temp.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.temp.DAO.EmployDAO;
import com.example.temp.DAO.MembershipPackageDAO;
import com.example.temp.Models.Employee;
import com.example.temp.Models.MembershipPackage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ManagementEmployeeController {

    @FXML
    private Button handelCreate;

    @FXML
    private TextField inputCode;

    @FXML
    private TextField inputName;

    @FXML
    private TextField inputPhoneNumber;

    @FXML
    private TextField inputRole;

    @FXML
    private TextField inputSearch;

    @FXML
    private TextField inputSearchX;

    @FXML
    private TextField inputSearchX1;
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert handelCreate != null : "fx:id=\"handelCreate\" was not injected: check your FXML file 'management-employee.fxml'.";
        assert inputCode != null : "fx:id=\"inputCode\" was not injected: check your FXML file 'management-employee.fxml'.";
        assert inputName != null : "fx:id=\"inputName\" was not injected: check your FXML file 'management-employee.fxml'.";
        assert inputPhoneNumber != null : "fx:id=\"inputPhoneNumber\" was not injected: check your FXML file 'management-employee.fxml'.";
        assert inputSearch != null : "fx:id=\"inputSearch\" was not injected: check your FXML file 'management-employee.fxml'.";
        assert inputSearchX != null : "fx:id=\"inputSearchX\" was not injected: check your FXML file 'management-employee.fxml'.";
        assert inputSearchX1 != null : "fx:id=\"inputSearchX1\" was not injected: check your FXML file 'management-employee.fxml'.";
        handelCreate.setOnAction(e -> createEmployee());
    }


    private void createEmployee() {
        String code = inputCode.getText();
        String name = inputName.getText();

        String phone = inputPhoneNumber.getText();
        String role = inputRole.getText();

        // Kiểm tra dữ liệu đầu vào
        if (code.isEmpty() || name.isEmpty() || phone.isEmpty() || role.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int id = Integer.parseInt(code);

            Employee employee = new Employee(id, name, "12345", phone,role);

            EmployDAO dao = new EmployDAO();
            dao.insertEmployee(employee);


            System.out.println("Thêm nhân viên: " + id + ", " + name + ", "  + ", " + phone + ", " + role);
            showAlert("Thành công", "Đã thêm nhân viên thành công!", Alert.AlertType.INFORMATION);
            clearInputs();
        } catch (NumberFormatException ex) {
            showAlert("Lỗi", "Giá không hợp lệ.", Alert.AlertType.ERROR);
        }
    }

    private void clearInputs() {
        inputCode.clear();
        inputName.clear();
        inputPhoneNumber.clear();
        inputRole.clear();

    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

//    private void loadMembershipPackages() {
//        EmployDAO dao = new EmployDAO();
//        System.out.println("Đang tải dữ liệu từ database...");
//        List<Employee> packages = dao.getAllPackages();
//        System.out.println("Số lượng gói lấy được: " + packages.size());
//        for (Employee p : packages) {
//            System.out.println(p.getId() + " - " + p.getName());
//        }
//
//        if (packages != null && !packages.isEmpty()) {
//            ObservableList<Employee> data = FXCollections.observableArrayList(packages);
//            listPackage.setItems(data);
//        } else {
//            System.out.println("Không có dữ liệu gói hội viên.");
//        }
//    }
//

}
