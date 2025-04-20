package com.example.temp.Controller;

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

    @FXML
    private Button managementMembership;

    @FXML
    private Button notification;

    @FXML
    private Button setting;

    @FXML
    private Pane topPane;

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



//    @FXML
//    public void initialize() {
//        setting.setOnAction(event -> openSetting(ActionEvent event));
//    }
    @FXML
    private void openSetting(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/setting-view.fxml"));
            Pane settingPane = loader.load();
            mainContent.getChildren().setAll(settingPane);
        } catch (IOException e) {

        }
    }

    @FXML
    private void openEmpManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/management-employee.fxml"));
            Pane root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Quản lý nhân viên"); // tiêu đề cửa sổ
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở cửa sổ Quản lý nhân viên.");
        }
    }

    @FXML
    private void openMem(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/manage-mem-view.fxml"));
            Pane root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Quản lý nhân viên"); // tiêu đề cửa sổ
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở cửa sổ Quản lý nhân viên.");
        }
    }


    @FXML
    private void openDeviceManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/device-management.fxml"));
            Pane root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Quản lý thiết bị"); // tiêu đề cửa sổ
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở cửa sổ Quản lý thiết bị.");
        }
    }

    @FXML
    private void openManageService(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/temp/View/manage-service.fxml"));
            Pane root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Quản lý thiết bị"); // tiêu đề cửa sổ
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể mở cửa sổ Quản lý gói hội viên.");
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
