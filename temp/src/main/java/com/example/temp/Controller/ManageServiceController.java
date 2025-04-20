package com.example.temp.Controller;

import com.example.temp.DAO.MembershipPackageDAO;
import com.example.temp.Models.MembershipPackage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;




public class ManageServiceController {

    @FXML
    private Button btnDeleteAll;

    @FXML
    private TableColumn<MembershipPackage, String> colChange;

    @FXML
    private TableColumn<MembershipPackage, String> colDel;


    @FXML
    private TableColumn<MembershipPackage, String> colDescriptionChange;

    @FXML
    private TableColumn<MembershipPackage, String> colDescriptionDel;


    @FXML
    private TableColumn<MembershipPackage, String> colExpDateChange;

    @FXML
    private TableColumn<MembershipPackage, String> colExpDateDel;


    @FXML
    private TableColumn<MembershipPackage, String> colIdChange;

    @FXML
    private TableColumn<MembershipPackage, String> colIdDel;


    @FXML
    private TableColumn<MembershipPackage, String> colNameChange;

    @FXML
    private TableColumn<MembershipPackage, String> colNameDel;


    @FXML
    private TableColumn<MembershipPackage, String> colPriceChange;

    @FXML
    private TableColumn<MembershipPackage, String> colPriceDel;

    @FXML
    private TableColumn<MembershipPackage, String> colStatus;

    @FXML
    private TableColumn<MembershipPackage, String> colStatusChange;

    @FXML
    private TableColumn<MembershipPackage, String> colStatusDel;

    @FXML
    private TextField inputSearchDel;

    @FXML
    private TableView<MembershipPackage> listPackageChange;

    @FXML
    private TableView<MembershipPackage> listPackageDel;



    @FXML
    private Tab tabViewPackagesDel;

    @FXML
    private Tab tabViewPackagesChange;


//    ---------------------------------

    @FXML
    private TableColumn<MembershipPackage, Integer> colId;

    @FXML
    private TableColumn<MembershipPackage, String> colName;

    @FXML
    private TableColumn<MembershipPackage, String> colDescription;

    @FXML
    private TableColumn<MembershipPackage, Integer> colExpDate;

    @FXML
    private TableColumn<MembershipPackage, Float> colPrice;


    @FXML
    private Button handelCreate;

    @FXML
    private TextField inputCode;

    @FXML
    private TextField inputDescription;

    @FXML
    private TextField inputExpDate;

    @FXML
    private TextField inputName;

    @FXML
    private TextField inputPrice;

    @FXML
    private TextField inputSearch;

    @FXML
    private TableView<MembershipPackage> listPackage;


    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabViewPackages;

    @FXML
    public void initialize() {
        // setup cell value factory
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory< >("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<> ("exp"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colIdDel.setCellValueFactory(new PropertyValueFactory<MembershipPackage, String>("id"));
        colNameDel.setCellValueFactory(new PropertyValueFactory< MembershipPackage, String >("name"));
        colDescriptionDel.setCellValueFactory(new PropertyValueFactory<MembershipPackage, String>("description"));
        colExpDateDel.setCellValueFactory(new PropertyValueFactory<MembershipPackage, String> ("exp"));
        colPriceDel.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatusDel.setCellValueFactory(new PropertyValueFactory<>("status"));

        colIdChange.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameChange.setCellValueFactory(new PropertyValueFactory< >("name"));
        colDescriptionChange.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpDateChange.setCellValueFactory(new PropertyValueFactory<> ("exp"));
        colPriceChange.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatusChange.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load dữ liệu
        loadDeleteAndChangeTables();
        loadMembershipPackages();

        inputSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPackages(); // Gọi mỗi khi người dùng nhập hoặc xoá ký tự
        });


        handelCreate.setOnAction(e -> {
            createMembershipPackage();
            loadMembershipPackages(); // reload lại sau khi thêm
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == tabViewPackages ||  newTab == tabViewPackagesDel) {
                loadDeleteAndChangeTables();
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

    private void loadDeleteAndChangeTables() {
        MembershipPackageDAO dao = new MembershipPackageDAO();
        List<MembershipPackage> packages = dao.getData();
        ObservableList<MembershipPackage> data = FXCollections.observableArrayList(packages);

        listPackageDel.setItems(data);
        listPackageChange.setItems(data);
    }





    private void createMembershipPackage() {
        String code = inputCode.getText();
        String name = inputName.getText();
        String description = inputDescription.getText();
        String expDate = inputExpDate.getText();
        String price = inputPrice.getText();

        // Kiểm tra dữ liệu đầu vào
        if (code.isEmpty() || name.isEmpty() || description.isEmpty() || expDate.isEmpty() || price.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int id = Integer.parseInt(code);
            float priceValue = Float.parseFloat(price);
            int exp = Integer.parseInt(expDate);
            boolean status = true; // hoặc kiểm tra từ UI nếu có lựa chọn trạng thái
            MembershipPackage membershipPackage = new MembershipPackage(id, name, priceValue, description, exp, status);
            MembershipPackageDAO dao = new MembershipPackageDAO();
            dao.insertMembershipPackage(membershipPackage);


            System.out.println("Tạo gói hội viên: " + id + ", " + name + ", " + description + ", " + expDate + ", " + priceValue);
            showAlert("Thành công", "Đã tạo gói hội viên thành công!", Alert.AlertType.INFORMATION);
            clearInputs();
        } catch (NumberFormatException ex) {
            showAlert("Lỗi", "Giá không hợp lệ.", Alert.AlertType.ERROR);
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



    private void loadMembershipPackages() {
        MembershipPackageDAO dao = new MembershipPackageDAO();
        System.out.println("Đang tải dữ liệu từ database...");
        List<MembershipPackage> packages = dao.getData();

        if (packages != null && !packages.isEmpty()) {
            ObservableList<MembershipPackage> data = FXCollections.observableArrayList(packages);
            listPackage.setItems(data);
            System.out.println("Số lượng gói lấy được: " + packages.size());
            for (MembershipPackage p : packages) {
                System.out.println(p.getId() + " - " + p.getName());
            }
        } else {
            System.out.println("Không có dữ liệu gói hội viên.");
        }
    }



    public void DeleteAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa tất cả gói hội viên?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                MembershipPackageDAO dao = new MembershipPackageDAO();
                dao.deleteAllPackages();
                showAlert("Thành công", "Đã xóa toàn bộ gói hội viên.", Alert.AlertType.INFORMATION);
                loadMembershipPackages();
                loadDeleteAndChangeTables(); // cập nhật lại các bảng
            }
        });
    }

    private void searchPackages() {
        String keyword = inputSearch.getText().trim();

        MembershipPackageDAO dao = new MembershipPackageDAO();
        List<MembershipPackage> result = dao.searchPackages(keyword); // Gọi DAO để lấy danh sách phù hợp

        ObservableList<MembershipPackage> data = FXCollections.observableArrayList(result);
        listPackage.setItems(data); // Hiển thị kết quả tìm kiếm ở bảng chính
    }


}
