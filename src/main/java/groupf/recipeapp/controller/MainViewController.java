package groupf.recipeapp.controller;

import groupf.recipeapp.dao.RecipeDAO; 
import groupf.recipeapp.dao.RecipeDAOImpl; 
import groupf.recipeapp.dao.RegionDAO; 
import groupf.recipeapp.dao.RegionDAOImpl;
import groupf.recipeapp.entity.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException; 
import java.util.List; 
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import groupf.recipeapp.App;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent; 
import groupf.recipeapp.entity.Ingredient; 
import groupf.recipeapp.entity.Instruction; 
import groupf.recipeapp.entity.InstructionEntry; 
import groupf.recipeapp.entity.Region; 
import javafx.scene.image.ImageView; 
import javafx.scene.image.Image; 


/**
 * the controller of the main view.
 * handle the user interaction, such as search and list selection, and coordinate with the backend service.
 */
public class MainViewController implements Initializable {

    // --- FXML UI Components ---
    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private Label previewRecipeName;
    @FXML
    private Label previewRegion;
    @FXML
    private Label previewServing;
    @FXML
    private Label previewDescription;
    @FXML
    private Label previewIngredients;
    @FXML
    private Label previewInstructions;
    @FXML
    private ScrollPane previewScrollPane; 
    @FXML
    private VBox recipeDetailsVBox;
    @FXML
    private ImageView previewImageView;

    // --- the backend service interface (now using the real implementation) ---
    private RecipeDAO recipeDAO; 
    private RegionDAO regionDAO;

    private Recipe currentSelectedRecipe;


    // used to store the current filtered region
    private String currentFilteredRegion = null;

    // --- instantiate the DAO in the constructor ---
    public MainViewController() {
        System.out.println("DEBUG: MainViewController: constructor is called. instantiate the DAO.");
        this.recipeDAO = new RecipeDAOImpl();
        this.regionDAO = new RegionDAOImpl();
    }

    /**
     * receive the region code from WorldMapView and filter the recipes.
     * called in App.java.
     * @param regionCode the region code passed from WorldMapView.
     */
    public void initData(String regionCode) {
        System.out.println("DEBUG: MainViewController: initData method is called. received the region code: " + regionCode + ". hash: " + this.hashCode());
        this.currentFilteredRegion = regionCode;
        filterRecipesByRegion(regionCode); // filter the recipes by the region code
        System.out.println("DEBUG: MainViewController: initData completed. currentFilteredRegion is set to: " + currentFilteredRegion + ". hash: " + this.hashCode());
    }
    
    
    @FXML
    private void handleRecommendedDishes(ActionEvent event) {
        System.out.println("left navigation button: World Cuisines is clicked, try to jump to the world map page.");
        try {
            App.setRoot("WorldMapView"); // call the setRoot method of App class to jump to WorldMapView
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Page jump failed", "Failed to load WorldMapView.fxml. Please check if the file exists and the path is correct.");
        }
    }

    /**
     * handle the mouse click event of recipeListView, distinguish single click and double click.
     * single click: display the recipe preview.
     * double click: jump to the recipe detail page.
     */
    @FXML
    private void handleRecipeListClick(javafx.scene.input.MouseEvent event) {
        // get the current selected recipe
        Recipe selectedRecipe = recipeListView.getSelectionModel().getSelectedItem();

        if (selectedRecipe != null) {
            if (event.getClickCount() == 1) { // single click event
                System.out.println("single click recipe: " + selectedRecipe.getName());
                displayRecipePreview(selectedRecipe);
            } else if (event.getClickCount() == 2) { // double click event
                System.out.println("double click recipe, prepare to jump to the detail page: " + selectedRecipe.getName());
                navigateToRecipeDetail(selectedRecipe, event); // pass in event to get the current Stage
            }
        }
    }

    /**
     * update the preview area on the right side according to the passed Recipe object.
     */
    private void displayRecipePreview(Recipe recipe) {

        this.currentSelectedRecipe = recipe; // save the selected recipe, used for full recipe page

        if (recipe != null) {
            previewRecipeName.setText(recipe.getName() != null ? recipe.getName() : "N/A");
            previewDescription.setText(recipe.getDescription() != null ? recipe.getDescription() : "No description.");
    
            // set the image
            if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
                try {
                    // the image path is relative to the resource directory
                    // the image file exists in src/main/resources/groupf/recipeapp/images/ 
                    Image image = new Image(getClass().getResourceAsStream(recipe.getImagePath()));
                    previewImageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("Failed to load image: " + recipe.getImagePath() + " - " + e.getMessage());
                    previewImageView.setImage(null); // clear the image when loading fails
                }
            } else {
                previewImageView.setImage(null); // clear the image when there is no image path
            }
    
            // removed other redundant Label assignments
        } else {
            // clear the preview area
            previewRecipeName.setText("Recipe Name");
            previewDescription.setText("Description goes here.");
            previewImageView.setImage(null); // clear the image
            // removed other redundant Label assignments
        }
    }

    /**
     * jump to the recipe detail page.
     * @param selectedRecipe
     * @param event the mouse event, used to get the current stage
     */
    private void navigateToRecipeDetail(Recipe selectedRecipe, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/groupf/recipeapp/fxml/FullRecipeView.fxml"));
            Parent root = loader.load();

            FullRecipeController controller = loader.getController();
            controller.setRecipe(selectedRecipe);

            Stage stage = new Stage();
            stage.setTitle("Recipe Details - " + selectedRecipe.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Failed to open the recipe detail page", e.getMessage()); // add error hint
        }
    }

    
    /**
     * when the FXML file is loaded, all @FXML members are injected, this method will be called automatically.
     * used to initialize the view, set the listener and load the initial data.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("MainViewController is initializing...");

        // 1. configure the ListView to display the Recipe object
        setupListViewCellFactory();

        // 2. configure the ListView to handle the item selection event

        // 3. load the initial recipe list (only load all recipes when there is no region filter)
        if (currentFilteredRegion == null) {
            System.out.println("DEBUG: MainViewController: initialize: currentFilteredRegion is null, load all recipes.");
            loadAllRecipes();
        } else {
            System.out.println("DEBUG: MainViewController: initialize: currentFilteredRegion is set ('" + currentFilteredRegion + "'), skip loading all recipes.");
        }
        System.out.println("DEBUG: MainViewController: initialize completed. hash: " + this.hashCode() + ". final currentFilteredRegion: " + currentFilteredRegion);
    }

    /**
     * handle the click event of the "search" button.
     * get the input from the text field, and call the corresponding data loading method.
     */
    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchText = searchTextField.getText().trim();

        if (searchText.isEmpty()) {
            // if the search field is empty, reload all recipes (or filter by the current region)
            if (currentFilteredRegion != null) {
                filterRecipesByRegion(currentFilteredRegion);
            } else {
                loadAllRecipes();
            }
        } else {
            // otherwise, search by the keyword (if the region filter is set, only search in the current region)
            // fix point: here the original code only passed searchText
            searchRecipes(searchText, currentFilteredRegion);
        }
    }

    /**
     * clear the region filter, and display all recipes.
     */
    @FXML
    private void handleResetRegion() {
        System.out.println("ResetRegion button is clicked.");
        currentFilteredRegion = null; // clear the region filter
        loadAllRecipes(); // reload all recipes
        System.out.println("DEBUG: MainViewController: handleResetRegion completed. currentFilteredRegion: " + currentFilteredRegion);
    }

    // --- data loading and display methods ---

    /**
     * load and display all recipes.
     */
    private void loadAllRecipes() {
        System.out.println("DEBUG: MainViewController: loadAllRecipes is called. hash: " + this.hashCode());
        List<Recipe> recipes = recipeDAO.getAllRecipes(); // use the DAO to get the data

        recipeListView.getItems().clear();
        recipeListView.getItems().addAll(recipes);
        displayRecipePreview(null); // clear the preview
        System.out.println("DEBUG: MainViewController: loadAllRecipes completed.");
    }

    /**
     * search recipes by the name and display the results.
     */
    private void searchRecipes(String name, String regionCode) {
        System.out.println("DEBUG: MainViewController: searchRecipes is called. name: " + name + ", region: " + regionCode + ". hash: " + this.hashCode());
        List<Recipe> recipes;
        if (regionCode != null) {
            // first get the region ID by the region code
            try {
                Region region = regionDAO.getRegionByCode(regionCode);
                if (region != null) {
                    recipes = recipeDAO.searchRecipesByNameAndRegion(name, region.getId());
                } else {
                    System.err.println("Error: region not found for the region code: " + regionCode);
                    recipes = recipeDAO.searchRecipesByName(name); // if the region does not exist, do not filter by the region
                }
            } catch (SQLException e) {
                System.err.println("Database operation error, cannot get the region or search recipes by the region code: " + e.getMessage());
                e.printStackTrace();
                recipes = recipeDAO.searchRecipesByName(name); // if an error occurs, revert to only search by the name
            }
        } else {
            recipes = recipeDAO.searchRecipesByName(name); // use the DAO to get the data
        }

        recipeListView.getItems().clear();
        recipeListView.getItems().addAll(recipes);
        displayRecipePreview(null); // clear the preview
        System.out.println("DEBUG: MainViewController: searchRecipes completed.");
    }

    private void filterRecipesByRegion(String regionCode) {
        System.out.println("DEBUG: MainViewController: filterRecipesByRegion is called. region: " + regionCode + ". hash: " + this.hashCode());
        // first get the region ID by the region code
        List<Recipe> recipes;
        try {
            Region region = regionDAO.getRegionByCode(regionCode);
            if (region != null) {
                recipes = recipeDAO.getRecipesByRegion(region.getId());
            } else {
                System.err.println("Error: region not found for the region code: " + regionCode);
                recipes = recipeDAO.getAllRecipes(); // if the region code is invalid, display all recipes
            }
        } catch (SQLException e) {
            System.err.println("Database operation error, cannot get the region or filter recipes by the region code: " + e.getMessage());
            e.printStackTrace();
            recipes = recipeDAO.getAllRecipes(); // if an error occurs, revert to display all recipes
        }
        
        recipeListView.getItems().clear();
        recipeListView.getItems().addAll(recipes);
        displayRecipePreview(null); // clear the preview
        System.out.println("DEBUG: MainViewController: filterRecipesByRegion completed.");
    }


    // --- UI configuration helper methods ---

    /**
     * set the CellFactory of the ListView.
     * this determines how each Recipe object is displayed in the list (only display the name).
     */
    private void setupListViewCellFactory() {
        recipeListView.setCellFactory(param -> new ListCell<Recipe>() {
            @Override
            protected void updateItem(Recipe recipe, boolean empty) {
                super.updateItem(recipe, empty);

                if (empty || recipe == null || recipe.getName() == null) {
                    setText(null);
                } else {
                    setText(recipe.getName());
                }
            }
        });
    }

    @FXML
    private void handleSearchRecipes(ActionEvent event) {
        System.out.println("left navigation button: Search Recipes is clicked");
    
        try {
            App.setRoot("MainView");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Failed to open the main page", e.getMessage());
        }
    }

    @FXML
    private void handleCreateNewRecipe(ActionEvent event) {
        System.out.println("left navigation button: Create New Recipe is clicked");

        try {
            App.setRoot("CreateRecipeView"); // use the setRoot method of App class to jump to CreateRecipeView
            System.out.println("jump to CreateRecipeView successfully");

        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Failed to open the create recipe page", e.getMessage());
        }

    }
    @FXML
    private void handleShowFullRecipe() {
        if (currentSelectedRecipe != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/groupf/recipeapp/fxml/FullRecipeView.fxml"));
                Parent root = loader.load();

                FullRecipeController controller = loader.getController();
                controller.setRecipe(currentSelectedRecipe);  // pass in the current selected recipe

                Stage stage = new Stage();
                stage.setTitle("Full Recipe - " + currentSelectedRecipe.getName());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no recipe is selected!");
        }
    }



    /**
     * show the error dialog.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}