package pl.niki.recipebookapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;
import pl.niki.recipebookapp.recipes.Instruction;

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
    private boolean added, edit;
    private Instruction editInstruction;
    private Image image;
    private int recipeKey;

    public AddInstructionController(DataManager dm, MathManager mm) {
        this.dm = dm;
        this.mm = mm;
        this.added = false;
        this.edit = false;
        this.image = null;
    }

    public AddInstructionController(DataManager dm, MathManager mm, Instruction instruction, int recipeKey) {
        this.dm = dm;
        this.mm = mm;
        this.added = false;
        this.edit = true;
        this.editInstruction = instruction;
        this.image = instruction.getImage();
        this.recipeKey = recipeKey;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (edit){
            if (image!=null){
                instructionImage.setImage(image);
            }
            else {
                instructionImage.setImage(mm.getAddImage());
            }
            instructionArea.setText(editInstruction.getDescriptionText());
        }
        else {
            instructionImage.setImage(mm.getAddImage());
        }
        instructionImage.setOnMouseClicked(this::imageAction);

        cancelButton.setOnAction(this::cancelAction);
        okButton.setOnAction(this::okAction);
    }

    private void okAction(ActionEvent event) {
        String description = instructionArea.getText();
        if (edit){
            if (description.length()>3){
                this.editInstruction.setDescriptionText(description);
                this.editInstruction.setDescription(description);
                this.editInstruction.setImage(image);
            }
            cancelAction(event);
        }
        else if (description.length()>3){
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
        if (mouseEvent.getButton() == MouseButton.SECONDARY){
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem();
            menuItem.setText("Delete");
            menuItem.setOnAction(this::deleteAction);
            contextMenu.getItems().add(menuItem);
            instructionImage.setOnContextMenuRequested(e -> contextMenu.show(instructionImage,e.getScreenX(),e.getScreenY()));
        }
        else {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(instructionImage.getScene().getWindow());
            try {
                if (file != null) {
                    String type = Files.probeContentType(file.toPath());
                    type = type.split("/")[0];
                    if (type.equals("image")) {
                        this.image = new Image(file.toURI().toString());
                        instructionImage.setImage(image);
                        dm.getRecipes().get(recipeKey).setInstructionImage(true);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                this.image = null;
            }
        }
    }

    private void deleteAction(ActionEvent event) {
        this.image = null;
        this.instructionImage.setImage(mm.getAddImage());
    }


    public MathManager getMm() {
        return mm;
    }

    public boolean isAdded() {
        return added;
    }
}
