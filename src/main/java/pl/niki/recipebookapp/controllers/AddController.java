package pl.niki.recipebookapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Instruction;
import pl.niki.recipebookapp.recipes.Recipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.spi.FileTypeDetector;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    public Button backButton, homeButton, recipesButton, addButton, addIngredientButton, addInstructionButton;
    public ImageView recipeImage;
    public ListView<Ingredient> ingredientsList;
    public TableView<Instruction> instructionTable;
    public TableColumn<Instruction,String> descriptionColumn;
    public TableColumn<Instruction, Image> imageColumn;
    public Label kcalLabel;

    private DataManager dm;
    private MathManager mm;
    private ObservableList<Ingredient> ingredients;
    private ObservableList<Instruction> instructions;

    public AddController(DataManager dm, MathManager mm) {
        this.dm = dm;
        this.mm = mm;
        mm.newRecipe();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        recipeImage.setImage(mm.getAddImage());
        recipeImage.setOnMouseClicked(this::imageAction);

        // ListView ====================================================================================================
        ingredients = FXCollections.observableArrayList(mm.getIngredients());
        ingredientsList.setItems(ingredients);
        ingredientsList.setCellFactory(i -> new ListCell<>(){
            @Override
            protected void updateItem(Ingredient ingredient, boolean b) {
                super.updateItem(ingredient, b);
                if (b || ingredient == null || ingredient.toString()==null){
                    setText(null);
                }
                else {
                    setText(ingredient.toString());
                }
            }
        });

        // TableView ===================================================================================================
        instructions = FXCollections.observableArrayList(mm.getInstructions());
        instructionTable.setItems(instructions);
        descriptionColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        descriptionColumn.setCellFactory(param -> {
            TableCell<Instruction,String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descriptionColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        // Button ======================================================================================================
        //back button
        if(mm.getBackIcon()!=null) {
            backButton.setGraphic(mm.getBackIcon());
        }
        else
            backButton.setText("Back");
//        backButton.setOnAction(this::backAction);

        //home button
        if(mm.getHomeIcon()!=null){
            homeButton.setGraphic(mm.getHomeIcon());
        }
//        homeButton.setOnAction(this::backAction);

        //recipe button
        if (mm.getRecipesIcon()!=null){
            recipesButton.setGraphic(mm.getRecipesIcon());
        }
//        recipesButton.setOnAction(this::backAction);

        //add button
        // add ingredient button
        // add instruction button
        if (mm.getAddIcon()!=null){
            addButton.setGraphic(mm.getAddIcon());
            addIngredientButton.setGraphic(mm.getAddIcon());
            addInstructionButton.setGraphic(mm.getAddIcon());
        }
//        addButton.setOnAction(this::backAction);
        addIngredientButton.setOnAction(this::addIngredientAction);
        addInstructionButton.setOnAction(this::addInstructionAction);
    }

    private void addInstructionAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/addInstruction-view.fxml"));
            AddInstructionController controller = new AddInstructionController(dm,mm);
            loader.setController(controller);
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addInstructionButton.getScene().getWindow());
            stage.showAndWait();

            if (controller.isAdded()){
                this.mm = controller.getMm();
                instructions = FXCollections.observableArrayList(mm.getInstructions());
                instructionTable.setItems(instructions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addIngredientAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/addIngredient-view.fxml"));
            AddIngredientController controller = new AddIngredientController(dm, mm);
            loader.setController(controller);
            Parent parent = loader.load();
            Stage stage  = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addIngredientButton.getScene().getWindow());
            stage.showAndWait();

            if (controller.isAdded()){
                this.mm = controller.getMm();
                ingredients = FXCollections.observableArrayList(mm.getIngredients());
                ingredientsList.setItems(ingredients);
                kcalLabel.setText(String.valueOf(mm.countKcal()));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void imageAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(recipeImage.getScene().getWindow());
        try {
            if (file!=null) {
                String type = Files.probeContentType(file.toPath());
                type = type.split("/")[0];
                if (type.equals("image")){
                    Image image = new Image(file.toURI().toString());
//                    FXMLLoader loader = new FXMLLoader();
//                    loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/imageCropper-view.fxml"));
//                    ImageCropperController controller = new ImageCropperController(image);
//                    loader.setController(controller);
//                    Parent parent = loader.load();
//                    Stage stage = new Stage();
//                    stage.setTitle("Cropper");
//                    stage.setScene(new Scene(parent));
//                    stage.initModality(Modality.WINDOW_MODAL);
//                    stage.initOwner(recipeImage.getScene().getWindow());
//                    stage.show();
                    recipeImage.setImage(image);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
