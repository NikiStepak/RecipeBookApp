package pl.niki.recipebookapp.recipes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Instruction implements Cloneable{
    private StringProperty description;
    private String descriptionText;
    private int step;
    private Image image;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Instruction(String description, int step) {
        this.descriptionText = description;
        this.step = step;
        descriptionProperty().set(step+".  "+description);
        try {
            this.image = new Image(new FileInputStream("D:\\PLIKI\\NIKI\\CV\\recipeBookApp\\src\\main\\java\\pl\\niki\\recipebookapp\\images\\pancake.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Instruction(String description,int step, Image image) {
        this.descriptionText = description;
        this.step = step;
        descriptionProperty().set(step+".  "+description);
        this.image = image;
    }

    public StringProperty descriptionProperty() {
        if (description==null)
            description = new SimpleStringProperty(this, null);
        else
            description.set(step+".  "+descriptionText);
        return description;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public int getStep() {
        return step;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setDescription(String description) {
        this.description.set(this.step+".  "+description);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return descriptionText + " - " + image;
    }
}
