package pl.niki.recipebookapp.manager;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Manager {
    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private final Image backIcon, deleteIcon, filterIcon, addIcon, editIcon, nextIcon, homeIcon, listIcon, doneIcon, printIcon, refreshIcon, websiteIcon;
    private final Image addImage;
    private Recipe newRecipe;

    // =================================================================================================================
    // Constructors
    // =================================================================================================================
    public Manager() {
        this.addImage = setIcon("add_photo.png");
        this.backIcon = setIcon("arrow_back_icon.png");
        this.deleteIcon = setIcon("delete_icon.png");
        this.addIcon = setIcon("add_icon.png");
        this.filterIcon = setIcon("filter_list_icon.png");
        this.editIcon = setIcon("edit_icon.png");
        this.nextIcon = setIcon("arrow_forward_icon.png");
        this.homeIcon = setIcon("home_icon.png");
        this.doneIcon = setIcon("done_icon.png");
        this.listIcon = setIcon("reorder_icon.png");
        this.printIcon = setIcon("print_icon.png");
        this.refreshIcon = setIcon("refresh_icon.png");
        this.websiteIcon = setIcon("website_icon.png");
    }

    // =================================================================================================================
    // Getters
    // =================================================================================================================
    public Recipe getNewRecipe() {
        return newRecipe;
    }

    public Image getAddImage() {
        return addImage;
    }

    public ImageView getFilterIcon() {
        return getIcon(filterIcon, 25);
    }

    private ImageView getIcon(Image image, int iconSize){
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(iconSize);
        imageView.setFitHeight(iconSize);
        return imageView;
    }

    public ImageView getWebsiteIcon(){
        return getIcon(websiteIcon, 25);
    }

    public ImageView getEditIcon() {
        return getIcon(editIcon, 25);
    }

    public ImageView getDeleteIcon(boolean small) {
        if (small) {
            return getIcon(deleteIcon, 15);
        } else {
            return getIcon(deleteIcon, 25);
        }
    }

    public ImageView getBackIcon() {
        return getIcon(backIcon, 25);
    }

    public ImageView getNextIcon() {
        return getIcon(nextIcon, 25);
    }

    public ImageView getHomeIcon() {
        return getIcon(homeIcon, 25);
    }

    public ImageView getAddIcon() {
        return getIcon(addIcon, 25);
    }

    public ImageView getRefreshIcon(){
        return getIcon(refreshIcon, 25);
    }

    public ImageView getDoneIcon(boolean small) {
        if (small){
            return getIcon(doneIcon,15);
        }
        else {
            return getIcon(doneIcon,25);
        }
    }

    public ImageView getRecipesIcon() {
        return getIcon(listIcon, 25);
    }

    public ImageView getPrintIcon() {
        return getIcon(printIcon, 25);
    }

    // =================================================================================================================
    // Setters
    // =================================================================================================================
    public void setNewRecipe(Recipe newRecipe) {
        this.newRecipe = new Recipe(newRecipe);
    }

    private Image setIcon(String iconName){
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pl/niki/recipebookapp/images/" +iconName)));
        } catch (NullPointerException e){
            return null;
        }
    }

    // =================================================================================================================
    // Public methods
    // =================================================================================================================
    public void setRectangleImage(ImageView imageView, int dropShadow, Color color){
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
        imageView.setEffect(new DropShadow(dropShadow, color));

        imageView.setImage(image);
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

    public void newRecipe(int id){
        newRecipe = new Recipe(id);
    }

    public void setNewRecipe(double kcal, String name, int time, int servings, String description, Image image, String cuisine, String course, String url) {
        this.newRecipe.set(name, kcal, time,servings,description,image,course,cuisine, url);
    }

    public double round_double(double d){
        double r_d = Math.round(d*100);
        return r_d/100;
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

    public <T> void showAndWait(Class<T> getC, String fxmlName, Object controller, Window window, String stageName){
        try {
            Parent parent = setController(getC,fxmlName,controller).load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle(stageName);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pl/niki/recipebookapp/images/recipes.png"))));
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(window);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> void show(Class<T> getC, String fxmlName, Object controller, Event event){
        showStage(setController(getC,fxmlName,controller),event);
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================
    private <T> FXMLLoader setController(Class<T> getC, String fxmlName, Object controller){
        FXMLLoader loader = getLoader(getC,fxmlName);
        loader.setController(controller);
        return loader;
    }

    private <T> FXMLLoader getLoader(Class<T> getC, String fxmlName){
        FXMLLoader loader = new FXMLLoader();
        String locationPath = "/pl/niki/recipebookapp/";
        loader.setLocation(getC.getResource(locationPath +fxmlName));
        return loader;
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
}
