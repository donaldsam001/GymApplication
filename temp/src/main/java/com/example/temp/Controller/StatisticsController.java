package com.example.temp.Controller;

import com.example.temp.DAO.PackageSalesStatsDAO;
import com.example.temp.Models.PackageSalesStats;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Year;
import java.util.ArrayList;
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
    @FXML private ComboBox<Integer> comboYear, comboTime;

    @FXML private Label totalRevenue;
    @FXML private TextField inputSearch;

    private ObservableList<PackageSalesStats> saleList = FXCollections.observableArrayList();

    private final PackageSalesStatsDAO dao = new PackageSalesStatsDAO();

    @FXML
    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        colRevenue.setCellValueFactory(cellData -> {
            int revenue = cellData.getValue().getRevenue();
            return new SimpleStringProperty(String.format("%,d₫", revenue));
        });

        comboStatType.setItems(FXCollections.observableArrayList("Theo tháng", "Theo quý", "Theo năm", "Tổng quát"));
        comboStatType.setValue("Tổng quát");

        comboStatType.setOnAction(event -> {
            updateComboTime();
            loadStatistics();
        });

        int currentYear = Year.now().getValue();
        for (int y = currentYear - 10; y <= currentYear; y++) {
            comboYear.getItems().add(y);
        }
        comboYear.setValue(currentYear);

        comboYear.setOnAction(e -> loadStatistics());
        comboTime.setOnAction(e -> loadStatistics());

        inputSearch.textProperty().addListener((obs, oldVal, newVal) -> searchPackages());

        updateComboTime();
        loadStatistics();
    }

    private void loadStatistics() {
        String type = comboStatType.getValue();
        int year = comboYear.getValue();
        Integer time = comboTime.getValue();

        if (year < 0) return;
        if ((type.equals("Theo tháng") || type.equals("Theo quý")) && time == null) return;

        PackageSalesStatsDAO dao = new PackageSalesStatsDAO();
        ObservableList<PackageSalesStats> statsList ;

        if (type.equals("Tổng quát")) {
            statsList = FXCollections.observableArrayList(dao.getStatsSummary());
        } else {
            String periodType = switch (type) {
                case "Theo tháng" -> "month";
                case "Theo quý" -> "quarter";
                case "Theo năm" -> "year";
                default -> throw new IllegalArgumentException("Loại thống kê không hợp lệ");
            };

            List<PackageSalesStats> sales = dao.getSalesByPeriod(periodType, year, type.equals("Theo năm") ? 0 : time);
            statsList = FXCollections.observableArrayList(aggregateSales(sales));
        }

        statsTable.setItems(statsList);

        saleList.setAll(statsList); // Cập nhật source gốc cho tìm kiếm
        searchPackages(); // Áp dụng tìm kiếm nếu đã có từ khóa

        int total = statsList.stream().mapToInt(PackageSalesStats::getRevenue).sum();
        totalRevenue.setText("Tổng doanh thu: " + String.format("%,d₫", total));
    }

    private List<PackageSalesStats> aggregateSales(List<PackageSalesStats> sales) {
        Map<Integer, PackageSalesStats> statsMap = new HashMap<>();

        for (PackageSalesStats sale : sales) {
            int packageId = sale.getPackageId();
            String packageName = sale.getPackageName();

            PackageSalesStats stat = statsMap.getOrDefault(packageId, new PackageSalesStats(packageId, packageName, 0, 0));

            stat.setTotalSales(stat.getTotalSales() + 1);
            stat.setRevenue(stat.getRevenue() + sale.getTotalPrice());

            statsMap.put(packageId, stat);
        }

        return new ArrayList<>(statsMap.values());
    }

    private void searchPackages() {
        String keyword = inputSearch.getText().trim().toLowerCase();
        String type = comboStatType.getValue();
        int year = comboYear.getValue();
        Integer time = comboTime.getValue();

        ObservableList<PackageSalesStats> sourceList;

        if (type.equals("Tổng quát")) {
            sourceList = FXCollections.observableArrayList(dao.getStatsSummary());
        } else {
            String periodType = switch (type) {
                case "Theo tháng" -> "month";
                case "Theo quý" -> "quarter";
                case "Theo năm" -> "year";
                default -> throw new IllegalArgumentException("Loại thống kê không hợp lệ");
            };

            List<PackageSalesStats> rawSales = dao.getSalesByPeriod(periodType, year, type.equals("Theo năm") ? 0 : time);
            sourceList = FXCollections.observableArrayList(aggregateSales(rawSales));
        }

        List<PackageSalesStats> filtered = sourceList.stream()
                .filter(stat ->
                        String.valueOf(stat.getPackageId()).contains(keyword) ||
                                stat.getPackageName().toLowerCase().contains(keyword)
                )
                .toList();

        saleList.setAll(filtered);
        statsTable.setItems(saleList);

        int total = saleList.stream().mapToInt(PackageSalesStats::getRevenue).sum();
        totalRevenue.setText("Tổng doanh thu: " + String.format("%,d₫", total));
    }


    private void updateComboTime() {
        String selectedType = comboStatType.getValue();
        comboTime.getItems().clear();

        switch (selectedType) {
            case "Theo tháng" -> {
                comboTime.setDisable(false);
                comboTime.setVisible(true);
                for (int i = 1; i <= 12; i++) comboTime.getItems().add(i);
                comboTime.getSelectionModel().selectFirst();
                comboYear.setDisable(false);
            }
            case "Theo quý" -> {
                comboTime.setDisable(false);
                comboTime.setVisible(true);
                for (int i = 1; i <= 4; i++) comboTime.getItems().add(i);
                comboTime.getSelectionModel().selectFirst();
                comboYear.setDisable(false);
            }
            case "Theo năm" -> {
                comboTime.setDisable(true);
                comboTime.setVisible(false);
                comboYear.setDisable(false);
            }
            case "Tổng quát" -> {
                comboTime.setDisable(true);
                comboTime.setVisible(false);
                comboYear.setDisable(true);
            }
        }
    }
}
