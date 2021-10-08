package pl.niki.recipebookapp.recipes;

public class Product {
    private String name;
    private double kcal;
    private int amount;

    public Product(String name, double kcal, int amount) {
        this.name = name;
        this.kcal = kcal;
        this.amount = amount; //gram
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
