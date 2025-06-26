package groupf.recipeapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.io.IOException; 
import groupf.recipeapp.controller.MainViewController; 


public class App extends Application {

    private static Scene scene; // declare a static Scene variable
    private static Stage primaryStage; // declare a static Stage variable, to be used in setRoot

    @Override
    public void start(Stage stage) throws Exception { // change the parameter name from primaryStage to stage
        App.primaryStage = stage; // save the main stage
        scene = new Scene(loadFXML("MainView")); // initialize the static Scene, load the main view
        stage.setTitle("GroupF Digital Cookbook");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * static method to load a new FXML file and update the root node of the current scene.
     * @param fxml the name of the FXML file to load (without .fxml extension).
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        // ensure primaryStage always displays App.scene
        if (primaryStage != null && primaryStage.getScene() != scene) {
            primaryStage.setScene(scene);
        }
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }

    /**
     * overloaded static method to load a new FXML file and update the root node of the current scene,
     * and pass a parameter to the target view controller.
     * mainly used to pass the region code from WorldMapController to MainViewController.
     * @param fxml the name of the FXML file to load (without .fxml extension).
     * @param param the parameter to pass to the target controller (e.g. MainViewController).
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static void setRoot(String fxml, String param) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/groupf/recipeapp/fxml/" + fxml + ".fxml"));
        Parent root = fxmlLoader.load();

        // if the loaded FXML is MainView and the region code is passed, pass it to MainViewController
        if (fxml.equals("MainView") && param != null) {
            MainViewController controller = fxmlLoader.getController();
            if (controller != null) {
                controller.initData(param); // call the method in MainViewController to set the region filter
            }
        }
        scene.setRoot(root);
        // ensure primaryStage always displays App.scene
        if (primaryStage != null && primaryStage.getScene() != scene) {
            primaryStage.setScene(scene);
        }
        // diagnostic code: ensure the stage is visible and at the front
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }

    /**
     * helper method to load the specified FXML file and return its root node.
     * @param fxml the name of the FXML file to load.
     * @return the root Parent node of the loaded FXML file.
     * @throws IOException if the FXML file cannot be loaded.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        String fxmlPath = "/groupf/recipeapp/fxml/" + fxml + ".fxml";
        URL fxmlUrl = App.class.getResource(fxmlPath); // try to get the resource URL

        System.out.println("trying to load FXML file: " + fxmlPath); // print the path trying to load
        if (fxmlUrl == null) {
            // if getResource returns null, the resource is not found
            System.err.println("error: FXML resource not found. URL is null, path: " + fxmlPath);
            // print the class from where App is loaded, this helps to diagnose class loader issues
            try {
                System.err.println("App.class 加载自: " + App.class.getProtectionDomain().getCodeSource().getLocation());
            } catch (SecurityException e) {
                System.err.println("cannot get the loading location of App.class: " + e.getMessage());
            }
            // throw a more specific exception, containing the not found path
            throw new IOException("FXML file not found: " + fxmlPath);
        } else {
            System.out.println("FXML resource found. URL: " + fxmlUrl); // print the found URL
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        return fxmlLoader.load();
    }
    /**
     * the main entry point of the JavaFX application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}