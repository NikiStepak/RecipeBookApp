module pl.niki.recipebookapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens pl.niki.recipebookapp to javafx.fxml;
    exports pl.niki.recipebookapp;
    exports pl.niki.recipebookapp.controllers;
    opens pl.niki.recipebookapp.controllers to javafx.fxml;
    exports pl.niki.recipebookapp.recipes;
    opens pl.niki.recipebookapp.recipes to javafx.fxml;
    exports pl.niki.recipebookapp.manager;
    opens pl.niki.recipebookapp.manager to javafx.fxml;
}