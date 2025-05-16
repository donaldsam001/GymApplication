package com.example.temp.Controller;

import com.example.temp.DAO.PackageSalesDAO;
import com.example.temp.Models.PackageSalesStats;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Year;
import java.util.List;

public class StatisticsController {

    @FXML private TableView<PackageSalesStats> statsTable;
    @FXML private TableColumn<PackageSalesStats, Integer> colID;
    @FXML private TableColumn<PackageSalesStats, String> colName;
    @FXML private TableColumn<PackageSalesStats, Integer> colSales;
    @FXML private TableColumn<PackageSalesStats, String> colRevenue;

    @FXML private ComboBox<String> comboStatType;
    @FXML private ComboBox<Integer> comboYear;

    @FXML private Label totalRevenue;
    @FXML private TextField inputSearch;

    private ObservableList<PackageSalesStats> saleList = FXCollections.observableArrayList();


    private final PackageSalesDAO dao = new PackageSalesDAO();

    @FXML
    public void initialize() {
        // Cột: ID gói hội viên
        colID.setCellValueFactory(new PropertyValueFactory<>("packageID"));

        // Cột: Tên gói hội viên hoặc thời gian (tuỳ loại thống kê)
        colName.setCellValueFactory(new PropertyValueFactory<>("packageName"));

        // Cột: Số lượt bán
        colSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));

        // Cột: Doanh thu, định dạng VND
        colRevenue.setCellValueFactory(cellData -> {
            int revenue = cellData.getValue().getRevenue();
            return new SimpleStringProperty(String.format("%,d₫", revenue));
        });

        // ComboBox loại thống kê
        comboStatType.setItems(FXCollections.observableArrayList("Theo tháng", "Theo quý", "Theo năm"));
        comboStatType.setValue("Theo tháng");

        // ComboBox năm (10 năm gần đây)
        int currentYear = Year.now().getValue();
        for (int y = currentYear - 10; y <= currentYear; y++) {
            comboYear.getItems().add(y);
        }
        comboYear.setValue(currentYear);

        // Load lần đầu
        loadStatistics();

        // Reload khi thay đổi
        comboStatType.setOnAction(e -> loadStatistics());
        comboYear.setOnAction(e -> loadStatistics());

        PackageSalesDAO dao = new PackageSalesDAO();
        var stats = dao.getAllStats();
        statsTable.setItems(FXCollections.observableArrayList(stats));

        int total = stats.stream().mapToInt(PackageSalesStats::getRevenue).sum();
        totalRevenue.setText("Tổng doanh thu: " + String.format("%,d₫", total));
        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchPackages());
    }

    private void loadStatistics() {
        String type = comboStatType.getValue();
        int year = comboYear.getValue();

        List<PackageSalesStats> stats;

        switch (type) {
            case "Theo tháng" -> stats = dao.getRevenueByMonth(year);
            case "Theo quý" -> stats = dao.getRevenueByQuarter(year);
            case "Theo năm" -> stats = dao.getRevenueByYear();
            default -> stats = FXCollections.observableArrayList();
        }

        statsTable.setItems(FXCollections.observableArrayList(stats));
    }

    private void searchPackages() {
        PackageSalesDAO dao = new PackageSalesDAO();
        String keyword = inputSearch.getText().trim();
        saleList.setAll(dao.searchPackagesByID(keyword));
        statsTable.setItems(saleList);
    }
}
