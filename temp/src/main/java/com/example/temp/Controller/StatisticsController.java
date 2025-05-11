package com.example.temp.Controller;

import com.example.temp.DAO.PackageSalesDAO;
import com.example.temp.Models.PackageSalesStats;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StatisticsController {
    @FXML
    private TableView<PackageSalesStats> salesTable;
    @FXML private TableColumn<PackageSalesStats, String> colName;
    @FXML private TableColumn<PackageSalesStats, Integer> colSales;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));

        salesTable.setItems(FXCollections.observableArrayList(PackageSalesDAO.getAllStats()));
    }


}
