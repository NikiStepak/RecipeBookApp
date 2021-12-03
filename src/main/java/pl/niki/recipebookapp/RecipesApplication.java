package pl.niki.recipebookapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.niki.recipebookapp.controllers.RecipesController;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.MathManager;

import java.io.IOException;

public class RecipesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RecipesApplication.class.getResource("recipes-view.fxml"));
        DataManager dm = new DataManager(getHostServices());
        MathManager mm = new MathManager();
        RecipesController controller = new RecipesController(dm, mm, 1100, 602);
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