package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.Manager;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Instruction;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;
import java.net.URL;
import java.util.*;

public class RecipeController implements Initializable {
    // =================================================================================================================
    // Public fields - elements of the recipe-view.fxml
    // =================================================================================================================
    public ListView<Ingredient> ingredientsList;
    public TableView<Instruction> instructionTable;
    public TableColumn<Instruction,String> descriptionColumn;
    public TableColumn<Instruction, Image> imageColumn;
    public ImageView recipeImage;
    public Label recipeNameLabel, servingsLabel, kcalLabel, descriptionLabel, timeLabel, courseLabel, cuisineLabel;
    public Button printButton, backButton, homeButton, recipesButton, addButton, nextButton, prevButton, deleteButton, editButton;
    public SplitPane split;
    public ScrollPane scroll;
    public ToolBar tool;
    public BorderPane border;
    public Hyperlink webSiteHyperLink;

    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private final DataManager dm;
    private final Manager mm;
    private final Recipe recipe;
    private final double width, height;
    private final ObservableSet<String> selectedCuisine, selectedCourse;
    private final ObservableSet<Product> selectedIngredient;
    private final int minTimeValue, maxTimeValue, minKcalValue, maxKcalValue, sortIndex;
    private final List<Recipe> recipeList;
    private final String searchText;

    // =================================================================================================================
    // Constructors
    // =================================================================================================================
    public RecipeController(DataManager dm, Manager mm, Recipe recipeKey, double width, double height) {
        this.dm = dm;
        this.mm = mm;
        this.recipe = recipeKey;
        this.width = width;
        this.height = height;
        this.selectedCuisine = FXCollections.observableSet();
        this.selectedCourse = FXCollections.observableSet();
        this.selectedIngredient = FXCollections.observableSet();
        this.minKcalValue=0;
        this.minTimeValue=0;
        dm.setMax();
        this.maxKcalValue = dm.getMaxKcal();
        this.maxTimeValue = dm.getMaxTime();
        this.recipeList = dm.getRecipes();
        this.searchText = "";
        this.sortIndex = 0;
    }

    public RecipeController(DataManager dm, Manager mm, Recipe recipeKey, double width, double height, ObservableSet<String> selectedCuisine, ObservableSet<String> selectedCourse, ObservableSet<Product> selectedIngredient, int minTimeValue, int maxTimeValue, int minKcalValue, int maxKcalValue, List<Recipe> recipeList, String searchText, int sortIndex) {
        this.dm = dm;
        this.mm = mm;
        this.recipe = recipeKey;
        this.width = width;
        this.height = height;
        this.selectedCuisine = selectedCuisine;
        this.selectedCourse = selectedCourse;
        this.selectedIngredient = selectedIngredient;
        this.minTimeValue = minTimeValue;
        this.maxTimeValue = maxTimeValue;
        this.minKcalValue = minKcalValue;
        this.maxKcalValue = maxKcalValue;
        this.recipeList = recipeList;
        this.searchText = searchText;
        this.sortIndex = sortIndex;
    }

    // =================================================================================================================
    // Override methods
    // =================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // height of window
        split.heightProperty().addListener(listener -> scroll.setPrefHeight(split.getHeight() - tool.getHeight()));
        split.setPrefHeight(height);
        split.setPrefWidth(width);

        // HyperLink ===================================================================================================
        if (recipe.getUrl() != null) {
            webSiteHyperLink.setGraphic(mm.getWebsiteIcon());
            webSiteHyperLink.setOnAction(event -> dm.getHostServices().showDocument(recipe.getUrl()));
        } else {
            webSiteHyperLink.setVisible(false);
        }

        // Labels ======================================================================================================
        recipeNameLabel.setText(recipe.getName().toUpperCase());
        servingsLabel.setText(String.valueOf(recipe.getAmount()));
        kcalLabel.setText(mm.round_double(recipe.getKcal()) + " kcal");
        descriptionLabel.setText(recipe.getDescription());
        timeLabel.setText(mm.countTime(recipe.getTime()));
        courseLabel.setText(recipe.getCourse());
        cuisineLabel.setText(recipe.getCuisine());

        // ListView ====================================================================================================
        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList(recipe.getIngredients());
        ingredientsList.setItems(ingredients);
        ingredientsList.setCellFactory(i -> new ListCell<>() {
            @Override
            protected void updateItem(Ingredient ingredient, boolean b) {
                super.updateItem(ingredient, b);
                if (b || ingredient == null || ingredient.toString() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(ingredient.toString());
                    setGraphic(mm.getDoneIcon(true));
                }
            }
        });

        // no scroll, no focus
        ingredientsList.setMouseTransparent(true);
        ingredientsList.setFocusTraversable(false);

        // height of list
        ingredientsList.setFixedCellSize(30);
        ingredientsList.prefHeightProperty().bind(ingredientsList.fixedCellSizeProperty().multiply(Bindings.size(ingredientsList.getItems()).add(0.3)));

        // TableView ===================================================================================================
        ObservableList<Instruction> instructions = FXCollections.observableArrayList(recipe.getInstructions());
        instructionTable.setItems(instructions);

        // height of table
        instructionTable.setFixedCellSize(150);
        instructionTable.prefHeightProperty().bind(instructionTable.fixedCellSizeProperty().multiply(Bindings.size(instructionTable.getItems()).add(0.3)));

        // description column - 1st column
        descriptionColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());                           // ?????????????????????????????????????????????????????????---
        descriptionColumn.setCellFactory(param -> {
            TableCell<Instruction, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descriptionColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        // image column - 2nd column
        if (recipe.isInstructionImage()) {
            imageColumn.setCellFactory(cell -> new TableCell<>() {
                @Override
                protected void updateItem(Image image, boolean b) {
                    super.updateItem(image, b);
                    if (b || image == null) {
                        setGraphic(null);
                    } else {
                        ImageView descriptionImage = new ImageView();
                        descriptionImage.setFitHeight(150);
                        descriptionImage.setFitWidth(200);
                        descriptionImage.setImage(image);
                        setGraphic(descriptionImage);
                    }
                }
            });
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));                                    // ---???????????????????????????????????????????????????????????????
        } else {
            imageColumn.setPrefWidth(0);
            imageColumn.setMaxWidth(0);
            imageColumn.setMinWidth(0);
        }

        // no scroll, no focus
        instructionTable.setMouseTransparent(true);
        instructionTable.setFocusTraversable(false);

        // ImageView ===================================================================================================
        recipeImage.setImage(recipe.getImage());
        mm.setRectangleImage(recipeImage, 30, Color.WHITE);

        // Buttons =====================================================================================================
        // next button
        nextButton.setGraphic(mm.getNextIcon());

        if (recipeList.indexOf(recipe) < (recipeList.size() - 1)) {
            nextButton.setOnAction(this::nextAction);
        } else {
            nextButton.setDisable(true);
        }

        // prev button
        prevButton.setGraphic(mm.getBackIcon());

        if (recipeList.indexOf(recipe) > 0) {
            prevButton.setOnAction(this::prevAction);
        } else {
            prevButton.setDisable(true);
        }

        // delete button
        deleteButton.setGraphic(mm.getDeleteIcon(false));
        deleteButton.setOnAction(this::deleteAction);

        // edit button
        editButton.setGraphic(mm.getEditIcon());
        editButton.setOnAction(this::editAction);

        // print button
        printButton.setGraphic(mm.getPrintIcon());
        printButton.setOnAction(this::printAction);

        // back button
        backButton.setGraphic(mm.getBackIcon());
        backButton.setOnAction(this::backAction);

        //home button
        homeButton.setGraphic(mm.getHomeIcon());
        homeButton.setOnAction(this::homeAction);

        //recipes button
        recipesButton.setGraphic(mm.getRecipesIcon());
        recipesButton.setOnAction(this::recipesAction);

        //add button
        addButton.setGraphic(mm.getAddIcon());
        addButton.setOnAction(this::addAction);
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================
    private void printAction(ActionEvent event) {

        double borderHeight = border.getHeight();
        double defaultPrintHeight = 1305;
        double printHeight = 1220;
        border = new BorderPane(border);

        List<WritableImage> screenshot = new ArrayList<>();

        if (borderHeight > defaultPrintHeight){
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            for (int i=0; i < borderHeight/printHeight; i++){
                snapshotParameters.setViewport(new Rectangle2D(0,i*printHeight,927,(i+1)*printHeight));
                screenshot.add(border.snapshot(snapshotParameters, null));
            }
            System.out.println(screenshot.size());

        }
        else {
            screenshot.add(border.snapshot(null, null));
        }

        RecipeController refreshController = new RecipeController(dm,mm, recipe, split.getWidth(),split.getHeight(),selectedCourse,selectedCuisine,selectedIngredient,minTimeValue,maxTimeValue,minKcalValue,maxKcalValue,recipeList, searchText, sortIndex);
        mm.show(getClass(), "recipe-view.fxml", refreshController, event);

        PrintController controller = new PrintController(screenshot);
        mm.showAndWait(getClass(), "print-view.fxml",controller, printButton.getScene().getWindow(), "Print Recipe");
    } // Print Button Action

    private void editAction(ActionEvent event) {
        AddController controller = new AddController(dm,mm, recipe, split.getWidth(), split.getHeight());
        mm.show(getClass(),"add-view.fxml",controller,event);
    } // Edit Button Action

    private void deleteAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to delete this recipe?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int recipeKey = recipeList.indexOf(recipe);
            recipeList.remove(recipe);
            dm.getRecipes().remove(recipe);
            if (recipeKey >= recipeList.size()){
                recipeKey = recipeList.size()-1;
            }
            if (recipeList.size()>0) {
                RecipeController controller = new RecipeController(dm, mm, recipeList.get(recipeKey), split.getWidth(), split.getHeight(), selectedCourse, selectedCuisine, selectedIngredient, minTimeValue, maxTimeValue, minKcalValue, maxKcalValue, recipeList,searchText, sortIndex);
                mm.show(getClass(), "recipe-view.fxml", controller, event);
            }
            else {
                RecipesController controller = new RecipesController(dm,mm,split.getWidth(),split.getHeight(),selectedCourse,selectedCuisine,selectedIngredient,minTimeValue,maxTimeValue,minKcalValue,maxKcalValue,searchText, sortIndex);
                mm.show(getClass(),"recipes-view.fxml", controller, event);
            }
        }
    } // Delete Button Action

    private void nextAction(ActionEvent event) {
        RecipeController controller = new RecipeController(dm,mm,recipeList.get(recipeList.indexOf(recipe)+1), split.getWidth(), split.getHeight(), selectedCourse,selectedCuisine,selectedIngredient,minTimeValue,maxTimeValue,minKcalValue,maxKcalValue,recipeList,searchText, sortIndex);
        mm.show(getClass(),"recipe-view.fxml",controller,event);
    } // Next Button Action

    private void prevAction(ActionEvent event) {
        RecipeController controller = new RecipeController(dm,mm,recipeList.get(recipeList.indexOf(recipe)-1), split.getWidth(), split.getHeight(),selectedCourse,selectedCuisine,selectedIngredient,minTimeValue,maxTimeValue,minKcalValue,maxKcalValue,recipeList,searchText, sortIndex);
        mm.show(getClass(),"recipe-view.fxml",controller,event);
    } // Prev Button Action

    // Menu's Buttons Actions ==========================================================================================
    private void recipesAction(ActionEvent event) { //
        RecipesController controller = new RecipesController(dm,mm, split.getWidth(), split.getHeight(), selectedCourse, selectedCuisine, selectedIngredient, minTimeValue,maxTimeValue,minKcalValue,maxKcalValue,searchText, sortIndex);
        mm.show(getClass(),"recipes-view.fxml",controller,event);
    } // Recipes Button Action

    private void addAction(ActionEvent event) {
        AddController controller = new AddController(dm, mm, split.getWidth(), split.getHeight());
        mm.show(getClass(),"add-view.fxml",controller,event);
    } // Add Recipes Button Action

    private void homeAction(ActionEvent event) {
        recipesAction(event);
    } // Home Button Action

    private void backAction(ActionEvent event) {
        recipesAction(event);
    } // Back Button Action
}
