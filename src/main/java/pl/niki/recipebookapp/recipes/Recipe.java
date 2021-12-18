package pl.niki.recipebookapp.recipes;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Recipe implements Cloneable {
    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private String name, description, cuisine, course, url;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;
    private Image image;
    private int amount, time;
    private double kcal;
    private boolean instructionImage;

    private final int id;

    // =================================================================================================================
    // Constructors
    // =================================================================================================================
    public Recipe(int id) {
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.id = id;
    }

    public Recipe(String name, String description, int time, int amount, int id) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.amount = amount;
        this.kcal = 0;
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
        try {
            this.image = new Image(new FileInputStream("D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\pancake.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.cuisine = "-";
        this.course = "soup";
        this.id = id;
        this.url = null;
    }

    public Recipe(String name, String description, int time, int amount, double kcal, int id) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.amount = amount;
        this.kcal = kcal;
        this.cuisine = "-";
        this.id = id;
        this.url = null;

    }

    public Recipe(String name, int id, String description, int time, List<Ingredient> ingredients, List<Instruction> preparation, int amount, double kcal) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.ingredients = ingredients;
        this.instructions = preparation;
        this.amount = amount;
        this.kcal = kcal;
        this.cuisine = "-";
        this.course = "soup";
        this.id = id;
        this.url = null;
    }

    // Copy Constructor
    public Recipe(Recipe newRecipe) {
        this.id = newRecipe.id;
        this.instructionImage = newRecipe.isInstructionImage();
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
        for (Ingredient ingredient: newRecipe.getIngredients()){
            try {
                this.ingredients.add((Ingredient) ingredient.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        for (Instruction instruction: newRecipe.getInstructions()){
            try {
                this.instructions.add((Instruction) instruction.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        set(newRecipe.name, newRecipe.kcal, newRecipe.time, newRecipe.amount, newRecipe.description, newRecipe.image, newRecipe.course, newRecipe.cuisine, newRecipe.url);
    }

    // =================================================================================================================
    // Getters
    // =================================================================================================================
    public String getUrl() {
        return url;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTime() {
        return time;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    public int getAmount() {
        return amount;
    }

    public double getKcal() {
        return kcal;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getCourse() {
        return course;
    }

    public boolean isInstructionImage() {
        return instructionImage;
    }

    // =================================================================================================================
    // Setters
    // =================================================================================================================
    public void setImage(Image image) {
        this.image = image;
    }

    // =================================================================================================================
    // Override methods
    // =================================================================================================================
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // =================================================================================================================
    // Public methods
    // =================================================================================================================
    public void set(String name, double kcal, int time, int servings, String description, Image image, String course, String cuisine, String url) {
        this.name = name;
        this.time = time;
        this.amount = servings;
        this.description = description;
        this.image = image;
        this.course = course;
        this.cuisine = cuisine;
        this.url = url;
        this.kcal = kcal;
    }

    public void countKcal(){
        this.kcal = 0;
        for (Ingredient ingredient: this.ingredients){
            this.kcal += ingredient.getKcal();
        }
    }

    // Ingredient List functions =======================================================================================
    public void addIngredient(Product product, int amount){
        Ingredient ingredient = new Ingredient(product,amount);
        this.kcal += ingredient.countKcal();
        ingredients.add(ingredient);
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        this.kcal += ingredient.getKcal();
    }

    public void removeIngredient(Ingredient ingredient) {
        this.kcal -= ingredient.getKcal();
        if (kcal<0) this.kcal = 0;
        this.ingredients.remove(ingredient);
    }

    // Instruction List functions ======================================================================================
    public void  addInstruction(String instruction){
        this.instructionImage = true;
        instructions.add(new Instruction(instruction, this.instructions.size()+1));
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
        if (instruction.getImage()!=null){
            this.instructionImage = true;
        }
    }

    public void removeInstruction(int index) {
        this.instructions.remove(index);
        setInstructionsStep();
        checkInstructionsImage();
    }

    public void checkInstructionsImage(){
        this.instructionImage = false;
        for (Instruction instruction: this.instructions){
            if (instruction.getImage()!=null){
                this.instructionImage = true;
                break;
            }
        }
        System.out.println(instructionImage);
    }

    public void removeInstructionImage(int index) {
        this.instructions.get(index).setImage(null);
        checkInstructionsImage();
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================
    private void setInstructionsStep() {
        int i = 1;
        for (Instruction instruction: this.instructions){
            instruction.setStep(i);
            i++;
        }
    }
}
