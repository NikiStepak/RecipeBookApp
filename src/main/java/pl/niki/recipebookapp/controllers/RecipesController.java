package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;
import java.net.URL;
import java.util.*;

public class RecipesController implements Initializable {
    public Button backButton, homeButton, recipesButton, addButton;
    public ToggleButton filterToggleButton;
    public TextField searchField; //ingredientField;
    public ChoiceBox<String> sortChoiceBox;
    public  ListView<Product> ingredientsList; //, cuisineList, courseList,
//    public Slider minTimeSlider, maxTimeSlider, maxRatingsSlider, minRatingsSlider, minKcalSlider, maxKcalSlider;
//    public Label minTimeLabel, maxTimeLabel, maxKcalLabel, minKcalLabel, minRatingsLabel, maxRatingsLabel;
    public ScrollPane scroll;
    public AnchorPane anchor, filterAnchor; //, ratingAnchor;
    public HBox hBox;
    public VBox vBox;
    public SplitPane split;
    public ToolBar tool;

    private final DataManager dm;
    private final MathManager mm;
    private int listAmount;
    private final double width, height;
    private final ObservableList<String> choice = FXCollections.observableArrayList("Sort by ...", "A -> Z", "Z -> A");
    private boolean isFilter;

    public RecipesController() {
        dm = new DataManager();
        mm = new MathManager();
        this.width = 1100;
        this.height = 602;
        this.isFilter = false;
    }

    public RecipesController(DataManager dm, MathManager mm, double width, double height) {
        this.dm = dm;
        this.mm = mm;
        this.width = width;
        this.height = height;
        this.isFilter = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Product> ingredients = FXCollections.observableArrayList(mm.getIngredients(dm.getRecipes()));
        ingredientsList.setItems(ingredients);
        ObservableSet<Product> selectedIngredient = FXCollections.observableSet();
        ingredientsList.setCellFactory(CheckBoxListCell.forListView(product -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected){
                    selectedIngredient.add(product);
                }
                else {
                    selectedIngredient.remove(product);
                }
                ingredientsFilterAction(selectedIngredient);
            });
            observable.set(selectedIngredient.contains(product));
            selectedIngredient.addListener((SetChangeListener.Change<? extends Product> c)->observable.set(selectedIngredient.contains(product)));
            return observable;
        }));

        searchField.setOnKeyTyped(this::searchAction);
        sortChoiceBox.setItems(choice);
        sortChoiceBox.setOnAction(this::sortAction);
        sortChoiceBox.getSelectionModel().select(0);
        if(mm.getFilterIcon()!=null) {
            filterToggleButton.setGraphic(mm.getFilterIcon());
        }

        filterToggleButton.setOnAction(this::filterAction);
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
        scroll.widthProperty().addListener(observable -> setPane(null,false,dm.getRecipes()));
        scroll.setMinWidth(200);

        split.heightProperty().addListener(l -> scroll.setPrefHeight(split.getHeight()-tool.getHeight()));

        split.setPrefWidth(width);
        split.setPrefHeight(height);
    }

    private void ingredientsFilterAction(ObservableSet<Product> selectedIngredient) {
        setPane(null,false,dm.getIngredientFilteredRecipes(selectedIngredient));
    }

    private void filterAction(ActionEvent event) {
        filterAnchor.setVisible(filterToggleButton.isSelected());
        this.isFilter = !this.isFilter;
        setPane(null, false, dm.getRecipes());
    }

    private void setPane(Comparator<Recipe> comparator, boolean sort, List<Recipe> searchedRecipes){
        hBox.getChildren().clear();
        if (filterAnchor.isVisible()) {
            filterAnchor.setMinHeight(this.split.getHeight() - tool.getHeight()-5);
            hBox.setAlignment(Pos.TOP_LEFT);
//            hBox.getChildren().add(filterAnchor);
            listAmount = (int) ((scroll.getWidth() - 188) / 188);
        }
        else {
            listAmount = (int) (scroll.getWidth() / 188);
        }
        ListView<Recipe>[] recipeList = new ListView[listAmount];
        ObservableList<Recipe>[] recipes = new ObservableList[listAmount];

        for (int i = 0; i < listAmount; i++) {
            recipes[i] = FXCollections.observableArrayList();
            recipeList[i] = new ListView<>();
        }

        if (sort) {
            List<Recipe> rrr = dm.getRecipes();
            rrr.sort(comparator);
            setRecipes(rrr, recipes);
        }
        else {
            setRecipes(searchedRecipes, recipes);
        }

        for (int i = 0; i < listAmount; i++) {
            recipeList[i].setItems(recipes[i]);
            setList(recipeList[i], i);

            anchor.setPrefWidth(scroll.getWidth() - 15);
            hBox.setPrefWidth(scroll.getWidth() - 15);
            hBox.getChildren().add(recipeList[i]);
        }
        if (filterAnchor.isVisible()) {
//            filterAnchor.setMinHeight(this.split.getHeight() - tool.getHeight() - 5);
//            hBox.setAlignment(Pos.TOP_LEFT);
            hBox.getChildren().add(filterAnchor);
        }
    }

    private void sortAction(Event event) {
        if (sortChoiceBox.getSelectionModel().getSelectedIndex() == 1) {
            setPane(Comparator.comparing(Recipe::getName), true, null);
        }
        else if (sortChoiceBox.getSelectionModel().getSelectedIndex() == 2){
            setPane(Comparator.comparing(Recipe::getName).reversed(), true, null);
        }
    }

    private void searchAction(KeyEvent keyEvent) {
        setPane(null,false, dm.getSearchedRecipes(searchField.getText()));
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

    private void setRecipes(List<Recipe> dmList, List<Recipe>[] recipes) {
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
