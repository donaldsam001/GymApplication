package com.example.temp.Controller;

import com.example.temp.DAO.AdminDAO;
import com.example.temp.DAO.EmployDAO;
import com.example.temp.Models.Admin;
import com.example.temp.Models.Employee;
import com.example.temp.Models.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class LoginController {

    @FXML
    private Button cancelButton;
    @FXML
    private CheckBox adminButton;


    @FXML
    private CheckBox empButton;
    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    void handleLogin(ActionEvent event) {
        try {
            int id = Integer.parseInt(username.getText().trim()); // dùng id
            String pass = password.getText().trim();

            // Check Admin trước
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getAdminInf(id);

            if (admin != null && admin.getPassword().equals(pass)) {
                Session.isAdmin = true;
                Session.userId = admin.getId();
                Session.userName = admin.getName();
                loadHome(event);
                return;
            }

            // Nếu không phải admin thì check Employee
            EmployDAO employDAO = new EmployDAO();
            Employee emp = employDAO.getEmployeeById(id);

            if (emp != null && emp.getPassword().equals(pass)) {
                Session.isAdmin = false;
                Session.userId = emp.getId();
                Session.userName = emp.getName();
                loadHome(event);
                return;
            }
            showAlert("Đăng nhập thất bại", "ID hoặc mật khẩu không đúng.");
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "ID phải là số nguyên.");
        }
    }


    private void loadHome(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/temp/View/home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Main");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải giao diện chính.");
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        // Đóng cửa sổ
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
