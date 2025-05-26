package com.example.temp.Controller;

import com.example.temp.Models.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class HomeController {

    public Button empManagement;
    public Button deviceManagement;
    public VBox vbox1;
    public Button managementMembership1;
    public VBox vbox;
    @FXML
    private Button logout;

    @FXML private Button managementMembership;

    @FXML private Button btnStatistics;

    @FXML private Pane topPane;

    private boolean isAdmin;
    private int userId;
    private String userName;

    public void setLoginInfo(boolean isAdmin, int userId, String userName) {
        this.isAdmin = isAdmin;
        this.userId = userId;
        this.userName = userName;
        adjustUI();
    }

//    public void setIsAdmin(boolean isAdmin) {
//        this.isAdmin = isAdmin;
//        adjustUI();
//    }

    private void adjustUI() {
        if (!isAdmin) {
            vbox1.setVisible(false);
            empManagement.setVisible(false);
            btnStatistics.setVisible(false);
        }
    }


    @FXML
    private void openLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/login-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Lấy stage hiện tại
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open login screen.");
        }
    }

    @FXML
    private Pane mainContent; // Khu vực hiển thị nội dung

    @FXML
    private void openQLHoiVienPage(ActionEvent event){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/membership-view.fxml"));            Pane root = loader.load();
            mainContent.getChildren().setAll(root);        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở cửa sổ Quản lý hội viên");
        }
    }

    @FXML
    private void openTrainingTime(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/trainingtime-view.fxml"));
            Pane root = loader.load();
            mainContent.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();  // In ra chi tiết lỗi để giúp phát hiện nguyên nhân
            showAlert("Error", "Không thể mở cửa sổ Quản lý thời gian tập.\n" + e.getMessage());
        }
    }

    @FXML
    private void openThe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/membershipcard-view.fxml"));
            Pane root = loader.load();
            mainContent.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở cửa sổ Quản lý thẻ.");
        }
    }

    @FXML
    private void openEmpManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/management-employee.fxml"));
            Pane root = loader.load();
            mainContent.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở trang.");
        }
    }

    @FXML
    private void openDeviceManagement(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/device-management.fxml"));
        Pane root = loader.load();
        mainContent.getChildren().setAll(root);
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error", "Không thể mở trang.");
    }
    }

    @FXML
    private void openManageService(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/manage-service.fxml"));
            Pane root = loader.load();
            mainContent.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở trang.");
        }
    }

    @FXML
    private void openStatisticsTable(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/statistics-view.fxml"));
            Pane statistics = loader.load();
            mainContent.getChildren().setAll(statistics);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở trang.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
