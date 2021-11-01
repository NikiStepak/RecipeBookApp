package pl.niki.recipebookapp.manager;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Instruction;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MathManager {
    private ImageView backIcon, homeIcon, recipesIcon, addIcon, doneIcon;
    private Image addImage;
    private int smallIconSize = 15, iconSize = 25;
    private String path = "D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\";
//    private List<Ingredient> ingredients;
//    private List<Instruction> instructions;
//    private boolean instructionsImages;
    private Recipe newRecipe;

    public Recipe getNewRecipe() {
        return newRecipe;
    }

    public void setNewRecipe(Recipe newRecipe) {
        this.newRecipe = newRecipe;
    }

//    public void addIngredient(Product product, int amount){
//        this.ingredients.add(new Ingredient(product, amount));
//    }
//
//    public List<Ingredient> getIngredients() {
//        return ingredients;
//    }
//
//    public void addInstruction(String description, Image image){
//        this.instructions.add(new Instruction(description, image));
//        if (image!=null){
//            this.instructionsImages = true;
//        }
//    }
//
//    public List<Instruction> getInstructions() {
//        return instructions;
//    }

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
//
//    public double countKcal(){
//        double kcal = 0;
//        for (Ingredient ingredient: this.ingredients){
//            kcal += ingredient.countKcal();
//        }
//        kcal = round_double(kcal);
//        return kcal;
//    }
//
//    public boolean isInstructionsImages() {
//        return instructionsImages;
//    }

    public void newRecipe(){
        newRecipe = new Recipe();
    }

    public MathManager() {
        setAddImage();
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


    public ImageView getBackIcon() {
        this.backIcon = new ImageView();
        setIcon(this.backIcon, "arrow_back_icon.png");
        return backIcon;
    }

    public ImageView getHomeIcon() {
        this.homeIcon = new ImageView();
        setIcon(this.homeIcon, "home_icon.png");
        return homeIcon;
    }

    public ImageView getAddIcon() {
        this.addIcon = new ImageView();
        setIcon(this.addIcon, "add_icon.png");
        return addIcon;
    }

    public ImageView getDoneIcon() {
        this.doneIcon = new ImageView();
        setIcon(this.doneIcon, "done_icon.png");
        return doneIcon;
    }

    public ImageView getRecipesIcon() {
        this.recipesIcon = new ImageView();
        setIcon(this.recipesIcon, "reorder_icon.png");
        return recipesIcon;
    }

    public double round_double(double d){
        double r_d = Math.round(d*100);
        return r_d/100;
    }

    public void setNewRecipe(String name, String time, int servings, String description, Image image) {
        this.newRecipe.set(name,time,servings,description,image);
    }
}
