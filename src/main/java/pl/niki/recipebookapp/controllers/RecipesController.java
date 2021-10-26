package pl.niki.recipebookapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.recipes.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipesController implements Initializable {
    public ListView<Recipe> recipesList;
    private ObservableList<Recipe> recipes;
    private DataManager dm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dm = new DataManager();

        // ListView ====================================================================================================
        recipes = FXCollections.observableArrayList(dm.getRecipes());
        recipesList.setItems(recipes);
        recipesList.setCellFactory(i -> new ListCell<>(){
            @Override
            protected void updateItem(Recipe recipe, boolean b) {
                super.updateItem(recipe, b);
                if (b || recipe == null || recipe.getImage() == null || recipe.getName() == null){
                    setText(null);
                }
                else {
                    setText(recipe.getName());
                }
            }
        });
    }

    public void clicked(MouseEvent mouseEvent) throws IOException {
        int selectedRecipe = recipesList.getSelectionModel().getSelectedIndex();
        //System.out.println("click" + selectedRecipe);
        if(selectedRecipe >= 0) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/recipe-view.fxml"));
            RecipeController controller = new RecipeController(selectedRecipe);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pl/niki/recipebookapp/styles/recipe-style.css").toExternalForm());
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
}
