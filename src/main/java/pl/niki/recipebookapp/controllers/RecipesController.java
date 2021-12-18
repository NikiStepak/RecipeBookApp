package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;
import java.net.URL;
import java.util.*;

public class RecipesController implements Initializable {
    // =================================================================================================================
    // Public fields - elements of the recipes-view.fxml
    // =================================================================================================================
    public Button backButton, homeButton, recipesButton, addButton, timeFilterButton, kcalFilterButton, cleanButton;
    public ToggleButton filterToggleButton;
    public TextField searchField, ingredientSearchField, cuisineSearchField;
    public ChoiceBox<String> sortChoiceBox;
    public ListView<Product> ingredientsList;
    public ListView<String> courseList, cuisineList;
    public Slider minTimeSlider, maxTimeSlider, minKcalSlider, maxKcalSlider;
    public Label minTimeLabel, maxTimeLabel, maxKcalLabel, minKcalLabel, courseLabel, cuisineLabel, ingredientLabel, timeLabel, kcalLabel;
    public ScrollPane scroll, filterScroll;
    public AnchorPane anchor, filterAnchor;
    public HBox listHBox;
    public VBox filterVBox;
    public SplitPane split;
    public ToolBar tool;
    public BorderPane border;
    public VBox vBox;

    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private final DataManager dm;
    private final MathManager mm;
    private final double width, height;
    private final ObservableList<String> choice = FXCollections.observableArrayList("Sort by ...", "A -> Z", "Z -> A");
    private final ObservableSet<String> selectedCourse, selectedCuisine;
    private final ObservableSet<Product> selectedIngredient;
    private final int maxTime, maxKcal, sortIndex;
    private final String searchText;

    private ObservableList<String> cuisines;
    private ObservableList<Product> ingredients;
    private boolean isFilter, isTimeSet, isKcalSet, addedVBox;
    private int minTimeValue, maxTimeValue,minKcalValue, maxKcalValue, listAmount;
    private List<Recipe> commonRecipes;

    // =================================================================================================================
    // Constructors
    // =================================================================================================================
    public RecipesController() {
        this.dm = new DataManager();
        this.mm = new MathManager();
        this.width = 1100;
        this.height = 602;
        this.selectedCourse = FXCollections.observableSet();
        this.selectedCuisine = FXCollections.observableSet();
        this.selectedIngredient = FXCollections.observableSet();
        dm.setMax();
        this.maxKcal = dm.getMaxKcal();
        this.maxTime = dm.getMaxTime();
        this.sortIndex = 0;
        this.searchText = "";

        this.isFilter = false;
        this.isTimeSet = false;
        this.isKcalSet = false;
        this.addedVBox = false;
        this.minTimeValue = 0;
        this.maxTimeValue = maxTime;
        this.minKcalValue = 0;
        this.maxKcalValue = maxKcal;
        this.commonRecipes = new ArrayList<>(dm.getRecipes());
    }

    public RecipesController(DataManager dm, MathManager mm, double width, double height) {
        this.dm = dm;
        this.mm = mm;
        this.width = width;
        this.height = height;
        this.selectedCourse = FXCollections.observableSet();
        this.selectedCuisine = FXCollections.observableSet();
        this.selectedIngredient = FXCollections.observableSet();
        dm.setMax();
        this.maxKcal = dm.getMaxKcal();
        this.maxTime = dm.getMaxTime();
        this.sortIndex = 0;
        this.searchText = "";

        this.isFilter = false;
        this.isTimeSet = false;
        this.isKcalSet = false;
        this.addedVBox = false;
        this.minTimeValue = 0;
        this.maxTimeValue = maxTime;
        this.minKcalValue = 0;
        this.maxKcalValue = maxKcal;
        this.commonRecipes = new ArrayList<>(dm.getRecipes());
    }

    public RecipesController(DataManager dm, MathManager mm, double width, double height, ObservableSet<String> selectedCourse, ObservableSet<String> selectedCuisine, ObservableSet<Product> selectedIngredient, int minTimeValue, int maxTimeValue, int minKcalValue, int maxKcalValue, String searchText, int sortIndex) {
        this.dm = dm;
        this.mm = mm;
        this.width = width;
        this.height = height;
        this.selectedCourse = selectedCourse;
        this.selectedCuisine = selectedCuisine;
        this.selectedIngredient = selectedIngredient;
        dm.setMax();
        this.maxKcal = dm.getMaxKcal();
        this.maxTime = dm.getMaxTime();
        this.searchText = searchText;
        this.sortIndex = sortIndex;

        this.isFilter = false;
        this.isTimeSet = true;
        this.isKcalSet = true;
        this.addedVBox = false;
        this.minTimeValue = minTimeValue;
        this.maxTimeValue = maxTimeValue;
        this.minKcalValue = minKcalValue;
        this.maxKcalValue = maxKcalValue;
        this.commonRecipes = new ArrayList<>(dm.getRecipes());
    }

    // =================================================================================================================
    // Override methods
    // =================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Size of window
        split.setPrefWidth(width);
        split.setPrefHeight(height);
        split.heightProperty().addListener(l -> scroll.setPrefHeight(split.getHeight() - tool.getHeight()));

        // Sliders =====================================================================================================
        // Kcal sliders
        maxKcalSlider.setMax(maxKcal);
        maxKcalSlider.setValue(maxKcalValue);
        maxKcalSlider.setMajorTickUnit((double) maxKcal / 5);

        minKcalSlider.setValue(minKcalValue);
        minKcalSlider.setMax(maxKcal);
        minKcalSlider.setMajorTickUnit((double) maxKcal / 5);

        // Time sliders
        minTimeSlider.setValue(minTimeValue);
        minTimeSlider.setMax(maxTime);
        minTimeSlider.setMajorTickUnit((double) maxTime / 5);

        maxTimeSlider.setValue(maxTimeValue);
        maxTimeSlider.setMax(maxTime);
        maxTimeSlider.setMajorTickUnit((double) maxTime / 5);

        // Sliders' labels
        minKcalLabel.setText("from " + (int) minKcalSlider.getValue() + " kcal");
        maxKcalLabel.setText("to " + (int) maxKcalSlider.getValue() + " kcal");
        minTimeLabel.setText("from " + mm.countTime((int) minTimeSlider.getValue()));
        maxTimeLabel.setText("to " + mm.countTime((int) maxTimeSlider.getValue()));

        // Sliders' buttons
        kcalFilterButton.setOnAction(this::kcalFilterAction);
        timeFilterButton.setOnAction(this::timeFilterAction);

        // Sliders' listeners
        minKcalSlider.valueProperty().addListener(((observableValue, oldNumber, newNumber) -> {
            if ((int) (newNumber.doubleValue()) > (int) (maxKcalSlider.getValue())) {
                minKcalSlider.setValue(maxKcalSlider.getValue());
                newNumber = maxKcalSlider.getValue();
            }
            minKcalLabel.setText("from " + (int) (newNumber.doubleValue()) + " kcal");
        }));

        maxKcalSlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> {
            if ((int) (newNumber.doubleValue()) < (int) (minKcalSlider.getValue())) {
                maxKcalSlider.setValue(minKcalSlider.getValue());
                newNumber = minKcalSlider.getValue();
            }
            maxKcalLabel.setText("to " + (int) (newNumber.doubleValue()) + " kcal");
        });

        minTimeSlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> {
            if ((int) (newNumber.doubleValue()) > (int) (maxTimeSlider.getValue())) {
                minTimeSlider.setValue(maxTimeSlider.getValue());
                newNumber = maxTimeSlider.getValue();
            }
            System.out.println("T" + minTimeValue);

            minTimeLabel.setText("from " + mm.countTime((int) newNumber.doubleValue()));
        });

        maxTimeSlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> {
            if ((int) (newNumber.doubleValue()) < (int) (minTimeSlider.getValue())) {
                maxTimeSlider.setValue(minTimeSlider.getValue());
                newNumber = minTimeSlider.getValue();
            }
            maxTimeLabel.setText("to " + mm.countTime((int) newNumber.doubleValue()));
        });

        // ListViews ===================================================================================================
        // Course list
        ObservableList<String> courses = FXCollections.observableArrayList(dm.getCourses());
        setFilterList(courseList, selectedCourse,courses,Comparator.naturalOrder());
        courseList.setFixedCellSize(25);
        courseList.prefHeightProperty().bind(courseList.fixedCellSizeProperty().multiply(Bindings.size(courseList.getItems()).add(0.3)));

        // Cuisine list
        cuisines = FXCollections.observableArrayList(dm.getCuisines());
        setFilterList(cuisineList,selectedCuisine,cuisines,Comparator.naturalOrder());

        // Ingredient list
        ingredients = FXCollections.observableArrayList(mm.getIngredients(dm.getRecipes()));
        setFilterList(ingredientsList, selectedIngredient, ingredients, Comparator.comparing(Product::getName));

        // Search TextFields ===========================================================================================
        ingredientSearchField.setOnKeyTyped(this::searchIngredientAction);
        cuisineSearchField.setOnKeyTyped(this::searchCuisineAction);
        searchField.setText(searchText);
        searchField.setOnKeyTyped(keyEvent -> getCommonRecipes());

        // Sort ChoiceBox ==============================================================================================
        sortChoiceBox.setItems(choice);
        sortChoiceBox.setOnAction(event -> getCommonRecipes());
        sortChoiceBox.getSelectionModel().select(sortIndex);

        // ToggleButton ================================================================================================
        if (mm.getFilterIcon() != null) {
            filterToggleButton.setGraphic(mm.getFilterIcon());
        }
        filterToggleButton.setOnAction(event -> getCommonRecipes());

        // Buttons =====================================================================================================
        //back button
        if (mm.getBackIcon() != null) {
            backButton.setGraphic(mm.getBackIcon());
        } else
            backButton.setText("Back");
        backButton.setOnAction(this::backAction);

        //home button
        if (mm.getHomeIcon() != null) {
            homeButton.setGraphic(mm.getHomeIcon());
        }
        homeButton.setOnAction(this::homeAction);

        //recipes button
        if (mm.getRecipesIcon() != null) {
            recipesButton.setGraphic(mm.getRecipesIcon());
        }
        recipesButton.setOnAction(this::recipesAction);

        //add button
        if (mm.getAddIcon() != null) {
            addButton.setGraphic(mm.getAddIcon());
        }
        addButton.setOnAction(this::addAction);

        cleanButton.setOnAction(this::cleanAction);

        // ScrollPane ====================================================================================================
        scroll.widthProperty().addListener(observable -> getCommonRecipes());
        scroll.setMinWidth(200);
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================
    private <T> void setFilterList(ListView<T> itemsList, ObservableSet<T> selectedItems, ObservableList<T> items, Comparator<T> comparator) {
        items.sort(comparator);
        itemsList.setItems(items);
        itemsList.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty property = new SimpleBooleanProperty();
            property.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
                getCommonRecipes();
            });
            property.set(selectedItems.contains(item));
            selectedItems.addListener((SetChangeListener.Change<? extends T> c) -> property.set(selectedItems.contains(item)));

            return property;
        }));
    }

    private void setList(ListView<Recipe> recipeList) {
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

        recipeList.setOnMouseClicked(event -> click(recipeList.getSelectionModel().getSelectedItem(), event));
    }

    private void setPane(Comparator<Recipe> comparator, List<Recipe> shownRecipes) {
        filterAnchor.setMinHeight(filterVBox.getHeight() + 50);
        listHBox.getChildren().clear();
        if (filterToggleButton.isSelected()) {
            border.setLeft(filterScroll);
        } else {
            border.setLeft(null);
        }

        listAmount = (int) ((scroll.getWidth() + 12) / 188);
        if (listAmount<1){
            listAmount = 1;
        }

        ListView<Recipe>[] recipeList = new ListView[listAmount];
        ObservableList<Recipe>[] recipes = new ObservableList[listAmount];

        for (int i = 0; i < listAmount; i++) {
            recipes[i] = FXCollections.observableArrayList();
            recipeList[i] = new ListView<>();
        }

        if (comparator != null) {
            shownRecipes.sort(comparator);
        }
        setRecipes(shownRecipes, recipes);


        for (int i = 0; i < listAmount; i++) {
            recipeList[i].setItems(recipes[i]);
            setList(recipeList[i]);

            anchor.setPrefWidth(scroll.getWidth() - 15);
            listHBox.setPrefWidth(scroll.getWidth() - 15);
            listHBox.getChildren().add(recipeList[i]);
        }
//        System.out.println(filterAnchor.getWidth());

    }

    private void setRecipes(List<Recipe> dmList, List<Recipe>[] recipes) {
        for (int i=0; i<dmList.size();i++){
            recipes[i%this.listAmount].add(dmList.get(i));
        }
    }

    private void click(Recipe selectedRecipe, MouseEvent mouseEvent) {
//        System.out.println("click" + selectedRecipe);
        if(selectedRecipe != null) {
            RecipeController controller = new RecipeController(dm, mm, selectedRecipe, split.getWidth(), split.getHeight(), selectedCourse, selectedCuisine, selectedIngredient, minTimeValue, maxTimeValue, minKcalValue, maxKcalValue, commonRecipes, searchField.getText(), sortChoiceBox.getSelectionModel().getSelectedIndex());
            mm.show(getClass(),"recipe-view.fxml",controller,mouseEvent);
        }
    }

    // Filter, Search TextField and Sort ChoiceBox Action ==============================================================
    private void cleanAction(ActionEvent event) {
        selectedCuisine.clear();
        selectedCourse.clear();
        selectedIngredient.clear();
        minTimeSlider.setValue(0);
        maxTimeSlider.setValue(maxTime);
        minKcalSlider.setValue(0);
        maxKcalSlider.setValue(maxKcal);
        isKcalSet = false;
        isTimeSet = false;
        getCommonRecipes();
    } // Clean Button Action

    private void setLabel(){
        courseLabel = new Label();
        courseLabel.setWrapText(true);
        cuisineLabel = new Label();
        cuisineLabel.setWrapText(true);
        ingredientLabel = new Label();
        ingredientLabel.setWrapText(true);
        timeLabel = new Label();
        timeLabel.setWrapText(true);
        kcalLabel = new Label();
        kcalLabel.setWrapText(true);
    }

    private void filterTextAction() {
        if (!addedVBox) {
            vBox = new VBox();
            setLabel();
            addedVBox = true;
            filterVBox.getChildren().add(1, vBox);
        }
        else {
            vBox.getChildren().clear();
        }
        if (selectedCourse.size() > 0) {
            courseLabel.setText("Course: " + selectedCourse);
            vBox.getChildren().add(courseLabel);
        }
        if (selectedCuisine.size() > 0) {
            cuisineLabel.setText("Cuisine: " + selectedCuisine);
            vBox.getChildren().add(cuisineLabel);
        }
        if (selectedIngredient.size() > 0) {
            ingredientLabel.setText("Ingredients: " + selectedIngredient);
            vBox.getChildren().add(ingredientLabel);
        }
        if ((int) minTimeSlider.getValue() > 0 || (int) maxTimeSlider.getValue() < maxTime) {
            timeLabel.setText("Time: " + minTimeLabel.getText() + " " + maxTimeLabel.getText());
            vBox.getChildren().add(timeLabel);
        }
        if ((int) minKcalSlider.getValue() > 0 || (int) maxKcalSlider.getValue() < maxKcal) {
            kcalLabel.setText("Kcal: " + minKcalLabel.getText() + " " + maxKcalLabel.getText());
            vBox.getChildren().add(kcalLabel);
        }
    }

    private void getCommonRecipes() {
        commonRecipes = new ArrayList<>(dm.getRecipes());
        isFilter = false;
        if (selectedCourse.size() > 0) {
            isFilter = true;
            commonRecipes.retainAll(dm.getCourseFilteredRecipes(selectedCourse));
        }
        if (selectedCuisine.size() > 0) {
            isFilter = true;
            commonRecipes.retainAll(dm.getCuisineFilteredRecipes(selectedCuisine));
        }
        if (selectedIngredient.size() > 0) {
            isFilter = true;
            commonRecipes.retainAll(dm.getIngredientFilteredRecipes(selectedIngredient));
        }
        if (isTimeSet) {
            if (minTimeValue > 0 || maxTimeValue < maxTime) {
                isFilter = true;
                commonRecipes.retainAll(dm.getTimeFilteredRecipes(minTimeValue, maxTimeValue));
            }
            else {
                isTimeSet = false;
            }
        }
        if (isKcalSet) {
            if (minKcalValue > 0 || maxKcalValue < maxKcal) {
                isFilter = true;
                commonRecipes.retainAll(dm.getKcalFilteredRecipes(minKcalValue, maxKcalValue));
            }
            else {
                isKcalSet = false;
            }
        }

        cleanButton.setVisible(isFilter);

        commonRecipes = dm.getSearchedRecipes(commonRecipes, searchField.getText());

        filterTextAction();

        if (sortChoiceBox.getSelectionModel().getSelectedIndex() == 1) {
            setPane(Comparator.comparing(Recipe::getName), commonRecipes);

        }
        else if (sortChoiceBox.getSelectionModel().getSelectedIndex() == 2){
            setPane(Comparator.comparing(Recipe::getName).reversed(), commonRecipes);
        }
        else {
            setPane(null, commonRecipes);
        }
    }

    // Search TextField Action =========================================================================================
    private void searchCuisineAction(KeyEvent keyEvent) {
        ObservableList<String> searchedCuisine = FXCollections.observableArrayList(mm.getSearchedCuisine(cuisines, cuisineSearchField.getText()));
        searchedCuisine.sort(Comparator.naturalOrder());
        cuisineList.setItems(searchedCuisine);
    }

    private void searchIngredientAction(KeyEvent keyEvent) {
        ObservableList<Product> searchedIngredients = FXCollections.observableArrayList(mm.getSearchedIngredients(ingredients, ingredientSearchField.getText()));
        searchedIngredients.sort(Comparator.comparing(Product::getName));
        ingredientsList.setItems(searchedIngredients);
    }

    // Filter Buttons Action ===========================================================================================
    private void kcalFilterAction(ActionEvent event) {
        isKcalSet = true;
        minKcalValue = (int) minKcalSlider.getValue();
        maxKcalValue = (int) maxKcalSlider.getValue();
        getCommonRecipes();
    }

    private void timeFilterAction(ActionEvent event) {
        isTimeSet = true;
        minTimeValue = (int) minTimeSlider.getValue();
        maxTimeValue = (int) maxTimeSlider.getValue();
        getCommonRecipes();
    }

    // Menu's Buttons Actions ==========================================================================================
    private void addAction(ActionEvent event) {
        AddController controller = new AddController(dm, mm, split.getWidth(), split.getHeight());
        mm.show(getClass(),"add-view.fxml",controller,event);
    } // Add Recipe Button Action

    private void homeAction(ActionEvent event) {
//        backAction(event);
    } // Home Button Action

    private void backAction(ActionEvent event) {
//        mm.show(getClass(), "recipes-view.fxml", event);
    } // Back Button Action

    private void recipesAction(ActionEvent event) {
        RecipesController controller = new RecipesController(dm, mm,split.getWidth(),split.getHeight());
        mm.show(getClass(),"recipes-view.fxml",controller,event);
    } // Recipes Button Action
}
