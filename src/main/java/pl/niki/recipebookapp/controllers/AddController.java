package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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
    public TextField recipeNameField, minTimeField, servingsField, cuisineField, hTimeField;
    public TextArea descriptionArea;
    public Label kcalLabel;
    public ChoiceBox<String> courseChoiceBox;

    public SplitPane split;
    public ScrollPane scroll;
    public ToolBar tool;

    private final DataManager dm;
    private MathManager mm;
    private ObservableList<Ingredient> ingredients;
    private ObservableList<Instruction> instructions;
    private Image newRecipeImage;
    private final boolean edit;
    private int recipeKey;
    private final double width, height;


    public AddController(DataManager dm, MathManager mm, double width, double height) {
        this.dm = dm;
        this.mm = mm;
        this.newRecipeImage = null;
        this.edit = false;
        mm.newRecipe();
        this.width = width;
        this.height = height;
    }

    public AddController(DataManager dm, MathManager mm, int recipeKey, double width, double height) {
        this.dm = dm;
        this.mm = mm;
        this.edit = true;
        this.recipeKey = recipeKey;
        mm.setNewRecipe(dm.getRecipes().get(recipeKey));
        this.newRecipeImage = mm.getNewRecipe().getImage();
        this.width = width;
        this.height = height;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> course = FXCollections.observableArrayList(dm.getCourses());
        courseChoiceBox.setItems(course);
        if (edit){
            courseChoiceBox.getSelectionModel().select(mm.getNewRecipe().getCourse());
        }

        // ImageView ===================================================================================================
        if (edit){
            recipeImage.setImage(mm.getNewRecipe().getImage());
        }
        else {
            recipeImage.setImage(mm.getAddImage());
        }
        recipeImage.setOnMouseClicked(this::imageAction);

        // Label, Fields and Area ========================================
        if (edit){
            kcalLabel.setText(mm.round_double(mm.getNewRecipe().getKcal()) + " kcal");
            recipeNameField.setText(mm.getNewRecipe().getName());
            int hours = mm.getNewRecipe().getTime()/60;
            int minutes = mm.getNewRecipe().getTime() - (hours*60);
            hTimeField.setText(String.valueOf(hours));
            minTimeField.setText(String.valueOf(minutes));
            servingsField.setText(String.valueOf(mm.getNewRecipe().getAmount()));
            descriptionArea.setText(mm.getNewRecipe().getDescription());
            cuisineField.setText(mm.getNewRecipe().getCuisine());
        }

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
                    final HBox hBox = new HBox();
                    final Button deleteIngredientButton = new Button();
                    if (mm.getSmallDeleteIcon()!=null){
                        deleteIngredientButton.setGraphic(mm.getSmallDeleteIcon());
                    }
                    else {
                        deleteIngredientButton.setText("Delete");
                    }
                    deleteIngredientButton.setOnAction(event -> deleteIngredientAction(getIndex()));
                    final Button editIngredientButton = new Button();
                    if (mm.getSmallEditIcon()!=null){
                        editIngredientButton.setGraphic(mm.getSmallEditIcon());
                    }
                    else {
                        editIngredientButton.setText("Edit");
                    }
                    editIngredientButton.setOnAction(event -> editIngredientAction(getIndex()));

                    final Label label = new Label(ingredient.toString());
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.getChildren().add(label);
                    hBox.getChildren().add(editIngredientButton);
                    hBox.getChildren().add(deleteIngredientButton);
                    setGraphic(hBox);
                }
            }
        });

        ingredientsList.setFixedCellSize(30);
        ingredientsList.prefHeightProperty().bind(ingredientsList.fixedCellSizeProperty().multiply(Bindings.size(ingredientsList.getItems()).add(0.3)));

        // TableView ===================================================================================================
        instructions = FXCollections.observableArrayList(mm.getNewRecipe().getInstructions());
        instructionTable.setItems(instructions);
        descriptionColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        descriptionColumn.setCellFactory(param -> {
            TableCell<Instruction,String> cell = new TableCell<>();
            Text text = new Text();
            HBox hBox = new HBox();
            VBox vBox = new VBox();
            Button deleteInstructionButton = new Button();
            if (mm.getSmallDeleteIcon()!=null){
                deleteInstructionButton.setGraphic(mm.getSmallDeleteIcon());
            }
            else {
                deleteInstructionButton.setText("Delete");
            }
            deleteInstructionButton.setOnAction(event -> deleteInstructionAction(cell.getIndex()));

            Button editInstructionButton = new Button();
            if (mm.getSmallEditIcon()!=null){
                editInstructionButton.setGraphic(mm.getSmallEditIcon());
            }
            else {
                editInstructionButton.setText("Edit");
            }
            editInstructionButton.setOnAction(event -> editInstructionAction(cell.getIndex()));

            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.getChildren().add(editInstructionButton);
            vBox.getChildren().add(deleteInstructionButton);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().add(vBox);
            hBox.getChildren().add(text);
            cell.setGraphic(hBox);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descriptionColumn.widthProperty().subtract(50));
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

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
                    super.updateItem(image, b);
                        if (image != null) {
                            descriptionImage.setFitHeight(150);
                            descriptionImage.setFitWidth(200);
                            descriptionImage.setImage(image);
                        }
                    }
                };
                cell.setGraphic(descriptionImage);
                return cell;
            });
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            instructionTable.setFixedCellSize(150);
            instructionTable.prefHeightProperty().bind(instructionTable.fixedCellSizeProperty().multiply(Bindings.size(instructionTable.getItems()).add(0.3)));
        } else {
            imageColumn.setPrefWidth(0);
            imageColumn.setMaxWidth(0);
            imageColumn.setMinWidth(0);
        }

//        instructionTable.setOnMouseClicked(event -> );

        // ========================
        split.heightProperty().addListener(l -> scroll.setPrefHeight(split.getHeight()-tool.getHeight()));
        split.setPrefHeight(height);
        split.setPrefWidth(width);

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

    private void addInstructionAction(ActionEvent event) {
        AddInstructionController controller = new AddInstructionController(dm, mm);
        mm.showAndWait(getClass(), "addInstruction-view.fxml", controller, addInstructionButton);

        if (controller.isAdded()) {
            this.mm = controller.getMm();
            setTable(false);
        }
    }

    private void setTable(boolean check){
        if (check){
            mm.getNewRecipe().checkInstructionsImage();
        }
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
                        }
                    }
                };
                cell.setGraphic(descriptionImage);
                return cell;
            });
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            instructionTable.setFixedCellSize(150);
            instructionTable.prefHeightProperty().bind(instructionTable.fixedCellSizeProperty().multiply(Bindings.size(instructionTable.getItems()).add(0.3)));
        } else {
            imageColumn.setPrefWidth(0);
            imageColumn.setMaxWidth(0);
            imageColumn.setMinWidth(0);
        }
    }

    private void editInstructionAction(int index) {
        AddInstructionController controller = new AddInstructionController(dm, mm, mm.getNewRecipe().getInstructions().get(index), recipeKey);
        mm.showAndWait(getClass(), "addInstruction-view.fxml", controller, addIngredientButton);
        setTable(true);
    }

    private void deleteInstructionAction(int index) {
        mm.getNewRecipe().removeInstruction(index);
        setTable(true);
    }

    private void editIngredientAction(int index) {
        AddIngredientController controller = new AddIngredientController(dm, mm, mm.getNewRecipe().getIngredients().get(index));
        mm.showAndWait(getClass(), "addIngredient-view.fxml", controller, addIngredientButton);
        ingredients = FXCollections.observableArrayList(mm.getNewRecipe().getIngredients());
        ingredientsList.setItems(ingredients);
        ingredientsList.setFixedCellSize(30);
        ingredientsList.prefHeightProperty().bind(ingredientsList.fixedCellSizeProperty().multiply(Bindings.size(ingredientsList.getItems()).add(0.3)));
        kcalLabel.setText(mm.round_double(mm.getNewRecipe().countKcal()) + " kcal");
    }

    private void setList(){
        ingredients = FXCollections.observableArrayList(mm.getNewRecipe().getIngredients());
        ingredientsList.setItems(ingredients);
        ingredientsList.setFixedCellSize(30);
        ingredientsList.prefHeightProperty().bind(ingredientsList.fixedCellSizeProperty().multiply(Bindings.size(ingredientsList.getItems()).add(0.3)));
        kcalLabel.setText(mm.round_double(mm.getNewRecipe().getKcal()) + " kcal");
    }

    private void addIngredientAction(ActionEvent event) {
        AddIngredientController controller = new AddIngredientController(dm, mm);
        mm.showAndWait(getClass(), "addIngredient-view.fxml", controller, addIngredientButton);

        if (controller.isAdded()) {
            this.mm = controller.getMm();
            setList();
        }
    }

    private void deleteIngredientAction(int index) {
        mm.getNewRecipe().removeIngredient(index);
        setList();
    }

    private void addRecipeAction(ActionEvent event) {
        if (recipeNameField.getText().length() > 2){
            if(minTimeField.getText().length() > 0 || hTimeField.getText().length() > 0){
                if (servingsField.getText().length() > 0){
                    if (descriptionArea.getText().length() > 2){
                        if (courseChoiceBox.getSelectionModel().getSelectedIndex()>=0) {
                            if (mm.getNewRecipe().getIngredients().size() > 0) {
                                if (mm.getNewRecipe().getInstructions().size() > 0) {
                                    if (this.newRecipeImage != null) {
                                        String cuisine = cuisineField.getText();
                                        if (cuisine.length() < 1) {
                                            cuisine = "-";
                                        } else {
                                            dm.addCuisine(cuisine);
                                        }
                                        mm.setNewRecipe(recipeNameField.getText(),dm.getRecipesSize(), Integer.parseInt(hTimeField.getText()) * 60 + Integer.parseInt(minTimeField.getText()), Integer.parseInt(servingsField.getText()), descriptionArea.getText(), this.newRecipeImage, cuisine, courseChoiceBox.getSelectionModel().getSelectedItem());
                                        RecipeController controller;
                                        if (edit) {
                                            controller = new RecipeController(dm, mm, recipeKey, split.getWidth(), split.getHeight());
                                        } else {
                                            dm.addRecipe(mm.getNewRecipe());
                                            controller = new RecipeController(dm, mm, dm.getRecipes().size() - 1, split.getWidth(), split.getHeight());
                                        }
                                        mm.show(getClass(), "recipe-view.fxml", controller, event);
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
                    recipeImage.setImage(newRecipeImage);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // menu's buttons action
    private void recipesAction(ActionEvent event) {
        RecipesController controller = new RecipesController(dm, mm, split.getWidth(),split.getHeight());
        mm.show(getClass(),"recipes-view.fxml",controller, event);
    }

    private void homeAction(ActionEvent event) {
        recipesAction(event);
    }

    private void backAction(ActionEvent event) {
        recipesAction(event);
    }
}
