package pl.niki.recipebookapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class AddIngredientController implements Initializable {
    public TextField productNameField, kcalField;
    public Button addButton, cancelButton;

    private final DataManager dm;
    private final MathManager mm;
    private Product newProduct;
    private boolean added;

    public AddIngredientController(DataManager dm, MathManager mm, Product newProduct) {
        this.dm = dm;
        this.mm = mm;
        this.newProduct = newProduct;
        this.added = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (newProduct.getName().length()>0){
            productNameField.setText(newProduct.getName());
        }

        // add button
        addButton.setText("Add");
        addButton.setOnAction(this::addAction);

        // cancel button
        cancelButton.setOnAction(this::cancelAction);

    }

    public MathManager getMm() {
        return mm;
    }

    public DataManager getDm() {
        return dm;
    }

    public boolean isAdded() {
        return added;
    }

    private void cancelAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void addAction(ActionEvent event) {
        if (productNameField.getText().length()>0){
            if (kcalField.getText().length()>0){//??????????? check if double value???????????????????????????
                newProduct.setName(productNameField.getText());
                newProduct.setKcal(Double.parseDouble(kcalField.getText()));
                newProduct.setNewNull(false);
                this.added = dm.addProduct(newProduct);
                cancelAction(event);
            }
        }
    }
}
