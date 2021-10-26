package pl.niki.recipebookapp.controllers;

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
import javafx.scene.effect.DropShadow;
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
    public ImageView imageView;
    public Label recipeNameLabel, servingsLabel, kcalLabel, descriptionLabel, timeLabel;
    public Button backButton;

    private DataManager dm;
    private MathManager mm;
    private Recipe recipe;
    private ObservableList<Ingredient> ingredients;
    private ObservableList<Instruction> instructions;
    private final int recipeKey;

    public RecipeController(int recipeKey) {
        this.recipeKey = recipeKey;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //System.out.println(recipeKey);
        dm = new DataManager();
        mm = new MathManager();
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
        descriptionColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        descriptionColumn.setCellFactory(i -> {
            TableCell<Instruction,String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descriptionColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        //        descriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instruction, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Instruction, String> instructionStringCellDataFeatures) {
//                return instructionStringCellDataFeatures.getValue().descriptionProperty();
//            }
//        });

//        instructionList.setItems(instructions);
//        instructionList.setCellFactory(i -> new ListCell<>(){
//            @Override
//            protected void updateItem(Instruction instruction, boolean b) {
//                super.updateItem(instruction, b);
//                if (b || instruction == null || instruction.getDescription() == null){
//                    setGraphic(null);
//                    setText(null);
//                }
//                else {
//                    setMinWidth(i.getWidth());
//                    setMaxWidth(i.getWidth());
//                    setPrefWidth(i.getWidth());
//
//
//
//                    setWrapText(true);
//                    setText(instruction.getDescription());
//                }
//            }
//        });


        // no scroll, no focus list and table
        ingredientsList.setMouseTransparent(true);
        ingredientsList.setFocusTraversable(false);

        instructionTable.setMouseTransparent(true);
        instructionTable.setFocusTraversable(false);

//        instructionList.setMouseTransparent( true );
//        instructionList.setFocusTraversable( false );

        // height of list
        ingredientsList.setPrefHeight(ingredients.size()*30+2);

        // ImageView ===================================================================================================
        imageView.setImage(recipe.getImage());

        //rounded rectangle ImageView
        Rectangle rectangle = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        rectangle.setArcWidth(25);
        rectangle.setArcHeight(25);
        imageView.setClip(rectangle);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        //add shadow
        imageView.setClip(null);
        imageView.setEffect(new DropShadow(20, Color.WHITE));

        imageView.setImage(image);

        backButton.setOnAction(this::backAction);
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
//            scene.getStylesheets().add(getClass().getResource("/pl/niki/recipebookapp/styles/recipe-style.css").toExternalForm());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
