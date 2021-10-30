package pl.niki.recipebookapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Recipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.spi.FileTypeDetector;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    public Button backButton, homeButton, recipesButton, addButton, addIngredientButton;
    public ImageView recipeImage;
    public ListView<Ingredient> ingredientsList;

    private DataManager dm;
    private MathManager mm;
    private ObservableList<Ingredient> ingredients;

    public AddController(DataManager dm, MathManager mm) {
        this.dm = dm;
        this.mm = mm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image addImage = null;
        try {
            addImage = new Image(new FileInputStream("D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\add_photo.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        recipeImage.setImage(addImage);
        recipeImage.setOnMouseClicked(this::imageAction);

        // ListView ====================================================================================================
        ingredients = FXCollections.observableArrayList(mm.getIngredients());
        ingredientsList.setItems(ingredients);
        ingredientsList.setCellFactory(i -> new ListCell<>(){
            @Override
            protected void updateItem(Ingredient ingredient, boolean b) {
                super.updateItem(ingredient, b);
                if (b || ingredient == null || ingredient.toString()==null){
                    setText(null);
                }
                else {
                    setText(ingredient.toString());
                }
            }
        });

        // TableView ===================================================================================================


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
        // add ingredient button
        if (mm.getAddIcon()!=null){
            addButton.setGraphic(mm.getAddIcon());
            mm.setAddIcon();
            addIngredientButton.setGraphic(mm.getAddIcon());
        }
//        addButton.setOnAction(this::backAction);
        addIngredientButton.setOnAction(this::addIngredientAction);
    }

    private void addIngredientAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/addIngredient-view.fxml"));
            AddIngredientController controller = new AddIngredientController(dm, mm);
            loader.setController(controller);
            Parent parent = loader.load();
            Stage stage  = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addIngredientButton.getScene().getWindow());
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("s");
    }

    private void imageAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(recipeImage.getScene().getWindow());
        try {
            if (file!=null) {
                String type = Files.probeContentType(file.toPath());
                type = type.split("/")[0];
                if (type.equals("image")){
                    Image image = new Image(file.toURI().toString());
//                    FXMLLoader loader = new FXMLLoader();
//                    loader.setLocation(getClass().getResource("/pl/niki/recipebookapp/imageCropper-view.fxml"));
//                    ImageCropperController controller = new ImageCropperController(image);
//                    loader.setController(controller);
//                    Parent parent = loader.load();
//                    Stage stage = new Stage();
//                    stage.setTitle("Cropper");
//                    stage.setScene(new Scene(parent));
//                    stage.initModality(Modality.WINDOW_MODAL);
//                    stage.initOwner(recipeImage.getScene().getWindow());
//                    stage.show();
                    recipeImage.setImage(image);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
