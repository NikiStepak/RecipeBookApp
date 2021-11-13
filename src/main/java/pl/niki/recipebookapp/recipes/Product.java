package pl.niki.recipebookapp.recipes;

public class Product {
    private final String name;
    private final double kcal;
    private final int amount;

    public Product(String name, double kcal, int amount) {
        this.name = name;
        this.kcal = kcal;
        this.amount = amount; //gram
    }

    public String getName() {
        return name;
    }

    public double getKcal() {
        return kcal;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return getName();
    }
}
