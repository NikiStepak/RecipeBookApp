package pl.niki.recipebookapp.recipes;

public class Ingredient {
    private final Product product;
    private int amount;
    private double kcal;

    public Ingredient(Product product, int amount) {
        this.product = product;
        this.amount = amount;
        countKcal();
    }

    public double countKcal(){
        double ingredientKcal = this.product.getKcal() * this.amount / this.product.getAmount();
        this.kcal = ingredientKcal;
        return ingredientKcal;
    }

    public Product getProduct() {
        return product;
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


    @Override
    public String toString() {
        return amount + "g " + product.getName();
    }
}
