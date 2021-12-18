package pl.niki.recipebookapp.recipes;

public class Ingredient implements Cloneable {
    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private Product product;
    private int amount;
    private double kcal;

    // =================================================================================================================
    // Constructors
    // =================================================================================================================
    public Ingredient(Product product, int amount, double kcal) {
        this.product = product;
        this.amount = amount;
        this.kcal = kcal;
    }

    public Ingredient(Product product, int amount) {
        this.product = product;
        this.amount = amount;
        countKcal();
    }

    // =================================================================================================================
    // Getters
    // =================================================================================================================
    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public double getKcal() {
        return kcal;
    }

    // =================================================================================================================
    // Setters
    // =================================================================================================================
    public void setAmount(int amount) {
        this.amount = amount;
        countKcal();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // =================================================================================================================
    // Override methods
    // =================================================================================================================
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return amount + "g " + product.getName();
    }

    // =================================================================================================================
    // Public methods
    // =================================================================================================================
    public double countKcal(){
        double ingredientKcal = this.product.getKcal() * this.amount / this.product.getAmount();
        this.kcal = ingredientKcal;
        return ingredientKcal;
    }
}
