package pl.niki.recipebookapp.recipes;

import java.util.Locale;

public class Product implements Cloneable {
    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private String name;
    private double kcal;
    private boolean newNull;

    private final int amount;

    // =================================================================================================================
    // Constructors
    // =================================================================================================================
    public Product(String name, double kcal, int amount, boolean newNull) {
        setName(name);
        this.kcal = kcal;
        this.amount = amount; //gram
        this.newNull = newNull;
    }

    // =================================================================================================================
    // Getters
    // =================================================================================================================
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

    // =================================================================================================================
    // Setters
    // =================================================================================================================
    public void setNewNull(boolean newNull) {
        this.newNull = newNull;
    }

    public void setName(String name) {
        if (name.length()>1){
            this.name = name.substring(0,1).toUpperCase() + name.substring(1);
        }
        else {
            this.name = name;
        }
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
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
        return name;
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================
}
