module com.example.temp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.temp to javafx.fxml;
    exports com.example.temp;
    exports com.example.temp.DAO;
    opens com.example.temp.DAO to javafx.fxml;
    exports com.example.temp.Controller;
    opens com.example.temp.Controller to javafx.fxml;
    exports com.example.temp.Utils;
    opens com.example.temp.Utils to javafx.fxml;
    exports com.example.temp.Models;
    opens com.example.temp.Models to javafx.fxml;
}