package groupf.recipeapp.controller;

import groupf.recipeapp.App; 

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker;

import java.net.URL;
import java.util.ResourceBundle;

public class WorldMapController implements Initializable {

    @FXML
    private WebView worldMapWebView;

    @FXML
    private Label selectedRegionPromptLabel;

    private WebEngine webEngine;
    private String currentSelectedRegion = null; 

    // debug point: constructor, check when the controller is instantiated
    public WorldMapController() {
        System.out.println("DEBUG: WorldMapController: constructor is called. instance hash: " + this.hashCode());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("DEBUG: WorldMapController: initialize method is called. instance hash: " + this.hashCode() + ". initial currentSelectedRegion: " + currentSelectedRegion);
        webEngine = worldMapWebView.getEngine();

        // enable JavaScript debugging, which helps to view the errors in the WebView during development
        webEngine.setJavaScriptEnabled(true);

        // load the HTML file
        URL htmlFileUrl = getClass().getResource("/groupf/recipeapp/html/worldMap.html");
        if (htmlFileUrl != null) {
            webEngine.load(htmlFileUrl.toExternalForm());
            System.out.println("DEBUG: WorldMapController: HTML file loaded: " + htmlFileUrl.toExternalForm());
        } else {
            System.err.println("Error: cannot find worldMap.html file. Please check the path.");
        }

        // --- use setPromptHandler to listen to the prompt call of JavaScript ---
        webEngine.setPromptHandler(promptData -> {
            String message = promptData.getMessage();
            System.out.println("DEBUG: WorldMapController: received prompt message from JavaScript: '" + message + "'. instance hash: " + this.hashCode());

            // the format of the prompt message is "regionCode:XYZ"
            if (message != null && message.startsWith("regionCode:")) {
                String regionCode = message.substring("regionCode:".length());
                System.out.println("DEBUG: WorldMapController: parsed region code from prompt message: '" + regionCode + "'. instance hash: " + this.hashCode());

                javafx.application.Platform.runLater(() -> {
                    selectedRegionPromptLabel.setText("You choose: " + regionCode + " region.");
                    currentSelectedRegion = regionCode; // save the current selected region
                    System.out.println("DEBUG: WorldMapController: UI updated. currentSelectedRegion is set to '" + currentSelectedRegion + "'. instance hash: " + this.hashCode());
                });
                return ""; // return an empty string, indicating that the prompt has been processed
            }
            return null; // return null, indicating that the prompt has not been processed, and the WebView will use the default behavior
        });
    }

    /**
     * handle the click event of the "view region details" button.
     * pass the selected region code to MainView.
     */
    @FXML
    private void handleViewRegionDetails() {
        System.out.println("DEBUG: WorldMapController: handleViewRegionDetails method is called. instance hash: " + this.hashCode() + ". when clicked, currentSelectedRegion: '" + currentSelectedRegion + "'");
        if (currentSelectedRegion != null) {
            try {
                System.out.println("DEBUG: WorldMapController: try to set MainView as the root view, and pass the region: " + currentSelectedRegion);
                App.setRoot("MainView", currentSelectedRegion);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("WorldMapController: cannot load MainView.fxml and pass the region code.");
            }
        } else {
            System.out.println("DEBUG: WorldMapController: please select a region first. (currentSelectedRegion is null)");
            selectedRegionPromptLabel.setText("Please click on the map area to select a region!"); // this line will reset the label text
        }
    }

    /**
     * handle the click event of the "back to main menu" button.
     */
    @FXML
    private void handleBackToMain() {
        System.out.println("DEBUG: WorldMapController: handleBackToMain method is called. instance hash: " + this.hashCode());
        try {
            App.setRoot("MainView"); // assume App class has a setRoot method to switch FXML views
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("WorldMapController: cannot load MainView.fxml.");
        }
    }
}