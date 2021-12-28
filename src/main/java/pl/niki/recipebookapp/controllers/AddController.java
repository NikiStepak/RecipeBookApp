package pl.niki.recipebookapp.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Instruction;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    // =================================================================================================================
    // Public fields - elements of the add-view.fxml
    // =================================================================================================================
    public Button backButton, addRecipeButton, homeButton, recipesButton, addButton, refreshButton;
    public ImageView recipeImage;
    public ListView<Ingredient> ingredientsList;
    public TableView<Instruction> instructionTable;
    public TableColumn<Instruction, String> descriptionColumn;
    public TableColumn<Instruction,Image> imageColumn;
    public TextField recipeNameField, minTimeField, servingsField, cuisineField, hTimeField, websiteField;
    public TextArea descriptionArea;
    public Label kcalLabel;
    public ChoiceBox<String> courseChoiceBox;
    public SplitPane split;
    public ScrollPane scroll;
    public ToolBar tool;

    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private final double width, height;
    private final boolean edit;

    private DataManager dm;
    private MathManager mm;
    private ObservableList<Ingredient> ingredients;
    private ObservableList<Instruction> instructions;
    private Recipe editRecipe;
    private Product selectedProduct;

    // =================================================================================================================
    // Controllers
    // =================================================================================================================
    public AddController(DataManager dm, MathManager mm, double width, double height) {
        this.width = width;
        this.height = height;
        this.edit = false;

        this.dm = dm;
        this.mm = mm;
        mm.newRecipe(dm.getRecipes().size());
        this.selectedProduct = null;
    }

    public AddController(DataManager dm, MathManager mm, Recipe recipe, double width, double height) {
        this.width = width;
        this.height = height;
        this.edit = true;

        this.dm = dm;
        this.mm = mm;
        this.editRecipe = recipe;
        mm.setNewRecipe(recipe);
        this.selectedProduct = null;
    }

    // =================================================================================================================
    // Override methods
    // =================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // size of window
        split.heightProperty().addListener(l -> scroll.setPrefHeight(split.getHeight() - tool.getHeight()));
        split.setPrefHeight(height);
        split.setPrefWidth(width);

        // ChoiceBox ===================================================================================================
        ObservableList<String> course = FXCollections.observableArrayList(dm.getCourses());
        courseChoiceBox.setItems(course);
        if (edit) {
            courseChoiceBox.getSelectionModel().select(mm.getNewRecipe().getCourse());
        }

        // ImageView ===================================================================================================
        if (edit) {
            recipeImage.setImage(mm.getNewRecipe().getImage());
            Rectangle rectangle = new Rectangle(recipeImage.getFitWidth(), recipeImage.getFitHeight());
            rectangle.setArcHeight(25);
            rectangle.setArcWidth(25);
            recipeImage.setClip(rectangle);
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage image = recipeImage.snapshot(parameters, null);
            recipeImage.setClip(null);
            recipeImage.setEffect(new DropShadow(30, Color.WHITE));
            recipeImage.setImage(image);

        } else {
            recipeImage.setImage(mm.getAddImage());
            recipeImage.setEffect(new InnerShadow(100,Color.WHITE));
        }
        recipeImage.setOnMouseClicked(event -> imageAction(recipeImage));

        // Label, Fields and Area ======================================================================================
        if (edit) {
            kcalLabel.setText(mm.round_double(mm.getNewRecipe().getKcal()) + " kcal");
            recipeNameField.setText(mm.getNewRecipe().getName());
            int hours = mm.getNewRecipe().getTime() / 60;
            int minutes = mm.getNewRecipe().getTime() - (hours * 60);
            hTimeField.setText(String.valueOf(hours));
            minTimeField.setText(String.valueOf(minutes));
            servingsField.setText(String.valueOf(mm.getNewRecipe().getAmount()));
            descriptionArea.setText(mm.getNewRecipe().getDescription());
            cuisineField.setText(mm.getNewRecipe().getCuisine());
            websiteField.setText(mm.getNewRecipe().getUrl());
        }

        // ListView ====================================================================================================
        ingredients = FXCollections.observableArrayList(mm.getNewRecipe().getIngredients());
        ingredientsList.setItems(ingredients);
        ingredientsList.getItems().add(new Ingredient(null,0, 0));
        ingredientsList.setCellFactory(i -> new ListCell<>() {

            @Override
            protected void updateItem(Ingredient ingredient, boolean b) {
                super.updateItem(ingredient, b);

                if (b || ingredient == null) {
                    setGraphic(null);
                }
                else {
                    final HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    TextField amountField = new TextField();
                    amountField.setMaxWidth(40);

                    Label label = new Label(" g ");

                    Button ingredientButton = new Button();
                    Tooltip ingredientTooltip = new Tooltip();

                    hBox.getChildren().add(amountField);
                    hBox.getChildren().add(label);

                    if (ingredient.getProduct() == null) {
                        selectedProduct = null;
                        // add Button
                        if (mm.getDoneIcon(false) != null) {
                            ingredientButton.setGraphic(mm.getDoneIcon(false));
                        }
                        ingredientButton.setOnAction(event -> addIngredientAction(amountField.getText(), ingredient));
                        ingredientButton.setMaxWidth(40);
                        ingredientTooltip.setText("Add Ingredient");

                        ComboBox<Product> productComboBox = new ComboBox<>();
                        ObservableList<Product> products = FXCollections.observableArrayList(mm.getNotAddedProducts(dm.getProducts()));
                        productComboBox.setItems(products);
                        if (productComboBox.getItems().isEmpty()){
                            productComboBox.getItems().add(new Product(productComboBox.getEditor().getText(), 0, 100, true));
                        }
                        productComboBox.setPromptText("Select product");
                        productComboBox.setCellFactory(cell -> new ListCell<>() {
                            @Override
                            protected void updateItem(Product product, boolean b) {
                                super.updateItem(product, b);
                                if (b || product == null) {
                                    setText(null);
                                    setGraphic(null);
                                } else if (product.isNewNull()) {
                                    if (mm.getAddIcon() != null) {
                                        setGraphic(mm.getAddIcon());
                                    } else {
                                        setGraphic(null);
                                    }
                                    setText("Add Product");
                                } else {
                                    setGraphic(null);
                                    setText(product.getName());
                                }
                            }
                        });

                        productComboBox.setOnAction(event -> productComboBoxAction(productComboBox));
                        productComboBox.getEditor().setOnMousePressed(mouseEvent -> showComboBox(productComboBox));
                        productComboBox.getEditor().setOnKeyTyped(keyEvent -> searchComboBoxEditor(productComboBox, products));

                        productComboBox.setEditable(true);
                        productComboBox.setMaxWidth(130);

                        hBox.getChildren().add(productComboBox);
                    }
                    else {
                        // delete Button
                        if (mm.getDeleteIcon(true) != null) {
                            ingredientButton.setGraphic(mm.getDeleteIcon(true));
                        } else {
                            ingredientButton.setText("Delete");
                        }
                        ingredientButton.setOnAction(event -> deleteIngredientAction(ingredient));
                        ingredientTooltip.setText("Delete Ingredient");

                        amountField.setText(String.valueOf(ingredient.getAmount()));
                        amountField.textProperty().addListener( (observableValue, oldText, newText) -> {
                            if (!newText.equals(oldText)) {
                                ingredient.setAmount(Integer.parseInt(newText));
                                mm.getNewRecipe().countKcal();
                            }
                        });

                        label.setText(label.getText() + ingredient.getProduct().getName());
                    }

                    ingredientButton.setTooltip(ingredientTooltip);
                    hBox.getChildren().add(ingredientButton);
                    setGraphic(hBox);
                }
            }
        });

        // height of list
        ingredientsList.setFixedCellSize(35);
        ingredientsList.prefHeightProperty().bind(ingredientsList.fixedCellSizeProperty().multiply(Bindings.size(ingredientsList.getItems()).add(0.4)));

        // TableView ===================================================================================================
        instructions = FXCollections.observableArrayList(mm.getNewRecipe().getInstructions());
        instructionTable.setItems(instructions);
        instructionTable.getItems().add(new Instruction(null, instructionTable.getItems().size() + 1, null));

        // Description Column - 1st Column
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionText"));
        descriptionColumn.setCellFactory(param -> {
            TextArea instructionArea = new TextArea();

            TableCell<Instruction, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String description, boolean b) {
                    super.updateItem(description, b);
                    if (b) {
                        setGraphic(null);
                    } else {
                        Label stepLabel = new Label(getIndex() + 1 + ".  ");
                        instructionArea.setWrapText(true);

                        if (description == null) {
                            instructionArea.textProperty().addListener( (observableValue, oldText, newText) -> {
                                if (!newText.equals("") && !newText.equals(oldText)) {
                                    instructionTable.getItems().get(getIndex()).setDescriptionText(newText);
                                }
                            });
                            instructionArea.setText("");
                        } else {
                            instructionArea.setText(instructionTable.getItems().get(getIndex()).getDescriptionText());
                            instructionArea.textProperty().addListener((observableValue, oldText, newText) -> {
                                if (getIndex() < mm.getNewRecipe().getInstructions().size() && !newText.equals(mm.getNewRecipe().getInstructions().get(getIndex()).getDescriptionText())) {
                                    instructionTable.getItems().get(getIndex()).setDescriptionText(newText);
                                }
                            });
                        }
                        instructionArea.setMaxWidth(descriptionColumn.getWidth() - 40);

                        HBox hBox = new HBox();

                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(stepLabel);
                        hBox.getChildren().add(instructionArea);
                        setGraphic(hBox);
                        setPrefHeight(Control.USE_COMPUTED_SIZE);
                    }
                }
            };

            descriptionColumn.widthProperty().addListener( (observableValue, oldWidth, newWidth) -> {
                instructionArea.setMinWidth(descriptionColumn.getWidth() - 40);
                instructionArea.setPrefWidth(descriptionColumn.getWidth() - 40);
                instructionArea.setMaxWidth(descriptionColumn.getWidth() - 40);
            });

            return cell;
        });

        // Image Column - 2nd Column
        imageColumn.setPrefWidth(250);
        imageColumn.setMaxWidth(250);
        imageColumn.setMinWidth(250);
        imageColumn.setCellFactory(cell ->  new TableCell<>() {
            @Override
            protected void updateItem(Image image, boolean b) {
                super.updateItem(image, b);
                if (b) {
                    setGraphic(null);
                } else {
                    // set ImageView
                    ImageView descriptionImage = new ImageView();
                    descriptionImage.setFitHeight(150);
                    descriptionImage.setFitWidth(200);
                    if (image != null) {
                        descriptionImage.setImage(image);
                    } else {
                        descriptionImage.setImage(mm.getAddImage());
                    }

                    // set Button
                    Button instructionButton = new Button();
                    Tooltip tooltip = new Tooltip();
                    if (instructionTable.getItems().get(getIndex()).getDescriptionText() == null) {
                        if (mm.getDoneIcon(false) != null) { // add Button
                            instructionButton.setGraphic(mm.getDoneIcon(false));
                        }
                        instructionButton.setOnAction(event -> addInstructionAction(instructionTable.getItems().get(getIndex())));
                        tooltip.setText("Add Instruction");
                    } else {
                        if (mm.getDeleteIcon(false) != null) { // delete Button
                            instructionButton.setGraphic(mm.getDeleteIcon(false));
                        }
                        instructionButton.setOnAction(event -> deleteInstructionAction(getIndex()));
                        tooltip.setText("Delete Instruction");
                    }
                    instructionButton.setTooltip(tooltip);

                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.getChildren().addAll(descriptionImage, instructionButton);
                    setGraphic(hBox);
                    setOnMouseClicked(event -> {
                        ContextMenu contextMenu = instructionImageAction(event, descriptionImage, getIndex());
                        if (contextMenu != null) {
                            setContextMenu(contextMenu);
                        }
                        if (descriptionImage.getImage() != mm.getAddImage()) {
                            instructionTable.getItems().get(getIndex()).setImage(descriptionImage.getImage());
                        }
                    });
                }
            }
        });
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        // height of table
        instructionTable.setFixedCellSize(155);
        instructionTable.prefHeightProperty().bind(instructionTable.fixedCellSizeProperty().multiply(Bindings.size(instructionTable.getItems()).add(0.2)));

        // Buttons =====================================================================================================
        // refresh Button
        refreshButton.setGraphic(mm.getRefreshIcon());
        refreshButton.setOnAction(this::refreshAction);

        // back button
        if (mm.getBackIcon() != null) {
            backButton.setGraphic(mm.getBackIcon());
        } else
            backButton.setText("Back");
        backButton.setOnAction(this::backAction);

        // home button
        if (mm.getHomeIcon() != null) {
            homeButton.setGraphic(mm.getHomeIcon());
        }
        homeButton.setOnAction(this::homeAction);

        // recipes button
        if (mm.getRecipesIcon() != null) {
            recipesButton.setGraphic(mm.getRecipesIcon());
        }
        recipesButton.setOnAction(this::recipesAction);

        // add recipe button
        if (mm.getDoneIcon(false) != null) {
            addRecipeButton.setGraphic(mm.getDoneIcon(false));
        } else {
            addRecipeButton.setText("Add Recipe");
        }
        addRecipeButton.setOnAction(this::addRecipeAction);

        // add button
        if (mm.getAddIcon() != null) {
            addButton.setGraphic(mm.getAddIcon());
        }
        addButton.setOnAction(this::addAction);
    }

    private void refreshAction(ActionEvent event) {
        AddController controller;
        if (edit){
            controller = new AddController(dm, mm, editRecipe, split.getWidth(), split.getHeight());
        }
        else {
            controller = new AddController(dm, mm, split.getWidth(), split.getHeight());
        }
        mm.show(getClass(),"add-view.fxml",controller,event);
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================
    private void imageAction(ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        try {
            if (file!=null) {
                String type = Files.probeContentType(file.toPath());
                type = type.split("/")[0];
                if (type.equals("image")){
                    Image newImage = new Image(file.toURI().toString());
                    imageView.setImage(newImage);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addRecipeAction(ActionEvent event) {
        if (recipeNameField.getText().length() > 2){
            if(minTimeField.getText().length() > 0 || hTimeField.getText().length() > 0){
                if (servingsField.getText().length() > 0){
                    if (descriptionArea.getText().length() > 2){
                        if (courseChoiceBox.getSelectionModel().getSelectedIndex()>=0) {
                            if (mm.getNewRecipe().getIngredients().size() > 0) {
                                if (mm.getNewRecipe().getInstructions().size() > 0) {
                                    if (recipeImage.getImage() != null) {
                                        String cuisine = cuisineField.getText();
                                        if (cuisine.length() < 1) {
                                            cuisine = "-";
                                        } else {
                                            dm.addCuisine(cuisine);
                                        }
                                        String url = null;
                                        if (websiteField.getText() != null && websiteField.getText().length() > 0){
                                            url = websiteField.getText();
                                        }
//                                        Double.parseDouble(this.kcalLabel.getText())
                                        mm.setNewRecipe(2000, recipeNameField.getText(), Integer.parseInt(hTimeField.getText()) * 60 + Integer.parseInt(minTimeField.getText()), Integer.parseInt(servingsField.getText()), descriptionArea.getText(), recipeImage.getImage(), cuisine, courseChoiceBox.getSelectionModel().getSelectedItem(), url);
                                        mm.getNewRecipe().checkInstructionsImage();
                                        RecipeController controller;
                                        if (edit) {
                                            dm.editRecipe(this.editRecipe, mm.getNewRecipe());
                                        } else {
                                            dm.addRecipe(mm.getNewRecipe());
                                        }
                                        controller = new RecipeController(dm, mm, mm.getNewRecipe(), split.getWidth(), split.getHeight());
                                        mm.show(getClass(), "recipe-view.fxml", controller, event);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } // Add (this) Recipe Button Action

    // Product ComboBox functions ======================================================================================
    private void productComboBoxAction(ComboBox<Product> comboBox) {
        if (comboBox.getSelectionModel().getSelectedIndex() >= 0 && comboBox.getSelectionModel().getSelectedItem() != null && comboBox.getSelectionModel().getSelectedItem().isNewNull()) {
            Product newProduct = comboBox.getSelectionModel().getSelectedItem();
            AddProductController controller = new AddProductController(dm, mm, newProduct);
            mm.showAndWait(getClass(), "addProduct-view.fxml", controller, comboBox.getScene().getWindow(), "Add New Product");
            mm = controller.getMm();
            dm = controller.getDm();
            if (controller.isAdded()) {
                comboBox.getSelectionModel().select(newProduct);
            } else {
                comboBox.getItems().add(null);
                comboBox.valueProperty().set(null);
            }
        } else if ((comboBox.getSelectionModel().getSelectedIndex() >= 0 && comboBox.getSelectionModel().getSelectedItem() != null)) {
            setProduct(comboBox.getSelectionModel().getSelectedItem());
        } else if (comboBox.getSelectionModel().getSelectedItem() == null || !(comboBox.getEditor().getText().length() > 0)) {
            selectedProduct = null;
        } else if (comboBox.getEditor().getText().length() > 0) {
            selectedProduct = dm.getProducts().stream().filter(product -> comboBox.getEditor().getText().equals(product.getName())).findAny().orElse(null);
            System.out.println(selectedProduct);
        }
    }

    private void searchComboBoxEditor(ComboBox<Product> comboBox, ObservableList<Product> allObjects) {
        showComboBox(comboBox);

        ObservableList<Product> list = FXCollections.observableArrayList();
        for (Product product : allObjects) {
            if (product != null && product.getName().toLowerCase().contains(comboBox.getEditor().getText().toLowerCase().trim())) {
                list.add(product);
            }
        }
        if (list.isEmpty()) {
            list.add(new Product(comboBox.getEditor().getText(), 0, 100, true));
        }
        comboBox.setItems(list);
    }

    private void showComboBox(ComboBox<Product> comboBox) {
        if (!comboBox.isShowing()) {
            comboBox.show();
        }
    }

    private void setProduct(Product selectedItem) {
        if (dm.getProducts().contains(selectedItem)){
            selectedProduct = selectedItem;
        }
        else {
            selectedProduct = null;
        }
    }

    // Ingredient List functions =======================================================================================
    private void setList(){
        ingredients = FXCollections.observableArrayList(mm.getNewRecipe().getIngredients());
        ingredientsList.setItems(ingredients);
        ingredientsList.getItems().add(new Ingredient(null, 0, 0));
        ingredientsList.setFixedCellSize(35);
        ingredientsList.prefHeightProperty().bind(ingredientsList.fixedCellSizeProperty().multiply(Bindings.size(ingredientsList.getItems()).add(0.4)));
        kcalLabel.setText(mm.round_double(mm.getNewRecipe().getKcal()) + " kcal");
    }

    private void addIngredientAction(String amountText, Ingredient ingredient) {
        if (selectedProduct != null) {
            if (amountText.length() > 0) {
                ingredientsList.getItems().remove(ingredient);
                ingredient.setProduct(selectedProduct);
                ingredient.setAmount(Integer.parseInt(amountText));
                mm.getNewRecipe().addIngredient(ingredient);
                kcalLabel.setText(mm.round_double(mm.getNewRecipe().getKcal()) + " kcal");
                ingredientsList.getItems().add(ingredient);
                ingredientsList.getItems().add(new Ingredient(null, 0, 0));
            }
        }
    }

    private void deleteIngredientAction(Ingredient index) {
        mm.getNewRecipe().removeIngredient(index);
        kcalLabel.setText(mm.round_double(mm.getNewRecipe().getKcal()) + " kcal");
        setList();
    }

    // Instruction Table functions =====================================================================================
    private void setTable(){
        instructions = FXCollections.observableArrayList(mm.getNewRecipe().getInstructions());
        instructionTable.getItems().clear();
        instructionTable.setItems(instructions);
        instructionTable.getItems().add(new Instruction(null,instructionTable.getItems().size()+1,null));
        instructionTable.setFixedCellSize(150);
        instructionTable.prefHeightProperty().bind(instructionTable.fixedCellSizeProperty().multiply(Bindings.size(instructionTable.getItems()).add(0.2)));
    }

    private void addInstructionAction(Instruction instruction) {
        if (instruction.getDescriptionText()!=null && instruction.getDescriptionText().length()>0){
            mm.getNewRecipe().addInstruction(instruction);
            setTable();
        }
    }

    private void deleteInstructionAction(int index) {
        mm.getNewRecipe().removeInstruction(index);
        setTable();
    }

    private ContextMenu instructionImageAction(MouseEvent mouseEvent, ImageView imageView, int index) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY){
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem();
            menuItem.setText("Delete");
            menuItem.setOnAction(event -> deleteInstructionImageAction(imageView, index));
            contextMenu.getItems().add(menuItem);
            return contextMenu;
        }
        else {
            imageAction(imageView);
            return null;
        }
    }

    private void deleteInstructionImageAction(ImageView imageView, int index) {
        imageView.setImage(mm.getAddImage());
        mm.getNewRecipe().removeInstructionImage(index);
    }

    // Menu's Buttons Actions ==========================================================================================
    private void recipesAction(ActionEvent event) {
        RecipesController controller = new RecipesController(dm, mm, split.getWidth(),split.getHeight());
        mm.show(getClass(),"recipes-view.fxml",controller, event);
    } // Recipes Button Action

    private void homeAction(ActionEvent event) {
        recipesAction(event);
    } // Home Button Action

    private void backAction(ActionEvent event) {
        recipesAction(event);
    } // Back Button Action

    private void addAction(ActionEvent event){
        AddController controller = new AddController(dm, mm, split.getWidth(), split.getHeight());
        mm.show(getClass(),"add-view.fxml",controller,event);
    } // Add Recipe Button Action
}
