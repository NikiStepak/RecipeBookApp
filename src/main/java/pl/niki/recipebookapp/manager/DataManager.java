package pl.niki.recipebookapp.manager;

import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataManager {
    List<Recipe> recipes;
    List<Product> products;

    public DataManager() {
        this.recipes = new ArrayList<>();
        this.products = new ArrayList<>();
        addProducts();
        addRecipes();
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Product addProduct(String name, double kcal){
        Product p = new Product(name, kcal, 100);
        this.products.add(p);
        return p;
    }

    private void addProducts() {
        this.products.add(new Product("carrot",41.3,100));
        this.products.add(new Product("tomato", 17.7, 100));
        this.products.add(new Product("sugar", 386.7,100));
        this.products.add(new Product("red pepper", 282.3, 100));
        this.products.add(new Product("onion",39.7,100));
        this.products.add(new Product("garlic",152,100));
        this.products.add(new Product("pasta", 131, 100));
        this.products.add(new Product("potato", 61,100));
        this.products.add(new Product("sausage", 346.1, 100));
        this.products.add(new Product("hard cheese",375,100));
        this.products.add(new Product("cream 30%",292,100));
        this.products.add(new Product("egg",155.1,100));
        this.products.add(new Product("oil", 884.1, 100));
        this.products.add(new Product("salt", 0,100));
        this.products.add(new Product("sweet paprika", 282, 100));
        this.products.add(new Product("pepper",251,100));

    }

    private void addRecipes() {
        Recipe recipe = new Recipe("Cheesy Potato Bake", "This super easy Cheesy Potato Bake is my go-to dish to make for family and friends, or when my husband and I feel like a treat. This makes a big batch that serves roughly 8 people as a side dish. It's also perfect to wrap up in tin foil and take along to a family BBQ.",
                "1h 40min", 1900);
        int i = 0;
        for (Product p: this.products){
            //if (i%2==0){
                recipe.addIngredient(p,10*(i+1));
            //}
            //System.out.println(10*(i+1));
            i++;
        }

        recipe.addInstruction("Preheat the oven to 190°C.");
        recipe.addInstruction("Thinly slice the potatoes.");
        recipe.addInstruction("Mix the cream and/or milk, garlic together with lots of salt and pepper.");
        recipe.addInstruction("Place a thin layer of potatoes on the base of an oven proof dish. Sprinkle with cheese and dollops of the creamy mix.");
        recipe.addInstruction("Repeat these layers using all of the potatoes.");
        recipe.addInstruction("Bake for 80 minutes until the potatoes are cooked and the top is golden.");

        this.recipes.add(recipe);

        for (i=0; i<20; i++) {
            this.recipes.add(new Recipe(i+" Cheesy Potato Bake", "This super easy Cheesy Potato Bake is my go-to dish to make for family and friends, or when my husband and I feel like a treat. This makes a big batch that serves roughly 8 people as a side dish. It's also perfect to wrap up in tin foil and take along to a family BBQ.",
                    "1h 40min", 1900));
        }
    }

    public List<Recipe> getSearchedRecipes(String searchText){
        List<Recipe> searchRecipes = new ArrayList<>();
        for (Recipe recipe: recipes){
            if (recipe.getName().toLowerCase().contains(searchText.toLowerCase().trim())){
                searchRecipes.add(recipe);
            }
        }
        return searchRecipes;
    }


    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    public List<Recipe> getIngredientFilteredRecipes(ObservableSet<Product> selectedIngredient) {
        List<Recipe> ingredientFilteredRecipes = new ArrayList<>();
        System.out.println(selectedIngredient.size());
        for (Recipe recipe: recipes){
            for (Product product: selectedIngredient){
                for (Ingredient ingredient: recipe.getIngredients()){
                    if(product == ingredient.getProduct()){
                        ingredientFilteredRecipes.add(recipe);
                    }
                }
            }
        }

        return ingredientFilteredRecipes;
    }
}
