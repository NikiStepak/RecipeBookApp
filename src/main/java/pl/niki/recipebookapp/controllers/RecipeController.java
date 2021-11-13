package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Instruction;
import pl.niki.recipebookapp.recipes.Recipe;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RecipeController implements Initializable {
    public ListView<Ingredient> ingredientsList;
    public TableView<Instruction> instructionTable;
    public TableColumn<Instruction,String> descriptionColumn;
    public TableColumn<Instruction, Image> imageColumn;
    public ImageView recipeImage;
    public Label recipeNameLabel, servingsLabel, kcalLabel, descriptionLabel, timeLabel;
    public Button backButton, homeButton, recipesButton, addButton, nextButton, prevButton, deleteButton, editButton;
    public SplitPane split;
    public ScrollPane scroll;
    public ToolBar tool;

    private final DataManager dm;
    private final MathManager mm;
    private final int recipeKey;
    private final double width, height;

    public RecipeController(DataManager dm, MathManager mm, int recipeKey, double width, double height) {
        this.dm = dm;
        this.mm = mm;
        this.recipeKey = recipeKey;
        this.width = width;
        this.height = height;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Recipe recipe = dm.getRecipes().get(recipeKey);

        // labels ======================================================================================================
        recipeNameLabel.setText(recipe.getName().toUpperCase());
        servingsLabel.setText(String.valueOf(recipe.getAmount()));
        kcalLabel.setText(mm.round_double(recipe.getKcal()) + " kcal");
        descriptionLabel.setText(recipe.getDescription());
        timeLabel.setText(recipe.getTime());

        // ListView ====================================================================================================
        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList(recipe.getIngredients());
        ingredientsList.setItems(ingredients);
        ingredientsList.setCellFactory(i -> new ListCell<>(){
            @Override
            protected void updateItem(Ingredient ingredient, boolean b) {
                super.updateItem(ingredient, b);
                if (b || ingredient == null || ingredient.toString() == null){
                    setText(null);
                }
                else {
                    setText(ingredient.toString());
                }
            }
        });

        // TableView ===================================================================================================
        ObservableList<Instruction> instructions = FXCollections.observableArrayList(recipe.getInstructions());
        instructionTable.setItems(instructions);
        instructionTable.setFixedCellSize(150);
        instructionTable.prefHeightProperty().bind(instructionTable.fixedCellSizeProperty().multiply(Bindings.size(instructionTable.getItems()).add(0.3)));

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

        if(recipe.isInstructionImage()) {
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
        }
        else {
            imageColumn.setPrefWidth(0);
            imageColumn.setMaxWidth(0);
            imageColumn.setMinWidth(0);
        }

        // no scroll, no focus list and table
        ingredientsList.setMouseTransparent(true);
        ingredientsList.setFocusTraversable(false);

        instructionTable.setMouseTransparent(true);
        instructionTable.setFocusTraversable(false);

        // height of list
        ingredientsList.setFixedCellSize(30);
        ingredientsList.prefHeightProperty().bind(ingredientsList.fixedCellSizeProperty().multiply(Bindings.size(ingredientsList.getItems()).add(0.3)));

        // ImageView ===================================================================================================
        recipeImage.setImage(recipe.getImage());

        //rounded rectangle ImageView
        Rectangle rectangle = new Rectangle(recipeImage.getFitWidth(), recipeImage.getFitHeight());
        rectangle.setArcWidth(25);
        rectangle.setArcHeight(25);
        recipeImage.setClip(rectangle);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = recipeImage.snapshot(parameters, null);

        //add shadow
        recipeImage.setClip(null);
        recipeImage.setEffect(new DropShadow(20, Color.WHITE));

        recipeImage.setImage(image);

        // height of window ===============
        split.heightProperty().addListener(listener -> scroll.setPrefHeight(split.getHeight()-tool.getHeight()));
        split.setPrefHeight(height);
        split.setPrefWidth(width);

        // Button ======================================================================================================
        // next button
        if (mm.getNextIcon() != null) {
            nextButton.setGraphic(mm.getNextIcon());
        }
        else {
            nextButton.setText("Next");
        }
        if (recipeKey < (dm.getRecipes().size()-1)) {
            nextButton.setOnAction(this::nextAction);
        }
        else {
            nextButton.setDisable(true);
        }

        // back button
        // prev button
        if(mm.getBackIcon()!=null) {
            prevButton.setGraphic(mm.getBackIcon());
            backButton.setGraphic(mm.getBackIcon());
        }
        else {
            backButton.setText("Back");
            prevButton.setText("Prev");
        }
        backButton.setOnAction(this::backAction);
        if (recipeKey > 0){
            prevButton.setOnAction(this::prevAction);
        }
        else {
            prevButton.setDisable(true);
        }

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

        //add button
        if (mm.getAddIcon()!=null){
            addButton.setGraphic(mm.getAddIcon());
        }
        addButton.setOnAction(this::addAction);

        // delete button
        if (mm.getDeleteIcon()!=null){
            deleteButton.setGraphic(mm.getDeleteIcon());
        }
        else {
            deleteButton.setText("Delete");
        }
        deleteButton.setOnAction(this::deleteAction);

        // edit button
        if (mm.getEditIcon()!=null){
            editButton.setGraphic(mm.getEditIcon());
        }
        else {
            editButton.setText("Edit");
        }
        editButton.setOnAction(this::editAction);
    }

    private void editAction(ActionEvent event) {
        AddController controller = new AddController(dm,mm,recipeKey, split.getWidth(), split.getHeight());
        mm.show(getClass(),"add-view.fxml",controller,event);
    }

    private void deleteAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to delete this recipe?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dm.getRecipes().remove(recipeKey);
            RecipeController controller = new RecipeController(dm, mm, recipeKey, split.getWidth(), split.getHeight());
            mm.show(getClass(), "recipe-view.fxml", controller, event);
        }
    }

    private void nextAction(ActionEvent event) {
        RecipeController controller = new RecipeController(dm,mm,recipeKey+1, split.getWidth(), split.getHeight());
        mm.show(getClass(),"recipe-view.fxml",controller,event);
    }

    private void prevAction(ActionEvent event) {
        RecipeController controller = new RecipeController(dm,mm,recipeKey-1, split.getWidth(), split.getHeight());
        mm.show(getClass(),"recipe-view.fxml",controller,event);
    }

    // menu's buttons action
    private void recipesAction(ActionEvent event) {
        RecipesController controller = new RecipesController(dm,mm, split.getWidth(), split.getHeight());
        mm.show(getClass(),"recipes-view.fxml",controller,event);
    }

    private void addAction(ActionEvent event) {
        AddController controller = new AddController(dm, mm, split.getWidth(), split.getHeight());
        mm.show(getClass(),"add-view.fxml",controller,event);
    }

    private void homeAction(ActionEvent event) {
        recipesAction(event);
    }

    private void backAction(ActionEvent event) {
        recipesAction(event);
    }
}
