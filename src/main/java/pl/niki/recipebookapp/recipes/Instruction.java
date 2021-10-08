package pl.niki.recipebookapp.recipes;

import javafx.scene.image.Image;

public class Instruction {
    private String description;
    private Image image;

    public Instruction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
