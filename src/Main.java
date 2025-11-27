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

    // Define the path to the FXML file. It is assumed to be in the classpath (e.g., in a 'resources' folder).
    // Note: The leading '/' means start search from the root of the classpath (e.g., src/resources).
    private static final String LOGIN_FXML_PATH = "/login_register.fxml";

    /**
     * The start method is the main entry point for all JavaFX applications.
     * @param stage The primary stage (window) for this application.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            // 1. Get the FXMLLoader instance.
            // This line finds and loads GUI structure defined in the FXML file.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_FXML_PATH));

            // 2. Load the root element (VBox) and set the scene size.
            Scene scene = new Scene(fxmlLoader.load(), 400, 450);

            // 3. Configure and display the primary stage (window).
            stage.setTitle("Stock Trader: Login & Register");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // Log and handle errors, especially if the FXML file is missing or the JavaFX setup is incomplete.
            System.err.println("FATAL ERROR: Failed to load the initial GUI or JavaFX components are missing.");
            e.printStackTrace();
            // Important: If this fails, it usually means your run configuration is missing the JavaFX VM arguments.
        }
    }
    /**
     * Standard main method required to launch a JavaFX application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // This static method is what tells the JVM to start the JavaFX runtime
        // and subsequently call the start(Stage) method above.
        launch(args);
    }
}