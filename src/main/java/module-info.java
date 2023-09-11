module slidebuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;
    requires java.desktop;
    requires com.fasterxml.jackson.core;
    requires DDSUtils;

    opens slidebuilder to javafx.fxml;
    opens slidebuilder.controllers to javafx.fxml;
    opens slidebuilder.controllers.interfaces to javafx.fxml;
    opens slidebuilder.data to javafx.base;
    exports slidebuilder;
}