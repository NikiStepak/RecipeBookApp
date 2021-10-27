package pl.niki.recipebookapp.manager;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MathManager {
    private ImageView backIcon, homeIcon, recipesIcon;
    private int smallIconSize = 15, iconSize = 25;

    public MathManager() {
        setBackIcon();
        setHomeIcon();
        setRecipesIcon();
    }

    public ImageView getBackIcon() {
        return backIcon;
    }

    public void setBackIcon() {
        try {
            Image image = new Image(new FileInputStream("D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\arrow_back_icon.png"));
            this.backIcon = new ImageView();
            this.backIcon.setFitWidth(this.smallIconSize);
            this.backIcon.setFitHeight(this.smallIconSize);
            this.backIcon.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.backIcon = null;
        }
    }

    public ImageView getHomeIcon() {
        return homeIcon;
    }

    public void setHomeIcon() {
        try {
            Image image = new Image(new FileInputStream("D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\home_icon.png"));
            this.homeIcon = new ImageView();
            this.homeIcon.setFitHeight(this.iconSize);
            this.homeIcon.setFitWidth(this.iconSize);
            this.homeIcon.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.homeIcon = null;
        }
    }

    public ImageView getRecipesIcon() {
        return recipesIcon;
    }

    public void setRecipesIcon() {
        try {
            Image image = new Image(new FileInputStream("D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\reorder_icon.png"));
            this.recipesIcon = new ImageView();
            this.recipesIcon.setFitWidth(this.iconSize);
            this.recipesIcon.setFitHeight(this.iconSize);
            this.recipesIcon.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.recipesIcon = null;
        }
    }

    public double round_double(double d){
        double r_d = Math.round(d*100);
        return r_d/100;
    }


}
