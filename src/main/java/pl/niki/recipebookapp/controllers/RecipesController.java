package pl.niki.recipebookapp.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    public Button backButton, homeButton, recipesButton, addButton;
    public ScrollPane scroll;
    public AnchorPane anchor;
    public HBox hbox;
    public SplitPane split;

    private DataManager dm;
    private MathManager mm;
    private int listAmount;
    private double width, height;

    public RecipesController() {
        dm = new DataManager();
        mm = new MathManager();
        this.width = 1100;
        this.height = 602;
    }

    public RecipesController(DataManager dm, MathManager mm, double width, double height) {
        this.dm = dm;
        this.mm = mm;
        this.width = width;
        this.height = height;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

        //add button
        if (mm.getAddIcon()!=null){
            addButton.setGraphic(mm.getAddIcon());
        }
        addButton.setOnAction(this::addAction);


        // ScrollPane ====================================================================================================
        scroll.widthProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                    hbox.getChildren().clear();
                    listAmount = (int) (scroll.getWidth() / 188);
                    ListView[] recipeList = new ListView[listAmount];
                    ObservableList[] recipes = new ObservableList[listAmount];

                    for (int i =0; i<listAmount; i++){
                        recipes[i] = FXCollections.observableArrayList();
                        recipeList[i] = new ListView<>();
                    }

                    setRecipes(dm.getRecipes(), recipes);

                    for (int i =0; i<listAmount; i++){
                        recipeList[i].setItems(recipes[i]);
                        setList(recipeList[i], i);

                        anchor.setPrefWidth(scroll.getWidth()-15);
                        hbox.setPrefWidth(scroll.getWidth()-15);
                        hbox.getChildren().add(recipeList[i]);
                    }
//                System.out.println((scroll.getWidth()/190));
            }
        });
        scroll.setMinWidth(200);

        split.setPrefWidth(width);
        split.setPrefHeight(height);
    }

    private void setList(ListView<Recipe> recipeList, int i) {
        recipeList.setCellFactory(cell -> new ListCell<>(){
            final Tooltip tooltip = new Tooltip();
            @Override
            protected void updateItem(Recipe recipe, boolean b) {
                super.updateItem(recipe, b);
                if (b || recipe == null || recipe.getName() == null){
                    setText(null);
                    setTooltip(null);
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
                    else {
                        setText(recipe.getName());
                    }
                    tooltip.setText(recipe.getName());
                    setTooltip(tooltip);
                }
            }
        });

        recipeList.setMinWidth(180);
        recipeList.setFixedCellSize(190);
        recipeList.prefHeightProperty().bind(recipeList.fixedCellSizeProperty().multiply(Bindings.size(recipeList.getItems()).add(0.2)));

        recipeList.setOnMouseClicked(event -> {
            int selectedRecipe = recipeList.getSelectionModel().getSelectedIndex() * this.listAmount + i;
            click(selectedRecipe, event);
        });
    }

    private void setRecipes(List<Recipe> dmList, List<Recipe> recipes[]) {
        for (int i=0; i<dmList.size();i++){
            recipes[i%this.listAmount].add(dmList.get(i));
        }
    }

    public void click(int selectedRecipe, MouseEvent mouseEvent) {
//        System.out.println("click" + selectedRecipe);
        if(selectedRecipe >= 0) {
            RecipeController controller = new RecipeController(dm, mm, selectedRecipe, split.getWidth(), split.getHeight());
            mm.show(getClass(),"recipe-view.fxml",controller,mouseEvent);
        }
    }


    // menu's buttons action
    private void addAction(ActionEvent event) {
        AddController controller = new AddController(dm, mm, split.getWidth(), split.getHeight());
        mm.show(getClass(),"add-view.fxml",controller,event);
    }

    private void homeAction(ActionEvent event) {
//        backAction(event);
    }

    private void backAction(ActionEvent event) {
//        mm.show(getClass(), "recipes-view.fxml", event);
    }
}
