package com.example.temp.Controller;

import com.example.temp.DAO.PackageSalesDAO;
import com.example.temp.Models.PackageSale;
import com.example.temp.Models.PackageSalesStats;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        colID.setCellValueFactory(new PropertyValueFactory<>("packageId"));
//        colName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        colRevenue.setCellValueFactory(cellData -> {
            int revenue = cellData.getValue().getRevenue();
            return new SimpleStringProperty(String.format("%,d₫", revenue));
        });

        comboStatType.setItems(FXCollections.observableArrayList("Theo tháng", "Theo quý", "Theo năm"));
        comboStatType.setValue("Theo tháng");

        int currentYear = Year.now().getValue();
        for (int y = currentYear - 10; y <= currentYear; y++) {
            comboYear.getItems().add(y);
        }
        comboYear.setValue(currentYear);

        comboStatType.setOnAction(e -> loadStatistics());
        comboYear.setOnAction(e -> loadStatistics());

        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchPackages());

        loadStatistics();
    }

    private void loadStatistics() {
        String type = comboStatType.getValue();
        int year = comboYear.getValue();

        List<PackageSale> sales;
        switch (type) {
            case "Theo tháng" -> sales = dao.getSalesByPeriod("month", year, 1);
            case "Theo quý" -> sales = dao.getSalesByPeriod("quarter", year, 1);
            case "Theo năm" -> sales = dao.getSalesByPeriod("year", year, 0);
            default -> sales = List.of();
        }

        Map<Integer, PackageSalesStats> statsMap = new HashMap<>();
        for (PackageSale sale : sales) {
            int packageId = sale.getPackageId();
            PackageSalesStats stat = statsMap.getOrDefault(packageId, new PackageSalesStats(packageId, 0, 0));
            stat.setTotalSales(stat.getTotalSales() + 1);
            stat.setRevenue(stat.getRevenue() + sale.getTotalPrice());
            statsMap.put(packageId, stat);
        }

        ObservableList<PackageSalesStats> statsList = FXCollections.observableArrayList(statsMap.values());
        statsTable.setItems(statsList);

        int total = statsList.stream().mapToInt(PackageSalesStats::getRevenue).sum();
        totalRevenue.setText("Tổng doanh thu: " + String.format("%,d₫", total));
    }

    private void searchPackages() {
        String keyword = inputSearch.getText().trim();
        List<PackageSalesStats> filtered = dao.getAllStats().stream()
                .filter(stat -> String.valueOf(stat.getPackageId()).contains(keyword))
                .toList();
        saleList.setAll(filtered);
        statsTable.setItems(saleList);
    }
}
