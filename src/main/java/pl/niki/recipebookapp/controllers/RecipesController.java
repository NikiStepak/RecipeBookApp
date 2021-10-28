package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RecipesController implements Initializable {
    public ListView<Recipe> recipesList1, recipesList2, recipesList3, recipesList4, recipesList5;
    public Button backButton, homeButton, recipesButton;
    private ObservableList<Recipe> recipes1, recipes2, recipes3, recipes4, recipes5;
    private DataManager dm;
    private MathManager mm;

    public RecipesController() {
        this.recipes1 = FXCollections.observableArrayList();
        this.recipes2 = FXCollections.observableArrayList();
        this.recipes3 = FXCollections.observableArrayList();
        this.recipes4 = FXCollections.observableArrayList();
        this.recipes5 = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dm = new DataManager();
        mm = new MathManager();

        // ListView ====================================================================================================
        setRecipes(dm.getRecipes());
        recipesList1.setItems(recipes1);
        recipesList2.setItems(recipes2);
        recipesList3.setItems(recipes3);
        recipesList4.setItems(recipes4);
        recipesList5.setItems(recipes5);

        setList(recipesList1);
        setList(recipesList2);
        setList(recipesList3);
        setList(recipesList4);
        setList(recipesList5);

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
    }

    private void setList(ListView<Recipe> recipeList) {
        recipeList.setCellFactory(cell -> new ListCell<>(){
            @Override
            protected void updateItem(Recipe recipe, boolean b) {
                super.updateItem(recipe, b);
                if (b || recipe == null || recipe.getName() == null){
                    setText(null);
                }
                else {
                    if (recipe.getImage()!=null){
                        final VBox vBox = new VBox();
                        final Label label = new Label(recipe.getName());
                        label.getStyleClass().add("blackText");
                        final ImageView recipeImage = new ImageView();
                        recipeImage.setFitHeight(150);
                        recipeImage.setFitWidth(150);
                        recipeImage.setImage(recipe.getImage());
                        vBox.setAlignment(Pos.CENTER);
                        vBox.getChildren().add(recipeImage);
                        vBox.getChildren().add(label);
                        setGraphic(vBox);
                    }
                    else
                        setText(recipe.getName());
                }
            }
        });

        recipeList.setFixedCellSize(188);
        recipeList.prefHeightProperty().bind(recipeList.fixedCellSizeProperty().multiply(Bindings.size(recipeList.getItems()).add(0.2)));

    }


    private void setRecipes(List<Recipe> recipes) {
        for (int i=0; i<recipes.size();i++){
            switch (i % 5) {
                case 0 -> recipes1.add(recipes.get(i));
                case 1 -> recipes2.add(recipes.get(i));
                case 2 -> recipes3.add(recipes.get(i));
                case 3 -> recipes4.add(recipes.get(i));
                case 4 -> recipes5.add(recipes.get(i));
            }
        }
    }

    public void click(int selectedRecipe, MouseEvent mouseEvent) {
//        System.out.println("click" + selectedRecipe);
        if(selectedRecipe >= 0) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/recipe-view.fxml"));
                RecipeController controller = new RecipeController(dm, mm, selectedRecipe);
                loader.setController(controller);
                Parent root = null;
                root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/pl/niki/recipebookapp/styles/recipe-style.css").toExternalForm());
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clicked1(MouseEvent mouseEvent) {
        int selectedRecipe = recipesList1.getSelectionModel().getSelectedIndex() * 5;
        click(selectedRecipe, mouseEvent);
    }

    public void clicked2(MouseEvent mouseEvent) {
        int selectedRecipe = (recipesList2.getSelectionModel().getSelectedIndex() * 5) + 1;
        click(selectedRecipe, mouseEvent);    }

    public void clicked3(MouseEvent mouseEvent) {
        int selectedRecipe = (recipesList3.getSelectionModel().getSelectedIndex() * 5) + 2;
        click(selectedRecipe, mouseEvent);
    }

    public void clicked4(MouseEvent mouseEvent) {
        int selectedRecipe = (recipesList4.getSelectionModel().getSelectedIndex() * 5) + 3;
        click(selectedRecipe, mouseEvent);
    }

    public void clicked5(MouseEvent mouseEvent) {
        int selectedRecipe = (recipesList5.getSelectionModel().getSelectedIndex() * 5) + 4;
        click(selectedRecipe, mouseEvent);
    }
}
