package pl.niki.recipebookapp.recipes;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Recipe {
    private String name, description, time;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;
    //private  photo;
    private int amount;
    private double kcal;
    private Image image;
    private boolean instructionImage;
    private String cuisine, course;

    public Recipe() {
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public Recipe(String name, String description, String time, int amount) {
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
    }

    public Recipe(String name, String description, String time, int amount, double kcal) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.amount = amount;
        this.kcal = kcal;
    }

    public Recipe(String name, String description, String time, List<Ingredient> ingredients, List<Instruction> preparation, int amount, double kcal) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.ingredients = ingredients;
        this.instructions = preparation;
        this.amount = amount;
        this.kcal = kcal;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public boolean isInstructionImage() {
        return instructionImage;
    }

    public void setInstructionImage(boolean instructionImage) {
        this.instructionImage = instructionImage;
    }

    public void addIngredient(Product product, int amount){
        Ingredient ingredient = new Ingredient(product,amount);
        this.kcal += ingredient.countKcal();
        ingredients.add(ingredient);
    }

    public void  addInstruction(String instruction, Image image){
//        Image image = null;
        if(image==null){
            this.instructionImage = false;
        }
        else
            this.instructionImage = true;
        instructions.add(new Instruction(instruction, (instructions.size()+1), image));
    }

    public void  addInstruction(String instruction){
//        Image image = null;
//        if(image==null){
//            this.instructionImage = false;
//        }
//        else
            this.instructionImage = true;
        instructions.add(new Instruction(instruction, this.instructions.size()+1));
    }


    public void set(String name, String time, int servings, String description, Image image, String course, String cuisine) {
        this.name = name;
        this.time = time;
        this.amount = servings;
        this.description = description;
        this.image = image;
        this.course = course;
        this.cuisine = cuisine;
    }

    public void removeIngredient(int index) {
        this.kcal -= this.ingredients.get(index).getKcal();
        if (kcal<0) this.kcal = 0;
        this.ingredients.remove(index);
    }
    public void removeIngredient(Ingredient ingredient) {
        this.kcal -= ingredient.getKcal();
        if (kcal<0) this.kcal = 0;
        this.ingredients.remove(ingredient);
    }

    public double countKcal(){
        this.kcal = 0;
        for (Ingredient ingredient: this.ingredients){
            this.kcal += ingredient.getKcal();
        }
        return kcal;
    }

    public void removeInstruction(int index) {
        this.instructions.remove(index);
        setInstructionsStep();
    }

    private void setInstructionsStep() {
        int i = 1;
        for (Instruction instruction: this.instructions){
            instruction.setStep(i);
            i++;
        }
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
}
