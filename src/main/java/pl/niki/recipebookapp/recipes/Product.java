package pl.niki.recipebookapp.recipes;

public class Product implements Cloneable {
    private String name;
    private double kcal;
    private int amount;
    private boolean newNull;

    public Product(Product newProduct) {
        this.name = newProduct.name;
        this.kcal = newProduct.kcal;
        this.amount = newProduct.amount;
        this.newNull = newProduct.newNull;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Product(String name, double kcal, int amount, boolean newNull) {
        this.name = name;
        this.kcal = kcal;
        this.amount = amount; //gram
        this.newNull = newNull;
    }

    public boolean isNewNull() {
        return newNull;
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

    public void setNewNull(boolean newNull) {
        this.newNull = newNull;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    @Override
    public String toString() {
        return getName();
    }
}
