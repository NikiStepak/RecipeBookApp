package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Instruction;
import pl.niki.recipebookapp.recipes.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeController implements Initializable {
    public ListView<Ingredient> ingredientsList;
    //public ListView<Instruction> instructionList;
    public TableView<Instruction> instructionTable;
    public TableColumn<Instruction,String> descriptionColumn;
    public TableColumn<Instruction, Image> imageColumn;
    public ImageView recipeImage;
    public Label recipeNameLabel, servingsLabel, kcalLabel, descriptionLabel, timeLabel;
    public Button backButton, homeButton, recipesButton;

    private DataManager dm;
    private MathManager mm;
    private Recipe recipe;
    private ObservableList<Ingredient> ingredients;
    private ObservableList<Instruction> instructions;
    private final int recipeKey;

    public RecipeController(int recipeKey) {
        this.recipeKey = recipeKey;
    }

    public RecipeController(DataManager dm, MathManager mm, int recipeKey) {
        this.dm = dm;
        this.mm = mm;
        this.recipeKey = recipeKey;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //System.out.println(recipeKey);
//        dm = new DataManager();
//        mm = new MathManager();
        recipe = dm.getRecipes().get(recipeKey);

        // labels ======================================================================================================
        recipeNameLabel.setText(recipe.getName().toUpperCase());
        servingsLabel.setText(String.valueOf(recipe.getAmount()));
        kcalLabel.setText(mm.round_double(recipe.getKcal()) + " kcal");
        descriptionLabel.setText(recipe.getDescribe());
        timeLabel.setText(recipe.getTime());

        // ListView ====================================================================================================
        ingredients = FXCollections.observableArrayList(recipe.getIngredients());
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
        instructions = FXCollections.observableArrayList(recipe.getInstructions());
        instructionTable.setItems(instructions);
        instructionTable.setFixedCellSize(150);
        instructionTable.prefHeightProperty().bind(instructionTable.fixedCellSizeProperty().multiply(Bindings.size(instructionTable.getItems()).add(0.3)));;

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
                        } else {
                        }
                    }
                };
                cell.setGraphic(descriptionImage);
                return cell;
            });
            imageColumn.setCellValueFactory(new PropertyValueFactory<Instruction, Image>("image"));
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
        homeButton.setOnAction(this::backAction);

        //recipe button
        if (mm.getRecipesIcon()!=null){
            recipesButton.setGraphic(mm.getRecipesIcon());
        }
        recipesButton.setOnAction(this::backAction);
    }

    private void backAction(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/recipes-view.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("/pl/niki/recipebookapp/styles/recipe-style.css").toExternalForm());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
