package pl.niki.recipebookapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class AddIngredientController implements Initializable {
    public ComboBox<String> amountComboBox;
    public ComboBox<Product> ingredientComboBox;
    public TextField amountField, ingredientField;
    public Button okButton, addButton, doneButton, cancelButton;
    public Label amountLabel, in100Label;

    private DataManager dm;
    private MathManager mm;
    private ObservableList<Product> products;
    private ObservableList<String> amounts;
    private boolean added;

    public AddIngredientController(DataManager dm, MathManager mm) {
        this.dm = dm;
        this.mm = mm;
        this.added = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // combo box
        products = FXCollections.observableArrayList(mm.getNotAddedProducts(dm.getProducts()));
        ingredientComboBox.setItems(products);
        ingredientComboBox.setCellFactory(new Callback<ListView<Product>, ListCell<Product>>() {
            @Override
            public ListCell<Product> call(ListView<Product> ingredientListView) {
                ListCell<Product> cell = new ListCell<>(){
                    @Override
                    protected void updateItem(Product product, boolean b) {
                        super.updateItem(product, b);
                        if (b || product == null || product.getName() == null){
                            setText(null);
                        }
                        else {
                            setText(product.getName());
                        }
                    }
                };
                return cell;
            }
        });

        amounts = FXCollections.observableArrayList();
        amounts.add("g");
        amountComboBox.setItems(amounts);

        // add button
        if(mm.getAddIcon()!=null){
            addButton.setGraphic(mm.getAddIcon());
        }
        else addButton.setText("Add");
        addButton.setOnAction(this::addAction);

        // added button
        if(mm.getDoneIcon()!=null){
            doneButton.setGraphic(mm.getDoneIcon());
        }
        else doneButton.setText("Done");
        doneButton.setOnAction(this::doneAction);

        // ok button
        okButton.setOnAction(this::okAction);

        // cancel button
        cancelButton.setOnAction(this::cancelAction);

    }

    public boolean isAdded() {
        return added;
    }

    public MathManager getMm() {
        return mm;
    }

    private void cancelAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void okAction(ActionEvent event) {
        Product product = ingredientComboBox.getSelectionModel().getSelectedItem();
        if (product!=null){
            if (amountField.getText().length()>0){
                mm.getNewRecipe().addIngredient(product, Integer.parseInt(amountField.getText()));
                this.added = true;
                cancelAction(event);
            }
        }
    }

    private void doneAction(ActionEvent event) {
        ingredientField.setVisible(false);
        doneButton.setVisible(false);
        ingredientComboBox.setVisible(true);
        addButton.setVisible(true);
        amountComboBox.setVisible(true);
        in100Label.setVisible(false);
        amountLabel.setText("Amount:");
        if (ingredientField.getText().length() > 2){
            products.add(dm.addProduct(ingredientField.getText(), Double.parseDouble(amountField.getText())));
            ingredientComboBox.getSelectionModel().select(products.size()-1);
            amountField.setText("");
        }
    }

    private void addAction(ActionEvent event) {
        ingredientField.setVisible(true);
        doneButton.setVisible(true);
        ingredientComboBox.setVisible(false);
        addButton.setVisible(false);
        amountComboBox.setVisible(false);
        in100Label.setVisible(true);
        amountLabel.setText("Kcal:");
    }
}
