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

    private DataManager dm;
    private MathManager mm;
    private int listAmount;

    public RecipesController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dm = new DataManager();
        mm = new MathManager();

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
        if (mm.getAddIcon()!=null){
            addButton.setGraphic(mm.getAddIcon());
        }
//        addButton.setOnAction(this::backAction);


        // ListView ====================================================================================================
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

    public void addAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/add-view.fxml"));
            AddController controller = new AddController(dm, mm);
            loader.setController(controller);
            Parent root = null;
            root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pl/niki/recipebookapp/styles/add-style.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
