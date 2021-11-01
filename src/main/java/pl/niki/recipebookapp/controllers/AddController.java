package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    public Button backButton, addRecipeButton, homeButton, recipesButton, addButton, addIngredientButton, addInstructionButton;
    public ImageView recipeImage;
    public ListView<Ingredient> ingredientsList;
    public TableView<Instruction> instructionTable;
    public TableColumn<Instruction,String> descriptionColumn;
    public TableColumn<Instruction, Image> imageColumn;
    public TextField recipeNameField, timeField, servingsField;
    public TextArea descriptionArea;
    public Label kcalLabel;

    private DataManager dm;
    private MathManager mm;
    private ObservableList<Ingredient> ingredients;
    private ObservableList<Instruction> instructions;
    private Image newRecipeImage;

    public AddController(DataManager dm, MathManager mm) {
        this.dm = dm;
        this.mm = mm;
        this.newRecipeImage = null;
        mm.newRecipe();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        recipeImage.setImage(mm.getAddImage());
        recipeImage.setOnMouseClicked(this::imageAction);

        // ListView ====================================================================================================
        ingredients = FXCollections.observableArrayList(mm.getNewRecipe().getIngredients());
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
        instructions = FXCollections.observableArrayList(mm.getNewRecipe().getInstructions());
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
        backButton.setOnAction(this::backAction);

        //home button
        if(mm.getHomeIcon()!=null){
            homeButton.setGraphic(mm.getHomeIcon());
        }
        homeButton.setOnAction(this::homeAction);

        //recipes button
        if (mm.getRecipesIcon()!=null){
            recipesButton.setGraphic(mm.getRecipesIcon());
        }
        recipesButton.setOnAction(this::recipesAction);

        // add recipe button
        if (mm.getDoneIcon()!=null){
            addRecipeButton.setGraphic(mm.getDoneIcon());
        }
        else {
            addRecipeButton.setText("Add Recipe");
        }
        addRecipeButton.setOnAction(this::addRecipeAction);

        // add button
        // add ingredient button
        // add instruction button
        if (mm.getAddIcon()!=null){
            addButton.setGraphic(mm.getAddIcon());
            addIngredientButton.setGraphic(mm.getAddIcon());
            addInstructionButton.setGraphic(mm.getAddIcon());
        }
        addIngredientButton.setOnAction(this::addIngredientAction);
        addInstructionButton.setOnAction(this::addInstructionAction);
    }

    private void addRecipeAction(ActionEvent event) {
        if (recipeNameField.getText().length() > 2){
            if(timeField.getText().length() > 0){
                if (servingsField.getText().length() > 0){
                    if (descriptionArea.getText().length() > 2){
                        if(mm.getNewRecipe().getIngredients().size() > 0){
                            if (mm.getNewRecipe().getInstructions().size() > 0){
                                if (this.newRecipeImage != null){
                                    mm.setNewRecipe(recipeNameField.getText(),timeField.getText(), Integer.parseInt(servingsField.getText()),descriptionArea.getText(), this.newRecipeImage);
                                    dm.addRecipe(mm.getNewRecipe());
                                    RecipeController controller = new RecipeController(dm, mm, dm.getRecipes().size()-1);
                                    mm.show(getClass(),"recipe-view.fxml",controller,event);
//                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                    alert.setHeaderText("aaa");
//                                    alert.showAndWait();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addInstructionAction(ActionEvent event) {
        AddInstructionController controller = new AddInstructionController(dm, mm);
        mm.showAndWait(getClass(), "addInstruction-view.fxml", controller, addInstructionButton);

        if (controller.isAdded()) {
            this.mm = controller.getMm();
            instructions = FXCollections.observableArrayList(mm.getNewRecipe().getInstructions());
            instructionTable.setItems(instructions);

            if (mm.getNewRecipe().isInstructionImage()) {

                imageColumn.setPrefWidth(200);
                imageColumn.setMaxWidth(200);
                imageColumn.setMinWidth(200);
                imageColumn.setCellFactory(param -> {
                    // set ImageView
                    final ImageView descriptionImage = new ImageView();

                    // set cell
                    TableCell<Instruction, Image> cell = new TableCell<>() {
                        @Override
                        protected void updateItem(Image image, boolean b) {
//                    super.updateItem(image, b);
                            if (image != null) {
                                descriptionImage.setFitHeight(150);
                                descriptionImage.setFitWidth(200);
                                descriptionImage.setImage(image);
                            } else {
                            }
                        }
                    };
                    cell.setGraphic(descriptionImage);
                    return cell;
                });
                imageColumn.setCellValueFactory(new PropertyValueFactory<Instruction, Image>("image"));
                instructionTable.setFixedCellSize(150);
                instructionTable.prefHeightProperty().bind(instructionTable.fixedCellSizeProperty().multiply(Bindings.size(instructionTable.getItems()).add(0.3)));
            } else {
                imageColumn.setPrefWidth(0);
                imageColumn.setMaxWidth(0);
                imageColumn.setMinWidth(0);
            }
        }
    }

    private void addIngredientAction(ActionEvent event) {
        AddIngredientController controller = new AddIngredientController(dm, mm);
        mm.showAndWait(getClass(), "addIngredient-view.fxml", controller, addIngredientButton);

        if (controller.isAdded()) {
            this.mm = controller.getMm();
            ingredients = FXCollections.observableArrayList(mm.getNewRecipe().getIngredients());
            ingredientsList.setItems(ingredients);
            ingredientsList.setFixedCellSize(30);
            ingredientsList.prefHeightProperty().bind(ingredientsList.fixedCellSizeProperty().multiply(Bindings.size(ingredientsList.getItems()).add(0.3)));
            kcalLabel.setText(String.valueOf(mm.round_double(mm.getNewRecipe().getKcal())));
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
                    this.newRecipeImage = new Image(file.toURI().toString());
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
                    recipeImage.setImage(newRecipeImage);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // menu's buttons action
    private void recipesAction(ActionEvent event) {
        RecipesController controller = new RecipesController(dm, mm);
        mm.show(getClass(),"recipes-view.fxml",controller, event);
    }

    private void homeAction(ActionEvent event) {
        recipesAction(event);
    }

    private void backAction(ActionEvent event) {
        recipesAction(event);
    }
}
