package pl.niki.recipebookapp.manager;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.Objects;

public class MathManager {
    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private Image backIcon, deleteIcon, filterIcon, addIcon, editIcon, nextIcon, homeIcon, listIcon, doneIcon, printIcon, refreshIcon, websiteIcon;
    private Image addImage;
    private Recipe newRecipe;

    private final String path = "D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\";
    private final String path0 = "/pl/niki/recipebookapp/images/";

    // =================================================================================================================
    // Constructors
    // =================================================================================================================
    public MathManager() {
        setAddImage();
        setBackIcon();
        setDeleteIcon();
        setAddIcon();
        setFilterIcon();
        setEditIcon();
        setNextIcon();
        setHomeIcon();
        setDoneIcon();
        setListIcon();
        setPrintIcon();
        setRefreshIcon();
        setWebsiteIcon();
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
        ImageView imageView = new ImageView(filterIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getWebsiteIcon(){
        ImageView imageView = new ImageView(websiteIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getEditIcon() {
        ImageView imageView = new ImageView(editIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getDeleteIcon(boolean small) {
        ImageView imageView = new ImageView(deleteIcon);
        if (small){
            setSmallIcon(imageView);
        }
        else {
            setImageView(imageView);
        }
        return imageView;
    }

    public ImageView getBackIcon() {
        ImageView imageView = new ImageView(backIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getNextIcon() {
        ImageView imageView = new ImageView(nextIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getHomeIcon() {
        ImageView imageView = new ImageView(homeIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getAddIcon() {
        ImageView imageView = new ImageView(addIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getRefreshIcon(){
        ImageView imageView = new ImageView(refreshIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getDoneIcon(boolean small) {
        ImageView imageView = new ImageView(doneIcon);
        if (small){
            setSmallIcon(imageView);
        }
        else {
            setImageView(imageView);
        }
        return imageView;
    }

    public ImageView getRecipesIcon() {
        ImageView imageView = new ImageView(listIcon);
        setImageView(imageView);
        return imageView;
    }

    public ImageView getPrintIcon() {
        ImageView imageView = new ImageView(printIcon);
        setImageView(imageView);
        return imageView;
    }

    // =================================================================================================================
    // Setters
    // =================================================================================================================
    public void setNewRecipe(Recipe newRecipe) {
        this.newRecipe = new Recipe(newRecipe);
    }

    private void setListIcon(){
        try {
            this.listIcon = new Image(new FileInputStream(path+"reorder_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.listIcon = null;
        }
    }

    private void setWebsiteIcon(){
        try {
            this.websiteIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path0+"website_icon.png")));
        } catch (NullPointerException e){
            this.websiteIcon = null;
        }
    }

    private void setRefreshIcon() {
        try {
            this.refreshIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path0+"refresh_icon.png")));
        } catch (NullPointerException e){
            this.refreshIcon = null;
        }
    }

    public void setPrintIcon() {
        try {
            this.printIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path0 + "print_icon.png")));
        } catch (NullPointerException e){
            this.printIcon = null;
        }
    }

    public void setDoneIcon() {
        try {
            this.doneIcon = new Image(new FileInputStream(path+"done_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.doneIcon = null;
        }
    }

    public void setAddImage() {
        try {
            this.addImage = new Image(new FileInputStream(path+"add_photo.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.addImage = null;
        }
    }

    public void setBackIcon() {
        try {
            this.backIcon = new Image(new FileInputStream(path+"arrow_back_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.backIcon = null;
        }
    }

    public void setDeleteIcon() {
        try {
            this.deleteIcon = new Image(new FileInputStream(path+"delete_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.deleteIcon = null;
        }
    }

    public void setAddIcon() {
        try {
            this.addIcon = new Image(new FileInputStream(path+"add_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.addIcon = null;
        }
    }

    public void setFilterIcon() {
        try {
            this.filterIcon = new Image(new FileInputStream(path+"filter_list_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.filterIcon = null;
        }
    }

    public void setEditIcon() {
        try {
            this.editIcon = new Image(new FileInputStream(path+"edit_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.editIcon = null;
        }
    }

    public void setHomeIcon() {
        try {
            this.homeIcon = new Image(new FileInputStream(path+"home_icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.homeIcon = null;
        }
    }

    public void setNextIcon() {
        try {
            this.nextIcon = new Image(new FileInputStream(path+"arrow_forward_icon.png"));
        } catch (FileNotFoundException e){
            e.printStackTrace();
            this.nextIcon = null;
        }
    }

    // =================================================================================================================
    // Public methods
    // =================================================================================================================
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
        //???????????????????????????????????????
        //check if cuisine exist in list
        //???????????????????????????????????????
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

    private void setImageView(ImageView imageView) {
        int iconSize = 25;
        imageView.setFitWidth(iconSize);
        imageView.setFitHeight(iconSize);
    }

    private void setSmallIcon(ImageView imageView) {
        int smallIconSize = 15;
        imageView.setFitWidth(smallIconSize);
        imageView.setFitHeight(smallIconSize);
    }
}
