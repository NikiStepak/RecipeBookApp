package pl.niki.recipebookapp.manager;

public class MathManager {

    public MathManager() {
    }

    public double round_double(double d){
        double r_d = Math.round(d*100);
        return r_d/100;
    }
}
