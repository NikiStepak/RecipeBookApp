package pl.niki.recipebookapp.manager;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MathManager {
    private ImageView backIcon, homeIcon, recipesIcon, addIcon, doneIcon;
    private int smallIconSize = 15, iconSize = 25;
    private String path = "D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\";
    private List<Ingredient> ingredients;

    public void addIngredient(Product product, int amount){
        this.ingredients.add(new Ingredient(product, amount));
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public MathManager() {
        this.backIcon = new ImageView();
        setIcon(this.backIcon, "arrow_back_icon.png");
        this.homeIcon = new ImageView();
        setIcon(this.homeIcon, "home_icon.png");
        this.recipesIcon = new ImageView();
        setIcon(this.recipesIcon, "reorder_icon.png");
        this.addIcon = new ImageView();
        setIcon(this.addIcon, "add_icon.png");
        this.doneIcon = new ImageView();
        setIcon(this.doneIcon, "done_icon.png");

        ingredients = new ArrayList<>();
    }

    private void setIcon(ImageView imageView, String fileName) {
        try {
            Image image = new Image(new FileInputStream(path+fileName));
            imageView.setFitWidth(this.iconSize);
            imageView.setFitHeight(this.iconSize);
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.backIcon = null;
        }
    }

    public void setAddIcon() {
        this.addIcon = new ImageView();
        setIcon(this.addIcon, "add_icon.png");

    }

    public ImageView getBackIcon() {
        return backIcon;
    }

    public void setBackIcon() {

    }

    public ImageView getHomeIcon() {
        return homeIcon;
    }

    public ImageView getAddIcon() {
        return addIcon;
    }

    public ImageView getDoneIcon() {
        return doneIcon;
    }

    public ImageView getRecipesIcon() {
        return recipesIcon;
    }

    public double round_double(double d){
        double r_d = Math.round(d*100);
        return r_d/100;
    }
}
