package groupf.recipeapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea; 
import javafx.scene.control.TextField; 
import javafx.scene.layout.HBox; 
import javafx.geometry.Pos; 
import javafx.stage.FileChooser; 
import java.io.File; 
import java.io.IOException; 
import java.nio.file.Files; 
import java.nio.file.Path; 
import java.nio.file.StandardCopyOption; 
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 
import javafx.scene.control.ComboBox; 

import groupf.recipeapp.entity.Recipe;
import groupf.recipeapp.entity.InstructionEntry;
import groupf.recipeapp.entity.Instruction;
import groupf.recipeapp.entity.Ingredient; 
import groupf.recipeapp.entity.Region; // import Region entity

import groupf.recipeapp.dao.InstructionEntryDAO;
import groupf.recipeapp.dao.InstructionEntryDAOImpl;
import groupf.recipeapp.dao.InstructionDAO;
import groupf.recipeapp.dao.InstructionDAOImpl;
import groupf.recipeapp.dao.RecipeDAO; 
import groupf.recipeapp.dao.RecipeDAOImpl; 
import groupf.recipeapp.dao.IngredientDAO; 
import groupf.recipeapp.dao.IngredientDAOImpl;
import groupf.recipeapp.dao.RegionDAO; // import RegionDAO
import groupf.recipeapp.dao.RegionDAOImpl; 

import java.util.*;

import javafx.event.ActionEvent; 
import javafx.scene.control.Button; 
import javafx.scene.control.Alert; 
import java.sql.SQLException; 
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 
import javafx.scene.Node;
import java.nio.file.Paths;

public class FullRecipeController {

    @FXML
    private Label previewRecipeName;

    @FXML
    private TextField editRecipeNameField; 


    @FXML
    private Label previewDescription;

    @FXML
    private TextArea editDescriptionArea; 

    @FXML
    private ImageView previewImageView;

    @FXML
    private VBox ingredientListBox;

    @FXML
    private VBox stepsListBox;

    @FXML
    private Label previewServing;

    @FXML
    private TextField editServingField; 

    @FXML
    private Label previewRegion; // display region in read-only mode

    @FXML
    private ComboBox<Region> editRegionComboBox; // allow selecting region in edit mode

    @FXML
    private TextField editImagePathField; // display/edit image path

    @FXML
    private Button uploadImageButton; // button to upload image

    @FXML
    private Button editCommitButton; 

    @FXML
    private HBox addIngredientButtonBox; 
    @FXML
    private HBox addInstructionButtonBox; 

    @FXML
    private Button deleteButton; 

    @FXML
    private Button scaleServingButton; 

    @FXML
    private HBox scaleInputBox; 

    @FXML
    private TextField scaleMultiplierField; 

    @FXML
    private HBox imageEditBox; // Added this line to control the visibility of the HBox containing image editing elements

    private Recipe recipe;
    private boolean isEditing = false; 
    private RecipeDAO recipeDAO; 
    private IngredientDAO ingredientDAO; 
    private RegionDAO regionDAO; // RegionDAO instance
    private String currentImagePath; // store the relative path of the current image

    // store the references of the dynamically created ingredient edit rows and instruction edit rows
    private List<IngredientEditRow> ingredientEditRows = new ArrayList<>();
    private List<InstructionEditRow> instructionEditRows = new ArrayList<>();
    private List<InstructionEntry> originalIngredients; // store the original ingredient list

    // inner class: used to encapsulate the UI components and data of the ingredient edit row
    private static class IngredientEditRow {
        InstructionEntry originalEntry; // the original InstructionEntry object
        TextField quantityField;
        TextField unitField;
        TextField ingredientNameField; // store the ingredient name

        public IngredientEditRow(InstructionEntry originalEntry, TextField quantityField, TextField unitField, TextField ingredientNameField) {
            this.originalEntry = originalEntry;
            this.quantityField = quantityField;
            this.unitField = unitField;
            this.ingredientNameField = ingredientNameField;
        }
    }

    // inner class: used to encapsulate the UI components and data of the instruction edit row
    private static class InstructionEditRow {
        Instruction originalInstruction; // the original Instruction object
        Label stepNumberLabel; // the label of the step number
        TextArea descriptionArea; // the text area of the step description

        public InstructionEditRow(Instruction originalInstruction, Label stepNumberLabel, TextArea descriptionArea) {
            this.originalInstruction = originalInstruction;
            this.stepNumberLabel = stepNumberLabel;
            this.descriptionArea = descriptionArea;
        }
    }

    // 构造函数：初始化DAO
    public FullRecipeController() {
        this.recipeDAO = new RecipeDAOImpl();
        this.ingredientDAO = new IngredientDAOImpl(); // initialize IngredientDAO
        this.regionDAO = new RegionDAOImpl(); // initialize RegionDAO
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        if (recipe == null) {
            System.err.println("The input recipe is null!");
            return;
        }

        System.out.println("Loading recipe: " + recipe.getName() + ", ID = " + recipe.getId());

        // display the basic information
        previewRecipeName.setText(recipe.getName());
        previewDescription.setText(recipe.getDescription());
        previewServing.setText(String.valueOf(recipe.getServings()));
        previewRegion.setText(recipe.getRegion() != null ? recipe.getRegion().getName() : "Unknown Region");

        // initialize in display mode
        setEditingMode(false);

        // display the image
        if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(recipe.getImagePath()));
                previewImageView.setImage(image);
                System.out.println("Image loaded successfully: " + recipe.getImagePath());
            } catch (Exception e) {
                previewImageView.setImage(null);
                System.err.println("Image loaded failed: " + recipe.getImagePath() + ", error message: " + e.getMessage());
            }
        }

        // load and display the ingredients and steps (initial in read-only mode)
        loadAndDisplayIngredients();
        loadAndDisplayInstructions();
        loadRegions(); // load regions when setting the recipe

        // set the image path and region in edit mode
        if (recipe.getImagePath() != null) {
            editImagePathField.setText(recipe.getImagePath());
            this.currentImagePath = recipe.getImagePath();
        } else {
            editImagePathField.setText("");
            this.currentImagePath = null;
        }

        // set the selected region
        if (recipe.getRegion() != null) {
            editRegionComboBox.getSelectionModel().select(recipe.getRegion());
        } else {
            editRegionComboBox.getSelectionModel().clearSelection();
        }
    }

     /**
     * Helper to load and display ingredients (read-only labels).
     */
    private void loadAndDisplayIngredients() {
        ingredientListBox.getChildren().clear();
        InstructionEntryDAO instructionEntryDAO = new InstructionEntryDAOImpl();
        originalIngredients = instructionEntryDAO.getInstructionEntriesByRecipeId(recipe.getId()); // load and store the original data

        if (originalIngredients == null || originalIngredients.isEmpty()) {
            ingredientListBox.getChildren().add(new Label("No ingredients found."));
            System.out.println("No ingredients found!");
        } else {
            displayIngredientsWithScale(1); // default display the original quantity (scale factor is 1)
        }
    }

    /**
     * Helper to display ingredients with a given scale factor.
     * @param scaleFactor the scale factor.
     */
    private void displayIngredientsWithScale(int scaleFactor) {
        ingredientListBox.getChildren().clear();
        if (originalIngredients == null || originalIngredients.isEmpty()) {
            ingredientListBox.getChildren().add(new Label("No ingredients found."));
            return;
        }

        int index = 1;
        for (InstructionEntry entry : originalIngredients) {
            String ingredientName = (entry.getIngredient() != null) ? entry.getIngredient().getName() : "Unknown Ingredient";
            // multiply by the scale factor and display
            String displayText = "Ingredient " + index++ + ": " + (entry.getQuantity() * scaleFactor) + " " + entry.getUnit() + " " + ingredientName;
            ingredientListBox.getChildren().add(new Label(displayText));
            System.out.println("Display instruction (scaled): " + displayText);
        }
    }

    /**
     * Helper to load and display instructions (read-only labels).
     */
    private void loadAndDisplayInstructions() {
        stepsListBox.getChildren().clear();
        InstructionDAO instructionDAO = new InstructionDAOImpl();
        List<Instruction> instructions = instructionDAO.getInstructionsByRecipeId(recipe.getId());

        // sort by the step number
        instructions.sort(Comparator.comparingInt(Instruction::getStepNumber));

        if (instructions == null || instructions.isEmpty()) {
            stepsListBox.getChildren().add(new Label("No instructions found."));
            System.out.println("No instructions found!");
        } else {
            for (Instruction instruction : instructions) {
                String stepText = "Step " + instruction.getStepNumber() + ": " + instruction.getDescription();
                stepsListBox.getChildren().add(new Label(stepText));
                System.out.println("Display step: " + stepText);
            }
        }
    }

    /**
     * Helper to load regions into the ComboBox.
     */
    private void loadRegions() {
        try {
            List<Region> regions = regionDAO.getAllRegions();
            ObservableList<Region> regionObservableList = FXCollections.observableArrayList(regions);
            editRegionComboBox.setItems(regionObservableList);
            // set display property for ComboBox (show region name)
            editRegionComboBox.setConverter(new javafx.util.StringConverter<Region>() {
                @Override
                public String toString(Region region) {
                    return region != null ? region.getName() : "";
                }

                @Override
                public Region fromString(String string) {
                    // this is not needed for display only
                    return null;
                }
            });
        } catch (Exception e) {
            showErrorDialog("Error", "Failed to load regions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * switch the UI state between edit and display mode.
     * @param editing true means enter edit mode, false means enter display mode.
     */
    private void setEditingMode(boolean editing) {
        this.isEditing = editing;

        // switch the display/edit mode of the recipe name
        previewRecipeName.setVisible(!editing);
        editRecipeNameField.setVisible(editing);
        if (editing) {
            editRecipeNameField.setText(recipe.getName());
        }

        // switch the display/edit mode of the description
        previewDescription.setVisible(!editing);
        editDescriptionArea.setVisible(editing);
        if (editing) {
            editDescriptionArea.setText(recipe.getDescription());
        }

        // switch the display/edit mode of the serving
        previewServing.setVisible(!editing);
        editServingField.setVisible(editing);
        if (editing) {
            editServingField.setText(String.valueOf(recipe.getServings()));
        }

        // switch the button text
        editCommitButton.setText(editing ? "Commit" : "Edit");

        // switch the visibility of the "add" button container
        addIngredientButtonBox.setVisible(editing);
        addInstructionButtonBox.setVisible(editing);

        // switch the visibility of the image path field and upload button
        editImagePathField.setVisible(editing);
        uploadImageButton.setVisible(editing);
        imageEditBox.setVisible(editing);

        // switch the visibility of the region display label and ComboBox
        previewRegion.setVisible(!editing);
        editRegionComboBox.setVisible(editing);

        // the visibility of the scale button and input box: hidden in edit mode, shown in display mode
        scaleServingButton.setVisible(!editing);
        scaleInputBox.setVisible(false); // hidden in both modes

        // handle the dynamic content of the ingredients and steps
        if (editing) {
            // clear the current display, and fill with editable fields
            ingredientListBox.getChildren().clear();
            ingredientEditRows.clear(); // clear the existing references, avoid duplication
            loadIngredientsForEditing();

            stepsListBox.getChildren().clear();
            instructionEditRows.clear(); // clear the existing references
            loadInstructionsForEditing();

        } else {
            // restore to read-only label display
            loadAndDisplayIngredients();
            loadAndDisplayInstructions();
            ingredientEditRows.clear(); // clear the edit row references
            instructionEditRows.clear(); // clear the edit row references
        }
    }

    private void loadIngredientsForEditing() {
        ingredientListBox.getChildren().clear();
        ingredientEditRows.clear();

        if (originalIngredients != null && !originalIngredients.isEmpty()) {
            for (InstructionEntry entry : originalIngredients) {
                addIngredientEditRow(entry);
            }
        } else {
            ingredientListBox.getChildren().add(new Label("No ingredients found."));
        }
        System.out.println("Loaded " + ingredientEditRows.size() + " ingredient edit rows.");

    }

    /**
     * load the existing steps and display them with editable fields.
     */
    private void loadInstructionsForEditing() {
        InstructionDAO instructionDAO = new InstructionDAOImpl();
        List<Instruction> instructions = instructionDAO.getInstructionsByRecipeId(recipe.getId());

        // sort by the step number
        instructions.sort(Comparator.comparingInt(Instruction::getStepNumber));

        if (instructions != null) {
            for (Instruction instruction : instructions) {
                addInstructionEditRow(instruction); // add the edit row for the existing step
            }
        }
    }

    /**
     * dynamically add an editable ingredient row.
     * if the entry is null, add an empty row (for new ingredient).
     */
    private void addIngredientEditRow(InstructionEntry entry) {
        HBox row = new HBox(10); // the spacing between the components
        row.setAlignment(Pos.CENTER_LEFT);

        TextField quantityField = new TextField(entry != null ? String.valueOf(entry.getQuantity()) : "");
        quantityField.setPromptText("Weight");
        quantityField.setPrefWidth(60);

        TextField unitField = new TextField(entry != null ? entry.getUnit() : "");
        unitField.setPromptText("Unit");
        unitField.setPrefWidth(80);

        TextField ingredientNameField = new TextField(entry != null && entry.getIngredient() != null ? entry.getIngredient().getName() : "");
        ingredientNameField.setPromptText("Ingredient Name");
        ingredientNameField.setPrefWidth(150);

        Button removeButton = new Button("Delete");
        removeButton.setOnAction(e -> {
            ingredientListBox.getChildren().remove(row); // remove from the UI
            ingredientEditRows.removeIf(r -> r.quantityField == quantityField && r.unitField == unitField && r.ingredientNameField == ingredientNameField); // remove the reference from the list
        });

        row.getChildren().addAll(quantityField, unitField, ingredientNameField, removeButton);
        ingredientListBox.getChildren().add(row);

        // store the reference to this edit row
        ingredientEditRows.add(new IngredientEditRow(entry, quantityField, unitField, ingredientNameField));
    }

    /**
     * dynamically add an editable instruction row.
     * if the instruction is null, add an empty row (for new instruction).
     */
    private void addInstructionEditRow(Instruction instruction) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.TOP_LEFT);

        Label stepNumberLabel = new Label(); // the step number will be set in recalculateStepNumbers
        stepNumberLabel.setStyle("-fx-font-weight: bold;");

        TextArea descriptionArea = new TextArea(instruction != null ? instruction.getDescription() : "");
        descriptionArea.setPromptText("Step");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(2);
        descriptionArea.setPrefWidth(350);

        Button removeButton = new Button("Delete");
        removeButton.setOnAction(e -> {
            stepsListBox.getChildren().remove(row); // remove from the UI
            instructionEditRows.removeIf(r -> r.descriptionArea == descriptionArea); // remove the reference from the list
            recalculateStepNumbers(); // remove the reference after recalculate the step numbers
        });

        row.getChildren().addAll(stepNumberLabel, descriptionArea, removeButton);
        stepsListBox.getChildren().add(row);

        // store the reference to this edit row
        instructionEditRows.add(new InstructionEditRow(instruction, stepNumberLabel, descriptionArea));
        recalculateStepNumbers(); // add the reference after recalculate the step numbers
    }

    /**
     * recalculate and update the step numbers after adding/removing steps.
     */
    private void recalculateStepNumbers() {
        for (int i = 0; i < instructionEditRows.size(); i++) {
            instructionEditRows.get(i).stepNumberLabel.setText((i + 1) + ".");
        }
    }


    @FXML
    private void handleAddIngredient(ActionEvent event) {
        addIngredientEditRow(null); // add a new empty ingredient row
    }

    @FXML
    private void handleAddInstruction(ActionEvent event) {
        addInstructionEditRow(null); // add a new empty instruction row
    }

    @FXML
    private void handleEditCommit(ActionEvent event) {
        if (isEditing) {
            // switch from edit mode to commit mode
            commitChanges();
        } else {
            // switch from display mode to edit mode
            setEditingMode(true);
        }
    }

    /**
     * commit the changes to the database and update the UI.
     */
    private void commitChanges() {
        if (recipe == null) {
            showErrorDialog("Error", "No recipe selected to commit changes.");
            return;
        }

        // 1. get the new values from the edit fields
        String newName = editRecipeNameField.getText();
        String newDescription = editDescriptionArea.getText();
        int newServings;
        try {
            newServings = Integer.parseInt(editServingField.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Input error", "Serving must be a valid number.");
            return;
        }

        // === get new image path and region ===
        String newImagePath = this.currentImagePath; // use currentImagePath which is updated by handleUploadImage
        Region newRegion = editRegionComboBox.getSelectionModel().getSelectedItem();

        // === add the scale factor logic ===
        int oldServings = recipe.getServings(); // the original serving
        if (oldServings > 0 && newServings > 0 && oldServings != newServings) {
            double scale = (double) newServings / oldServings;
            System.out.println("Serving scale: " + scale);

            for (IngredientEditRow row : ingredientEditRows) {
                String quantityStr = row.quantityField.getText();
                if (quantityStr == null || quantityStr.isEmpty()) continue;

                try {
                    int originalQuantity = Integer.parseInt(quantityStr);
                    int scaledQuantity = (int) Math.round(originalQuantity * scale);
                    row.quantityField.setText(String.valueOf(scaledQuantity));
                } catch (NumberFormatException e) {
                    System.err.println("Failed to scale quantity: " + quantityStr);
                }
            }
        }

        // 2. update the Recipe object
        recipe.setName(newName);
        recipe.setDescription(newDescription);
        recipe.setServings(newServings);
        recipe.setImagePath(newImagePath); // update image path
        recipe.setRegion(newRegion); // update region

        // 3. save the changes of the Recipe to the database
        try {
            recipeDAO.updateRecipe(recipe); // call the update method of RecipeDAO
            System.out.println("Recipe basic information updated successfully: " + recipe.getName()); 

            // 4. handle the update of the ingredients and steps
            updateIngredientsAndInstructions();

            showInfoDialog("Success", "Recipe '" + recipe.getName() + "' updated successfully!");

            // 5. update the UI to display mode
            setEditingMode(false);
            // redisplay the updated data
            previewRecipeName.setText(recipe.getName());
            previewDescription.setText(recipe.getDescription());
            previewServing.setText(String.valueOf(recipe.getServings()));
            previewRegion.setText(recipe.getRegion() != null ? recipe.getRegion().getName() : "Unknown Region"); // update region display

        } catch (SQLException e) {
            showErrorDialog("Database error", "Error updating recipe: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) { // Catch the custom exception for input errors
            showErrorDialog("Input Error", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { // catch other possible exceptions
            showErrorDialog("Error", "Unknown error occurred while processing ingredients or steps: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateIngredientsAndInstructions() throws SQLException {
        InstructionEntryDAO instructionEntryDAO = new InstructionEntryDAOImpl();
        IngredientDAO ingredientDAO = new IngredientDAOImpl();
        InstructionDAO instructionDAO = new InstructionDAOImpl();

        // 1. save all the ingredient rows in the GUI
        List<InstructionEntry> newEntries = new ArrayList<>();
        for (IngredientEditRow row : ingredientEditRows) {
            String quantityStr = row.quantityField.getText().trim();
            String unit = row.unitField.getText().trim().toLowerCase();
            String name = row.ingredientNameField.getText().trim();

            if (quantityStr.isEmpty() || unit.isEmpty() || name.isEmpty()) {
                throw new IllegalArgumentException("Ingredient quantity, unit, and name cannot be empty.");
            }

            double quantity;
            try {
                quantity = Double.parseDouble(quantityStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid quantity format for ingredient \'" + name + "\': \'" + quantityStr + "\'. Quantity must be a valid number.");
            }

            Ingredient ingredient = ingredientDAO.getIngredientByName(name);
            if (ingredient == null) {
                ingredient = new Ingredient(name);
                if (!ingredientDAO.insertIngredient(ingredient)) {
                    System.err.println("Failed to insert new ingredient: " + name);
                    continue;
                }
                ingredient = ingredientDAO.getIngredientByName(name);
            }

            InstructionEntry entry = new InstructionEntry();
            entry.setRecipe(recipe);
            entry.setIngredient(ingredient);
            entry.setQuantity(quantity);
            entry.setUnit(unit);
            newEntries.add(entry);
        }

        // Debug print
        System.out.println("Ingredients to save:");
        for (InstructionEntry e : newEntries) {
            System.out.println(" - " + e.getIngredient().getName() + " | " + e.getQuantity() + " " + e.getUnit());
        }

        // 2. delete the old data
        instructionEntryDAO.deleteInstructionEntriesByRecipeId(recipe.getId());
        System.out.println("Deleted old ingredient entries");

        // 3. insert the new data
        for (InstructionEntry e : newEntries) {
            instructionEntryDAO.insertInstructionEntry(e);
            System.out.println("Inserted ingredient: " + e.getIngredient().getName());
        }


    // 2. handle the steps (Instruction)
        InstructionDAO InstructionDAO = new InstructionDAOImpl();
        List<Instruction> oldInstructions = instructionDAO.getInstructionsByRecipeId(recipe.getId());
        List<Instruction> newOrUpdatedInstructions = new ArrayList<>();

        // iterate the instructionEditRows
        int currentStepNumber = 1;
        for (InstructionEditRow row : instructionEditRows) {
            String description = row.descriptionArea.getText();
            if (description.isEmpty()) {
                System.err.println("Invalid instruction row, skip");
                continue;
            }

            if (row.originalInstruction != null) {
                // update the existing step
                row.originalInstruction.setDescription(description);
                row.originalInstruction.setStepNumber(currentStepNumber); // update the step number
                newOrUpdatedInstructions.add(row.originalInstruction);
            } else {
                // add a new step
                Instruction newInstruction = new Instruction();
                newInstruction.setRecipe(recipe);
                newInstruction.setStepNumber(currentStepNumber);
                newInstruction.setDescription(description);
                newOrUpdatedInstructions.add(newInstruction);
            }
            currentStepNumber++;
        }

        // delete the steps that exist in the old data but not in the new data
        if (oldInstructions != null) {
            for (Instruction oldInst : oldInstructions) {
                boolean foundInNew = false;
                for (Instruction newInst : newOrUpdatedInstructions) {
                    // use the composite primary key (recipe_id and stepNumber) to determine if it is the same step
                    if (oldInst.getRecipe().getId() == newInst.getRecipe().getId() && 
                        oldInst.getStepNumber() == newInst.getStepNumber()) {
                        foundInNew = true;
                        break;
                    }
                }
                if (!foundInNew) {
                    // the old step is not in the new list, it means it is deleted
                    // call the new deleteInstruction method, pass in recipeId and stepNumber
                    instructionDAO.deleteInstruction(oldInst.getRecipe().getId(), oldInst.getStepNumber());
                    System.out.println("Deleted step: Recipe ID: " + oldInst.getRecipe().getId() + ", Step Number: " + oldInst.getStepNumber());
                }
            }
        }

        // insert or update all the new data and modified data
        for (Instruction instruction : newOrUpdatedInstructions) {
            // for Instruction, we do not have an independent ID field to determine if it is a new or updated record
            // simply try to update, if the update fails (Affected Rows = 0), it means the record does not exist, execute insert
            boolean updated = instructionDAO.updateInstruction(instruction);
            if (!updated) {
                // if the update fails, it means this is a new record, try to insert
                instructionDAO.insertInstruction(instruction);
                System.out.println("Inserted new step: " + instruction.getDescription());
            } else {
                System.out.println("Updated step: " + instruction.getDescription() + " (Recipe ID: " + instruction.getRecipe().getId() + ", Step Number: " + instruction.getStepNumber() + ")");
            }
        }
    }

    @FXML
    private void handleCloseWindow() {
        previewRecipeName.getScene().getWindow().hide();
    }

    @FXML
    private void handleDeleteRecipe() {
        if (recipe == null) {
            showErrorDialog("Error", "No recipe selected to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm delete");
        confirmAlert.setHeaderText("Are you sure you want to delete this recipe?");
        confirmAlert.setContentText("Deleting it cannot be recovered: " + recipe.getName());

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    boolean deleted = recipeDAO.deleteRecipe(recipe.getId());
                    if (deleted) {
                        showInfoDialog("Success", "Recipe '" + recipe.getName() + "' deleted successfully.");
                        previewRecipeName.getScene().getWindow().hide(); // close the current window
                    } else {
                        showErrorDialog("Failed", "Failed to delete recipe '" + recipe.getName() + "'.");
                    }
                } catch (SQLException e) {
                    showErrorDialog("Database error", "Error deleting recipe: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleScaleServingButton() {
        // switch the visibility of the scale input box
        scaleInputBox.setVisible(!scaleInputBox.isVisible());
        // reset the input box content after each click of the scale button
        scaleMultiplierField.setText("");
        // if hidden, ensure to display the original data
        if (!scaleInputBox.isVisible()) {
            displayIngredientsWithScale(1);
        }
    }

    @FXML
    private void handleConfirmScale() {
        String multiplierText = scaleMultiplierField.getText();
        if (multiplierText == null || multiplierText.trim().isEmpty()) {
            showErrorDialog("Input error", "Please enter a valid scale factor.");
            return;
        }

        try {
            int scaleFactor = Integer.parseInt(multiplierText.trim());
            if (scaleFactor <= 0) {
                showErrorDialog("Input error", "Scale factor must be a positive integer.");
                return;
            }
            displayIngredientsWithScale(scaleFactor);
            scaleInputBox.setVisible(false); // hide the input box after confirmation
        } catch (NumberFormatException e) {
            showErrorDialog("Input error", "Scale factor must be a valid integer.");
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

                String originalFileName = selectedFile.getName();
                String fileExtension = "";
                int dotIndex = originalFileName.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
                    fileExtension = originalFileName.substring(dotIndex); // includes the dot, e.g., ".png"
                }

                // Get recipe name from editRecipeNameField and sanitize it
                String recipeName = editRecipeNameField.getText().trim();
                String sanitizedRecipeName = recipeName.isEmpty() ? "unknown_recipe" : recipeName.replaceAll("[^a-zA-Z0-9\\s]", "").trim().replaceAll("\\s+", "_");

                // Generate new file name: recipeName_image_timestamp.extension
                String newFileName = sanitizedRecipeName + "_image_" + System.currentTimeMillis() + fileExtension;
                Path destinationPath = new File(destDir, newFileName).toPath();

                // copy the file to src/main/resources
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // Determine the project root directory more robustly
                String projectRoot = System.getProperty("user.dir");

                // Construct the path for target/classes directory
                Path targetClassesDestDir = Paths.get(projectRoot, "target", "classes", "groupf", "recipeapp", "images");
                if (!Files.exists(targetClassesDestDir)) {
                    Files.createDirectories(targetClassesDestDir); // Create directories if they don't exist
                }
                Path targetClassesDestinationPath = targetClassesDestDir.resolve(newFileName);

                // Copy the file to target/classes
                Files.copy(selectedFile.toPath(), targetClassesDestinationPath, StandardCopyOption.REPLACE_EXISTING);

                // store the relative path, this is the path we will save to the database
                this.currentImagePath = "/groupf/recipeapp/images/" + newFileName;
                editImagePathField.setText(newFileName); // display the new file name on the UI

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Image Upload Success");
                alert.setHeaderText(null);
                alert.setContentText("Image uploaded successfully to local resources with new name: " + newFileName);
                alert.showAndWait();

            } catch (IOException e) {
                showErrorDialog("Error", "Failed to copy image file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * show the information dialog.
     */
    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * show the error dialog.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
