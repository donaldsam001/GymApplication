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
    private CheckBox checkIsAdmin;
    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private TextField tfID;

    @FXML
    void handleLogin(ActionEvent event) {
        String idText = tfID.getText().trim();
        String passwordText = password.getText().trim();
        boolean isAdmin = checkIsAdmin.isSelected();

        try {
            int id = Integer.parseInt(tfID.getText().trim()); // dùng id
            String pass = password.getText().trim();

            if (idText.isEmpty() || passwordText.isEmpty()) {
                showAlert("Lỗi", "ID và mật khẩu không được để trống.");
                return;
            }

            if (isAdmin){
                // Check Admin trước
                AdminDAO adminDAO = new AdminDAO();
                Admin admin = adminDAO.getAdminInf(id);
                if (admin != null && admin.getPassword().equals(pass)) {
                    loadHome(event, true, admin.getId(), admin.getName());
                    return;
                }

//                if (admin != null && admin.getPassword().equals(pass)) {
//                    Session.isAdmin = true;
//                    Session.userId = admin.getId();
//                    Session.userName = admin.getName();
//                    loadHome(event);
//                    return;
//                }
            }
            else{
                // Nếu không phải admin thì check Employee
                EmployDAO employDAO = new EmployDAO();
                Employee emp = employDAO.getEmployeeById(id);
                if (emp != null && emp.getPassword().equals(pass) && emp.isReceptionist()) {
                    loadHome(event, false, emp.getId(), emp.getName());
                    return;
                }

//                if (emp != null && emp.getPassword().equals(pass) && emp.isReceptionist() ) {
//                    Session.isAdmin = false;
//                    Session.userId = emp.getId();
//                    Session.userName = emp.getName();
//                    loadHome(event);
//                    return;
//                }

            }
            showAlert("Đăng nhập thất bại", "ID hoặc mật khẩu không đúng.");
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "ID phải là số nguyên.");
        }
    }


    private void loadHome(ActionEvent event, boolean isAdmin, int userId, String userName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/temp/View/home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Lấy controller và truyền isAdmin
            HomeController homeController = fxmlLoader.getController();
//            homeController.setIsAdmin(checkIsAdmin.isSelected());
            homeController.setLoginInfo(isAdmin, userId, userName);

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
