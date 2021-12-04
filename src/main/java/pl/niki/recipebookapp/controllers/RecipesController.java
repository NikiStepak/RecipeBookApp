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
import javafx.scene.layout.*;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;
import java.net.URL;
import java.util.*;

public class RecipesController implements Initializable {
    public Button backButton, homeButton, recipesButton, addButton, timeFilterButton, kcalFilterButton;
    public ToggleButton filterToggleButton;
    public TextField searchField, ingredientSearchField, cuisineSearchField;
    public ChoiceBox<String> sortChoiceBox;
    public ListView<Product> ingredientsList;
    public ListView<String> courseList, cuisineList;
    public Slider minTimeSlider, maxTimeSlider, minKcalSlider, maxKcalSlider;
    public Label minTimeLabel, maxTimeLabel, maxKcalLabel, minKcalLabel;
    public ScrollPane scroll, filterScroll;
    public AnchorPane anchor, filterAnchor;
    public HBox listHBox;
    public VBox filterVBox;
    public SplitPane split;
    public ToolBar tool;
    public BorderPane border;

    private final DataManager dm;
    private final MathManager mm;
    private int listAmount;
    private final double width, height;
    private ObservableList<String> courses, cuisines;
    private final ObservableList<String> choice = FXCollections.observableArrayList("Sort by ...", "A -> Z", "Z -> A");
    private ObservableSet<String> selectedCourse = FXCollections.observableSet();
    private ObservableList<Product> ingredients;
    private ObservableSet<String> selectedCuisine = FXCollections.observableSet();
    private ObservableSet<Product> selectedIngredient = FXCollections.observableSet();
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
        dm.setMax();
        minKcalLabel.setText("from " + (int) minKcalSlider.getValue() + " kcal");
        int maxKcal = dm.getMaxKcal();
        maxKcalSlider.setMax(maxKcal);
        maxKcalSlider.setValue(maxKcal);
        minKcalSlider.setMax(maxKcal);
        minKcalSlider.setMajorTickUnit(maxKcal/5);
        maxKcalSlider.setMajorTickUnit(maxKcal/5);
        maxKcalLabel.setText("to " + (int) maxKcalSlider.getValue() + " kcal");

        minKcalSlider.valueProperty().addListener(((observableValue, oldNumber, newNumber) -> {
            if ((int) (newNumber.doubleValue()) > (int) (maxKcalSlider.getValue())){
                minKcalSlider.setValue(maxKcalSlider.getValue());
                newNumber = maxKcalSlider.getValue();
            }
            minKcalLabel.setText("from " + (int) (newNumber.doubleValue()) + " kcal");
        }));

        maxKcalSlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> {
            if ((int)(newNumber.doubleValue()) < (int) (minKcalSlider.getValue())){
                maxKcalSlider.setValue(minKcalSlider.getValue());
                newNumber = minKcalSlider.getValue();
            }
            maxKcalLabel.setText("to " + (int) (newNumber.doubleValue()) + " kcal");
        });

        kcalFilterButton.setOnAction(this::kcalFilterButton);

        minTimeLabel.setText("from " + mm.countTime((int) minTimeSlider.getValue()));
        int maxTime = dm.getMaxTime();
        maxTimeSlider.setMax(maxTime);
        maxTimeSlider.setValue(maxTime);
        minTimeSlider.setMax(maxTime);
        minTimeSlider.setMajorTickUnit(maxTime/5);
        maxTimeSlider.setMajorTickUnit(maxTime/5);
        maxTimeLabel.setText("to " + mm.countTime((int) maxTimeSlider.getValue()));

        minTimeSlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> {
            if ((int) (newNumber.doubleValue()) > (int) (maxTimeSlider.getValue())) {
                minTimeSlider.setValue(maxTimeSlider.getValue());
                newNumber = maxTimeSlider.getValue();
            }
            minTimeLabel.setText("from " + mm.countTime((int) newNumber.doubleValue()));
        });

        timeFilterButton.setOnAction(this::timeFilterAction);

        maxTimeSlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> {
            if ((int)(newNumber.doubleValue()) < (int) (minTimeSlider.getValue())){
                maxTimeSlider.setValue(minTimeSlider.getValue());
                newNumber = minTimeSlider.getValue();
            }
            maxTimeLabel.setText("to " + mm.countTime((int) newNumber.doubleValue()));
        });

        courses = FXCollections.observableArrayList(dm.getCourses());
        courses.sort(Comparator.naturalOrder());
        courseList.setItems(courses);
        courseList.setCellFactory(CheckBoxListCell.forListView(course -> {
            BooleanProperty property = new SimpleBooleanProperty();
            property.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected){
                    selectedCourse.add(course);
                }
                else {
                    selectedCourse.remove(course);
                }
                courseFilterAction();
            });
            property.set(selectedCourse.contains(course));
            selectedCourse.addListener((SetChangeListener.Change<? extends String> c) -> property.set(selectedCourse.contains(course)));
            return property;
        }));
        courseList.setFixedCellSize(25);
        courseList.prefHeightProperty().bind(courseList.fixedCellSizeProperty().multiply(Bindings.size(courseList.getItems()).add(0.3)));

        cuisines = FXCollections.observableArrayList(dm.getCuisines());
        cuisines.sort(Comparator.naturalOrder());
        cuisineList.setItems(cuisines);
        cuisineList.setCellFactory(CheckBoxListCell.forListView(cuisine->{
            BooleanProperty property = new SimpleBooleanProperty();
            property.addListener((obs,wasSelected,isNowSelected)->{
                if (isNowSelected){
                    selectedCuisine.add(cuisine);
                }
                else {
                    selectedCuisine.remove(cuisine);
                }
                cuisineFilterAction();
            });
            property.set(selectedCuisine.contains(cuisine));
            selectedCuisine.addListener((SetChangeListener.Change<? extends String> c) -> property.set(selectedCuisine.contains(cuisine)));
            return property;
        }));


        ingredients = FXCollections.observableArrayList(mm.getIngredients(dm.getRecipes()));
        ingredients.sort(Comparator.comparing(Product::getName));
        ingredientsList.setItems(ingredients);
        ingredientsList.setCellFactory(CheckBoxListCell.forListView(product -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected){
                    selectedIngredient.add(product);
                }
                else {
                    selectedIngredient.remove(product);
                }
                ingredientsFilterAction();
            });
            observable.set(selectedIngredient.contains(product));
            selectedIngredient.addListener((SetChangeListener.Change<? extends Product> c)->observable.set(selectedIngredient.contains(product)));
            return observable;
        }));

        ingredientSearchField.setOnKeyTyped(this::searchIngredientAction);
        cuisineSearchField.setOnKeyTyped(this::searchCuisineAction);

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

    private void searchCuisineAction(KeyEvent keyEvent) {
        ObservableList<String> searchedCuisine = FXCollections.observableArrayList(mm.getSearchedCuisine(cuisines, cuisineSearchField.getText()));
        searchedCuisine.sort(Comparator.naturalOrder());
        cuisineList.setItems(searchedCuisine);
    }

    private boolean timeSet = false, kcalSet = false;

    private void kcalFilterButton(ActionEvent event) {
        kcalSet = true;
        getCommonRecipes();
    }

    public Label courseLabel, cuisineLabel, ingredientLabel, timeLabel, kcalLabel;
    private boolean addedVBox = false;
    public VBox vBox;

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
        if (timeSet) {
            timeLabel.setText("Time: " + minTimeLabel.getText() + " " + maxTimeLabel.getText());
            vBox.getChildren().add(timeLabel);
        }
        if (kcalSet) {
            kcalLabel.setText("Kcal: " + minKcalLabel.getText() + " " + maxKcalLabel.getText());
            vBox.getChildren().add(kcalLabel);
        }
    }

    private void timeFilterAction(ActionEvent event) {
        timeSet = true;
        getCommonRecipes();
    }

    private void courseFilterAction() {
        getCommonRecipes();
    }

    private void cuisineFilterAction() {
        getCommonRecipes();
    }

    private void ingredientsFilterAction() {
        getCommonRecipes();
    }


    private void getCommonRecipes() {
        List<Recipe> commonRecipes = new ArrayList<>(dm.getRecipes());
        if (selectedCourse.size() > 0) {
            commonRecipes.retainAll(dm.getCourseFilteredRecipes(selectedCourse));
        }
        if (selectedCuisine.size() > 0) {
            commonRecipes.retainAll(dm.getCuisineFilteredRecipes(selectedCuisine));
        }
        if (selectedIngredient.size() > 0) {
            commonRecipes.retainAll(dm.getIngredientFilteredRecipes(selectedIngredient));
        }
        if (timeSet) {
            commonRecipes.retainAll(dm.getTimeFilteredRecipes((int) (minTimeSlider.getValue()), (int) maxTimeSlider.getValue()));
        }
        if (kcalSet) {
            commonRecipes.retainAll(dm.getKcalFilteredRecipes(minKcalSlider.getValue(), maxKcalSlider.getValue()));
        }
        filterTextAction();
        setPane(null, false, commonRecipes);
    }

    private void filterAction(ActionEvent event) {
        setPane(null, false, dm.getRecipes());
    }

    private void setPane(Comparator<Recipe> comparator, boolean sort, List<Recipe> shownRecipes){
        filterAnchor.setMinHeight(filterVBox.getHeight()+50);
        listHBox.getChildren().clear();
        if (filterToggleButton.isSelected()) {
            border.setLeft(filterScroll);
        }
        else {
            border.setLeft(null);
        }

        listAmount = (int) ((scroll.getWidth() + 12) / 188);

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
            setRecipes(shownRecipes, recipes);
        }

        for (int i = 0; i < listAmount; i++) {
            recipeList[i].setItems(recipes[i]);
            setList(recipeList[i], i);

            anchor.setPrefWidth(scroll.getWidth() - 15);
            listHBox.setPrefWidth(scroll.getWidth() - 15);
            listHBox.getChildren().add(recipeList[i]);
        }
//        System.out.println(filterAnchor.getWidth());

    }

    private void sortAction(Event event) {
        if (sortChoiceBox.getSelectionModel().getSelectedIndex() == 1) {
            setPane(Comparator.comparing(Recipe::getName), true, null);
        }
        else if (sortChoiceBox.getSelectionModel().getSelectedIndex() == 2){
            setPane(Comparator.comparing(Recipe::getName).reversed(), true, null);
        }
    }

    private void searchIngredientAction(KeyEvent keyEvent) {
        ObservableList<Product> searchedIngredients = FXCollections.observableArrayList(mm.getSearchedIngredients(ingredients, ingredientSearchField.getText()));
        searchedIngredients.sort(Comparator.comparing(Product::getName));
        ingredientsList.setItems(searchedIngredients);
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
            int selectedRecipe = recipeList.getSelectionModel().getSelectedItem().getId();
            click(selectedRecipe, event);
        });
    }

    private void setRecipes(List<Recipe> dmList, List<Recipe>[] recipes) {
        for (int i=0; i<dmList.size();i++){
            recipes[i%this.listAmount].add(dmList.get(i));
        }
    }

    public void click(int selectedRecipe, MouseEvent mouseEvent) {
        System.out.println("click" + selectedRecipe);
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
