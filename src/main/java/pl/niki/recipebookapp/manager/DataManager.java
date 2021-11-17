package pl.niki.recipebookapp.manager;

import javafx.collections.ObservableSet;
import pl.niki.recipebookapp.recipes.Ingredient;
import pl.niki.recipebookapp.recipes.Product;
import pl.niki.recipebookapp.recipes.Recipe;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private final List<Recipe> recipes;
    private final List<Product> products;
    private final String[] courses = {"soup", "side dish", "salad", "main dish", "drink", "dessert",
            "bread", "snack", "cake", "breakfast, brunch or supper"};
    private final List<String> cuisines;

    public String[] getCourses() {
        return courses;
    }

    public DataManager() {
        this.recipes = new ArrayList<>();
        this.products = new ArrayList<>();
        this.cuisines = new ArrayList<>();
        addProducts();
        addRecipes();
    }

    private int checkIfCuisineExist(String cuisine){
        int exist = -1;
        for (int i=0; i<cuisines.size(); i++){
            if (cuisines.get(i).equals(cuisine)){
                exist = i;
                break;
            }
        }
        return exist;
    }

    public void addCuisine(String cuisine){
        int exist = checkIfCuisineExist(cuisine);
        if (exist>=0){

        }
        else {
            cuisines.add(cuisine);
        }
    }

    public List<String> getCuisines() {
        return cuisines;
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
                100, 1900);
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
                    80, 1900));
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
        if (selectedIngredient.size()>0) {
            List<Recipe> ingredientFilteredRecipes = new ArrayList<>();
            boolean addedRecipe;
            for (Recipe recipe : recipes) {
                addedRecipe = false;
                for (Product product : selectedIngredient) {
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        if (product == ingredient.getProduct()) {
                            ingredientFilteredRecipes.add(recipe);
                            addedRecipe = true;
                            break;
                        }
                    }
                    if (addedRecipe){
                        break;
                    }
                }
            }

            return ingredientFilteredRecipes;
        }
        else {
            return recipes;
        }
    }


    public List<Recipe> getCuisineFilteredRecipes(ObservableSet<String> selectedCuisine) {
        if (selectedCuisine.size()>0){
            List<Recipe> cuisineFilteredRecipes = new ArrayList<>();

            for (Recipe recipe: recipes){
                for (String cuisine: selectedCuisine){
                    if (recipe.getCuisine().equals(cuisine)){
                        cuisineFilteredRecipes.add(recipe);
                    }
                }
            }

            return cuisineFilteredRecipes;
        }
        else {
            return recipes;
        }
    }

    public List<Recipe> getCourseFilteredRecipes(ObservableSet<String> selectedCourse) {
        if (selectedCourse.size()>0){
            List<Recipe> courseFilteredRecipes = new ArrayList<>();

            for (Recipe recipe: recipes){
                for (String course: selectedCourse){
                    if (recipe.getCourse().equals(course)){
                        courseFilteredRecipes.add(recipe);
                    }
                }
            }

            return courseFilteredRecipes;
        }
        else {
            return recipes;
        }
    }

    public List<Recipe> getTimeFilteredRecipes(int minValue, int maxValue) {
        if (minValue == 0 && maxValue == this.maxTime) {
            return recipes;
        }
        else {
            List<Recipe> timeFilteredRecipes = new ArrayList<>();
            for (Recipe recipe: recipes){
                if (recipe.getTime() >= minValue && recipe.getTime() <= maxValue){
                    timeFilteredRecipes.add(recipe);
                }
            }
            return timeFilteredRecipes;
        }
    }

    private int maxTime, maxKcal;

    public void setMax(){
        this.maxTime = 0;
        this.maxKcal = 0;
        for (Recipe recipe: recipes){
            if (recipe.getTime() > this.maxTime){
                this.maxTime = recipe.getTime();
            }
            if (recipe.getKcal() > this.maxKcal){
                this.maxKcal = (int) (recipe.getKcal());
                this.maxKcal += 1;
            }
        }
    }

    public int getMaxKcal() {
        return maxKcal;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public List<Recipe> getKcalFilteredRecipes(double minValue, double maxValue) {
        if (minValue == 0 && maxValue == maxKcal){
            return recipes;
        }
        else {
            List<Recipe> kcalFilteredRecipes = new ArrayList<>();
            for (Recipe recipe: recipes){
                if(recipe.getKcal() >= minValue && recipe.getKcal() <= maxValue){
                    kcalFilteredRecipes.add(recipe);
                }
            }
            return kcalFilteredRecipes;
        }
    }
}
