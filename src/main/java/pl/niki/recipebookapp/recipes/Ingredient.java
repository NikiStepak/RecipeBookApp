package pl.niki.recipebookapp.recipes;

public class Ingredient implements Cloneable {
    private Product product;
    private int amount;
    private double kcal;

    @Override
    protected Object clone() throws CloneNotSupportedException {
//        Ingredient ingredient = new Ingredient(product,amount,kcal);
//        ingredient.product = (Product) product.clone();
        return new Ingredient(product,amount,kcal);
    }

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
        countKcal();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getKcal() {
        return kcal;
    }


    @Override
    public String toString() {
        return amount + "g " + product.getName();
    }
}
