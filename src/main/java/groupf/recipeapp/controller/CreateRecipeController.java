package groupf.recipeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser; 
import java.io.File; 
import java.io.IOException; 
import java.nio.file.Files; 
import java.nio.file.Path; 
import java.nio.file.StandardCopyOption; 
import java.util.List; 
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 


import groupf.recipeapp.entity.Recipe;
import groupf.recipeapp.entity.Ingredient;
import groupf.recipeapp.entity.Instruction;
import groupf.recipeapp.entity.InstructionEntry;
import groupf.recipeapp.entity.Region;
import groupf.recipeapp.dao.RecipeDAO;
import groupf.recipeapp.dao.RecipeDAOImpl;
import groupf.recipeapp.dao.RegionDAO; 
import groupf.recipeapp.dao.RegionDAOImpl; 
import groupf.recipeapp.App; 


public class CreateRecipeController {

    @FXML
    private TextField recipeNameField;

    @FXML
    private TextField servingsField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private VBox ingredientsBox;

    @FXML
    private VBox instructionsBox;

    @FXML
    private ComboBox<Region> regionComboBox; // FXML annotation for regionComboBox

    @FXML
    private Label imagePathLabel; // FXML annotation for imagePathLabel

    private String selectedImagePath; // store the relative path of the selected image file

    private RegionDAO regionDAO; // region DAO instance

    @FXML
    public void initialize() {
        regionDAO = new RegionDAOImpl(); // instantiate RegionDAO
        loadRegions();
    }

    private void loadRegions() {
        try {
            List<Region> regions = regionDAO.getAllRegions();
            ObservableList<Region> regionObservableList = FXCollections.observableArrayList(regions);
            regionComboBox.setItems(regionObservableList);
            if (!regions.isEmpty()) {
                regionComboBox.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            showError("Failed to load regions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                String resourceDir = "src/main/resources/groupf/recipeapp/images/";
                File destDir = new File(resourceDir);
                if (!destDir.exists()) {
                    destDir.mkdirs(); // if the directory does not exist, create it
                }

                // generate a unique file name to prevent duplication
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path destinationPath = new File(destDir, fileName).toPath();

                // copy the file
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // store the relative path, this is the path we will save to the database
                // start from "src/main/resources/"
                this.selectedImagePath = "/groupf/recipeapp/images/" + fileName;
                imagePathLabel.setText(selectedFile.getName()); // display the file name on the UI

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Image uploaded successfully");
                alert.setHeaderText(null);
                alert.setContentText("Image uploaded successfully to local resources.");
                alert.showAndWait();

            } catch (IOException e) {
                showError("Failed to copy image file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddIngredient() {
        HBox ingredientRow = new HBox(5);
        TextField weightField = new TextField();
        weightField.setPromptText("Weight");

        TextField unitField = new TextField();
        unitField.setPromptText("Unit");

        TextField nameField = new TextField();
        nameField.setPromptText("Ingredient Name");

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> ingredientsBox.getChildren().remove(ingredientRow));

        ingredientRow.getChildren().addAll(weightField, unitField, nameField, deleteButton);
        ingredientsBox.getChildren().add(ingredientRow);
    }

    @FXML
    private void handleAddInstruction() {
        HBox stepRow = new HBox(5);
        TextField stepField = new TextField();
        stepField.setPromptText("Step");

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> instructionsBox.getChildren().remove(stepRow));

        stepRow.getChildren().addAll(stepField, deleteButton);
        instructionsBox.getChildren().add(stepRow);
    }

    @FXML
    private void handleSubmitRecipe() {
        String name = recipeNameField.getText().trim();
        String servingsStr = servingsField.getText().trim();
        String description = descriptionArea.getText().trim();
        Region selectedRegion = regionComboBox.getSelectionModel().getSelectedItem(); // get the selected region

        if (name.isEmpty() || servingsStr.isEmpty() || selectedRegion == null) { // check if the region is selected
            showError("Recipe name, servings, and region are required.");
            return;
        }

        int servings;
        try {
            servings = Integer.parseInt(servingsStr);
        } catch (NumberFormatException e) {
            showError("Servings must be a number.");
            return;
        }

        Recipe recipe = new Recipe(name, servings);
        recipe.setDescription(description);
        recipe.setRegion(selectedRegion); // set the region

        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            try {
                // 1. get the actual file path of the current image
                // the image is stored in the src/main/resources/ directory
                String projectRoot = System.getProperty("user.dir"); // get the project root directory
                String currentFilePathOnDisk = projectRoot + File.separator + "src" + File.separator + "main" +
                                               File.separator + "resources" + selectedImagePath.replace("/", File.separator);
                File currentImageFile = new File(currentFilePathOnDisk);

                if (currentImageFile.exists()) {
                    // 2. extract the extension of the original file
                    String currentFileName = currentImageFile.getName();
                    String fileExtension = "";
                    int dotIndex = currentFileName.lastIndexOf('.');
                    if (dotIndex > 0 && dotIndex < currentFileName.length() - 1) {
                        fileExtension = currentFileName.substring(dotIndex); // contains a dot, e.g. ".png"
                    }

                    // 3. construct a new file name: recipe name_image.extension
                    // clean the recipe name, make it suitable for a file name (replace special characters with spaces or underscores)
                    String sanitizedRecipeName = name.replaceAll("[^a-zA-Z0-9\\s]", "").trim().replaceAll("\\s+", "_");
                    String newFileName = sanitizedRecipeName + "_image" + fileExtension;
                    
                    // construct a new destination file path
                    Path newDestinationPath = new File(currentImageFile.getParentFile(), newFileName).toPath();

                    // 4. rename (move) the file
                    Files.move(currentImageFile.toPath(), newDestinationPath, StandardCopyOption.REPLACE_EXISTING);

                    // 5. update selectedImagePath to the new relative path
                    this.selectedImagePath = "/groupf/recipeapp/images/" + newFileName;
                    System.out.println("Image renamed to: " + this.selectedImagePath); // debug information
                } else {
                    System.err.println("Image file not found: " + currentFilePathOnDisk);
                    // warn the user that the image file is not found, but still continue to submit the recipe (do not prevent submission)
                }

            } catch (IOException e) {
                showError("Failed to rename image file: " + e.getMessage());
                e.printStackTrace();
                return; // if the renaming fails, stop the recipe submission
            }
        }
        recipe.setImagePath(selectedImagePath); // set the image path
        

        // collect the input items in ingredientsBox
        for (Node node : ingredientsBox.getChildren()) {
            if (node instanceof HBox hbox) { // check if it is an HBox
                // ensure hbox has at least 3 TextFields (quantity, unit, name)
                if (hbox.getChildren().size() >= 3 &&
                    hbox.getChildren().get(0) instanceof TextField &&
                    hbox.getChildren().get(1) instanceof TextField &&
                    hbox.getChildren().get(2) instanceof TextField) {

                    TextField quantityField = (TextField) hbox.getChildren().get(0);
                    TextField unitField = (TextField) hbox.getChildren().get(1);
                    TextField nameField = (TextField) hbox.getChildren().get(2);

                    try {
                        double quantity = Double.parseDouble(quantityField.getText().trim());
                        String unit = unitField.getText().trim();
                        String ingredientName = nameField.getText().trim();
                        Ingredient ingredient = new Ingredient(ingredientName);
                        recipe.addIngredient(new InstructionEntry(ingredient, quantity, unit));
                    } catch (NumberFormatException e) {
                        showError("Ingredient quantity must be a number.");
                        return;
                    }
                }
            }
        }

        // collect the input items in instructionsBox
        int stepNumber = 1;
        for (Node node : instructionsBox.getChildren()) {
            // ensure it is an HBox and contains a TextField
            if (node instanceof HBox hbox && hbox.getChildren().size() >= 1 && hbox.getChildren().get(0) instanceof TextField) {
                TextField field = (TextField) hbox.getChildren().get(0);
                String stepDescription = field.getText().trim();
                if (!stepDescription.isEmpty()) { // avoid adding empty steps
                    // here we need to pass the recipe object
                    recipe.addInstruction(new Instruction(stepNumber++, stepDescription, recipe));
                }
            }
        }


        // insert into the database
        RecipeDAO recipeDAO = new RecipeDAOImpl();
        boolean success = recipeDAO.insertRecipe(recipe);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Recipe Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Recipe \"" + name + "\" submitted successfully!");
            alert.showAndWait();
            // clear the form after successful submission
            recipeNameField.clear();
            servingsField.clear();
            descriptionArea.clear();
            ingredientsBox.getChildren().clear();
            instructionsBox.getChildren().clear();
            regionComboBox.getSelectionModel().clearSelection();
            imagePathLabel.setText("No image selected");
            selectedImagePath = null; // clear the selected image path

        } else {
            showError("Failed to submit recipe to database.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSearchRecipes(ActionEvent event) {
        System.out.println("Left navigation button: Search Recipes was clicked");

        try {
            App.setRoot("MainView"); // use the setRoot method of App class to jump to MainView
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handle the click event of the "World Cuisines" button, jump to the world map page.
     */
    @FXML
    private void handleWorldCuisines() {
        System.out.println("Left navigation button: World Cuisines was clicked, trying to jump to the world map page.");
        try {
            App.setRoot("WorldMapView"); // call the setRoot method of App class to jump to WorldMapView
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load WorldMapView.fxml. Please check if the file exists and the path is correct.");
        }
    }

}