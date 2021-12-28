package pl.niki.recipebookapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pl.niki.recipebookapp.controllers.RecipesController;
import pl.niki.recipebookapp.manager.DataManager;
import pl.niki.recipebookapp.manager.Manager;
import java.io.IOException;
import java.util.Objects;

public class RecipesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RecipesApplication.class.getResource("recipes-view.fxml"));
        DataManager dm = new DataManager(getHostServices());
        Manager mm = new Manager();
        RecipesController controller = new RecipesController(dm, mm, 1100, 602);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pl/niki/recipebookapp/images/recipes.png"))));
        stage.setTitle("Recipe Book");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}