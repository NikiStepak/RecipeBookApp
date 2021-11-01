package pl.niki.recipebookapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AddInstructionController implements Initializable {
    public TextArea instructionArea;
    public ImageView instructionImage;
    public Button cancelButton, okButton;

    private DataManager dm;
    private MathManager mm;
    private boolean added;
    private Image image;

    public AddInstructionController(DataManager dm, MathManager mm) {
        this.dm = dm;
        this.mm = mm;
        this.added = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instructionImage.setImage(mm.getAddImage());
        instructionImage.setOnMouseClicked(this::imageAction);

        cancelButton.setOnAction(this::cancelAction);
        okButton.setOnAction(this::okAction);
    }

    private void okAction(ActionEvent event) {
        String description = instructionArea.getText();
        if (description.length()>3){
            mm.getNewRecipe().addInstruction(description, this.image);
            this.added = true;
            cancelAction(event);
        }
    }

    private void cancelAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void imageAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(instructionImage.getScene().getWindow());
        try {
            if (file!=null) {
                String type = Files.probeContentType(file.toPath());
                type = type.split("/")[0];
                if (type.equals("image")){
                    this.image = new Image(file.toURI().toString());
                    instructionImage.setImage(image);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            this.image = null;
        }
    }


    public MathManager getMm() {
        return mm;
    }

    public boolean isAdded() {
        return added;
    }
}
