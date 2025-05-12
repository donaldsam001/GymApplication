package com.example.temp.Controller;

import com.example.temp.DAO.MembershipPackageDAO;
import com.example.temp.DAO.PackageSalesDAO;
import com.example.temp.Models.MembershipPackage;
import com.example.temp.Models.PackageSalesStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StatisticsController {
    @FXML
    private TableView<PackageSalesStats> salesTable;

    @FXML private TableColumn<PackageSalesStats, Integer> colID;
    @FXML private TableColumn<PackageSalesStats, String> colName;
    @FXML private TableColumn<PackageSalesStats, Integer> colSales;
    @FXML private TableColumn<PackageSalesStats, String> colRevenue;

    @FXML private Label totalRevenue;
    @FXML private TextField inputSearch;

    private ObservableList<PackageSalesStats> saleList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("packageID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        colRevenue.setCellValueFactory(cellData -> {
            int vnd = cellData.getValue().getRevenue();
            return new javafx.beans.property.SimpleStringProperty(String.format("%,d₫", vnd));
        });

        PackageSalesDAO dao = new PackageSalesDAO();
        var stats = dao.getAllStats();
        salesTable.setItems(FXCollections.observableArrayList(stats));

        int total = stats.stream().mapToInt(PackageSalesStats::getRevenue).sum();
        totalRevenue.setText("Tổng doanh thu: " + String.format("%,d₫", total));
        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchPackages());

    }

    private void searchPackages() {
        PackageSalesDAO dao = new PackageSalesDAO();
        String keyword = inputSearch.getText().trim();
        saleList.setAll(dao.searchPackagesByID(keyword));
        salesTable.setItems(saleList);
    }
}
