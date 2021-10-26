package pl.niki.recipebookapp.recipes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Instruction {
    private StringProperty description;
    private Image image;

    public Instruction(String description) {
        descriptionProperty().set(description);
    }

    public StringProperty descriptionProperty() {
        if (description==null)
            description = new SimpleStringProperty(this, null);
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
