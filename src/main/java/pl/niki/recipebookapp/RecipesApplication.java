package pl.niki.recipebookapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.niki.recipebookapp.controllers.RecipesController;

import java.io.IOException;

public class RecipesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RecipesApplication.class.getResource("recipes-view.fxml"));
        RecipesController controller = new RecipesController();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}