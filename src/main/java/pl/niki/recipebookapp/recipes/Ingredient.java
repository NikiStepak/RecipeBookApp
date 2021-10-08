package pl.niki.recipebookapp.recipes;

public class Ingredient {
    private Product product;
    private int amount;
    private double kcal;

    public Ingredient(Product product, int amount, double kcal) {
        this.product = product;
        this.amount = amount;
        this.kcal = kcal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    @Override
    public String toString() {
        return amount + "g " + product.getName();
    }
}
