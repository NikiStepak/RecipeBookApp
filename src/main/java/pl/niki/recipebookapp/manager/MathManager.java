package pl.niki.recipebookapp.manager;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MathManager {
    private ImageView backIcon, editIcon, deleteIcon;
    private Image addImage;
    private final String path = "D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\";
    private Recipe newRecipe;

    public Recipe getNewRecipe() {
        return newRecipe;
    }

    public void setNewRecipe(Recipe newRecipe) {
        this.newRecipe = new Recipe(newRecipe);
    }

    public void setAddImage() {
        try {
            this.addImage = new Image(new FileInputStream("D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\add_photo.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.addImage = null;
        }
    }

    public Image getAddImage() {
        return addImage;
    }

    public List<Product> getNotAddedProducts(List<Product> allProduct){
        List<Product> products = new ArrayList<>();
        boolean isIngredient;
        for (Product product: allProduct){
            isIngredient = false;
            for (Ingredient ingredient: newRecipe.getIngredients()){
                if(product == ingredient.getProduct()){
                    isIngredient = true;
                    break;
                }
            }
            if (!isIngredient){
                products.add(product);
            }

        }
        return products;
    }

    public List<Product> getIngredients(List<Recipe> recipes){
        List<Product> ingredients = new ArrayList<>();
        for (Recipe recipe: recipes){
            for (Ingredient ingredient: recipe.getIngredients()){
                if (!ingredients.contains(ingredient.getProduct())){
                    ingredients.add(ingredient.getProduct());
                }
            }
        }
        return ingredients;
    }

    public void newRecipe(int id){
        newRecipe = new Recipe(id);
    }

    public MathManager() {
        setAddImage();
    }

    private void setIcon(ImageView imageView, String fileName) {
        try {
            Image image = new Image(new FileInputStream(path+fileName));
            int iconSize = 25;
            imageView.setFitWidth(iconSize);
            imageView.setFitHeight(iconSize);
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.backIcon = null;
        }
    }

    private void setSmallIcon(ImageView imageView, String fileName) {
        try {
            Image image = new Image(new FileInputStream(path+fileName));
            int smallIconSize = 15;
            imageView.setFitWidth(smallIconSize);
            imageView.setFitHeight(smallIconSize);
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.backIcon = null;
        }
    }

    public ImageView getFilterIcon() {
        ImageView filterIcon = new ImageView();
        setIcon(filterIcon, "filter_list_icon.png");
        return filterIcon;
    }

    public ImageView getSmallEditIcon() {
        this.editIcon = new ImageView();
        setSmallIcon(this.editIcon,"edit_icon.png");
        return editIcon;
    }

    public ImageView getSmallDeleteIcon() {
        this.deleteIcon = new ImageView();
        setSmallIcon(this.deleteIcon,"delete_icon.png");
        return deleteIcon;
    }

    public ImageView getEditIcon() {
        this.editIcon = new ImageView();
        setIcon(this.editIcon,"edit_icon.png");
        return editIcon;
    }

    public ImageView getDeleteIcon() {
        this.deleteIcon = new ImageView();
        setIcon(this.deleteIcon,"delete_icon.png");
        return deleteIcon;
    }

    public ImageView getBackIcon() {
        this.backIcon = new ImageView();
        setIcon(this.backIcon, "arrow_back_icon.png");
        return backIcon;
    }

    public ImageView getNextIcon() {
        ImageView nextIcon = new ImageView();
        setIcon(nextIcon,"arrow_forward_icon.png");
        return nextIcon;
    }

    public ImageView getHomeIcon() {
        ImageView homeIcon = new ImageView();
        setIcon(homeIcon, "home_icon.png");
        return homeIcon;
    }

    public ImageView getAddIcon() {
        ImageView addIcon = new ImageView();
        setIcon(addIcon, "add_icon.png");
        return addIcon;
    }

    public ImageView getDoneIcon() {
        ImageView doneIcon = new ImageView();
        setIcon(doneIcon, "done_icon.png");
        return doneIcon;
    }

    public ImageView getRecipesIcon() {
        ImageView recipesIcon = new ImageView();
        setIcon(recipesIcon, "reorder_icon.png");
        return recipesIcon;
    }
//
    public double round_double(double d){
        double r_d = Math.round(d*100);
        return r_d/100;
    }

    public void setNewRecipe(double kcal, String name, int time, int servings, String description, Image image, String cuisine, String course, String url) {
        //???????????????????????????????????????
        //check if cuisine exist in list
        //???????????????????????????????????????
        this.newRecipe.set(name, kcal, time,servings,description,image,course,cuisine, url);
    }

    private FXMLLoader setController(Class getC, String fxmlName, Object controller){
        FXMLLoader loader = getLoader(getC,fxmlName);
        loader.setController(controller);
        return loader;
    }

    private FXMLLoader getLoader(Class getC, String fxmlName){
        FXMLLoader loader = new FXMLLoader();
        String locationPath = "/pl/niki/recipebookapp/";
        loader.setLocation(getC.getResource(locationPath +fxmlName));
        return loader;
    }

    public void showAndWait(Class getC, String fxmlName, Object controller, Window window){
        try {
            Parent parent = setController(getC,fxmlName,controller).load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(window);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(Class getC, String fxmlName, Object controller, Event event){
        showStage(setController(getC,fxmlName,controller),event);
    }

    private void showStage(FXMLLoader loader, Event event){
        try {
            Parent parent = loader.load();
            //        scene.getStylesheets().add(getClass().getResource("/pl/niki/recipebookapp/styles/recipe-style.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String countTime(int value){
        String time;
        int hours = value/60;
        if (hours > 0){
            time = hours + "h ";
            if (hours * 60 < value){
                time += (value - (hours*60)) +"min";
            }
        }
        else {
            time = value + "min";
        }
        return time;
    }

    public List<Product> getSearchedIngredients(ObservableList<Product> ingredients, String text) {
        if (text.length() > 0) {
            List<Product> searchedIngredients = new ArrayList<>();
            for (Product product : ingredients) {
                if (product.getName().toLowerCase().contains(text.toLowerCase().trim())) {
                    searchedIngredients.add(product);
                }
            }
            return searchedIngredients;
        }
        else {
            return ingredients;
        }
    }

    public List<String> getSearchedCuisine(ObservableList<String> cuisines, String text) {
        if (text.length() > 0) {
            List<String> searchedCuisine = new ArrayList<>();
            for (String cuisine : cuisines) {
                if (cuisine.toLowerCase().contains(text.toLowerCase().trim())) {
                    searchedCuisine.add(cuisine);
                }
            }
            return searchedCuisine;
        } else {
            return cuisines;
        }
    }

}
