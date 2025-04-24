package com.example.temp.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.temp.DAO.EmployDAO;
import com.example.temp.DAO.MembershipPackageDAO;
import com.example.temp.Models.Employee;
import com.example.temp.Models.MembershipPackage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ManagementEmployeeController {

    @FXML
    private TableColumn<Employee, Void> colDel;

    @FXML
    private TableColumn<Employee, Integer> colID, colIDDel, colIDUp;



    @FXML
    private TableColumn<Employee, String> colName, colNameDel, colNameUp;

    @FXML
    private TableColumn<Employee, String> colPhone, colPhoneDel, colPhoneUp;

    @FXML
    private TableColumn<Employee, String> colRole, colRoleDel, colRoleUp;


    @FXML
    private Button handelCreate;

    @FXML
    private TextField inputCode;

    @FXML
    private TextField inputName;

    @FXML
    private TextField inputPhoneNumber;

    @FXML
    private TextField inputRole;

    @FXML
    private TextField inputSearch;

    @FXML
    private TextField inputSearchX;

    @FXML
    private TextField inputSearchX1;

    @FXML
    private TableView<Employee> tableDel, tableUp, tableView;




}
