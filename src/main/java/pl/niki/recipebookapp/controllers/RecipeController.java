package pl.niki.recipebookapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Instruction;
import pl.niki.recipebookapp.recipes.Recipe;

import java.net.URL;
import java.util.ResourceBundle;

public class RecipeController implements Initializable {
    public ListView<Ingredient> ingredientsList;
    public ListView<Instruction> instructionList;
    public ImageView imageView;
    public Label recipeNameLabel, servingsLabel, kcalLabel, descriptionLabel, timeLabel;;

    private DataManager dm;
    private Recipe recipe;
    private ObservableList<Ingredient> ingredients;
    private ObservableList<Instruction> instructions;
    private int recipeKey;

    public RecipeController(int recipeKey) {
        this.recipeKey = recipeKey;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //System.out.println(recipeKey);
        dm = new DataManager();
        recipe = dm.getRecipes().get(recipeKey);

        // labels ======================================================================================================
        recipeNameLabel.setText(recipe.getName());
        servingsLabel.setText(String.valueOf(recipe.getAmount()));
        kcalLabel.setText(String.valueOf(recipe.getKcal()) + " kcal");
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

        instructions = FXCollections.observableArrayList(recipe.getInstructions());
        instructionList.setItems(instructions);
        instructionList.setCellFactory(i -> new ListCell<>(){
            @Override
            protected void updateItem(Instruction instruction, boolean b) {
                super.updateItem(instruction, b);
                if (b || instruction == null || instruction.getDescription() == null){
                    setText(null);
                }
                else {
                    setText(instruction.getDescription());
                }
            }
        });

        // ImageView
        imageView.setImage(recipe.getImage());
    }
}
