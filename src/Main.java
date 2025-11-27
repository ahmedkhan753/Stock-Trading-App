package stockapp.src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main application class. This is the single entry point for the Stock Trader application.
 * It is responsible for initializing the JavaFX environment and loading the first GUI screen (Login/Register).
 */
public class Main extends Application {

    private static final String LOGIN_FXML_PATH = "/login_register.fxml";
    @Override
    public void start(Stage stage) throws Exception {
        try {
            // 1. Get the FXMLLoader instance.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_FXML_PATH));
            Scene scene = new Scene(fxmlLoader.load(), 400, 450);
            stage.setTitle("Stock Trader: Login & Register");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("FATAL ERROR: Failed to load the initial GUI or JavaFX components are missing.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}