package pl.niki.recipebookapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.Manager;
import pl.niki.recipebookapp.recipes.Product;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    // =================================================================================================================
    // Public fields - elements of the addProduct-view.fxml
    // =================================================================================================================
    public TextField productNameField, kcalField;
    public Button addButton, cancelButton;

    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private final DataManager dm;
    private final Manager mm;
    private final Product newProduct;

    private boolean added;

    // =================================================================================================================
    // Constructors
    // =================================================================================================================
    public AddProductController(DataManager dm, Manager mm, Product newProduct) {
        this.dm = dm;
        this.mm = mm;
        this.newProduct = newProduct;
        this.added = false;
    }

    // =================================================================================================================
    // Override methods
    // =================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Stage stage = (Stage) addButton.getScene().getWindow();
//        stage.setTitle("Add New Product");
//        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pl/niki/recipebookapp/images/recipes.png"))));

        // TextField ===================================================================================================
        if (newProduct.getName().length() > 0) {
            productNameField.setText(newProduct.getName());
        }

        // Buttons =====================================================================================================
        // add button
        addButton.setText("Add");
        addButton.setOnAction(this::addAction);

        // cancel button
        cancelButton.setOnAction(this::cancelAction);
    }

    // =================================================================================================================
    // Public methods
    // =================================================================================================================
    public Manager getMm() {
        return mm;
    }

    public DataManager getDm() {
        return dm;
    }

    public boolean isAdded() {
        return added;
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================
    private void cancelAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    } // Cancel Button Action

    private void addAction(ActionEvent event) {
        if (productNameField.getText().length() > 0) {
            if (kcalField.getText().length() > 0) {                                                                        //??????????? check if double value???????????????????????????
                newProduct.setName(productNameField.getText());
                newProduct.setKcal(Double.parseDouble(kcalField.getText()));
                newProduct.setNewNull(false);
                this.added = dm.addProduct(newProduct);
                cancelAction(event);
            }
        }
    } // Add Button Action
}
