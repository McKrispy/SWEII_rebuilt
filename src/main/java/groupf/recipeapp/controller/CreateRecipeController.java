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
import java.io.IOException;
import groupf.recipeapp.entity.Recipe;
import groupf.recipeapp.entity.Ingredient;
import groupf.recipeapp.entity.Instruction;
import groupf.recipeapp.entity.InstructionEntry;
import groupf.recipeapp.entity.Region;
import groupf.recipeapp.dao.RecipeDAO; // 新增导入
import groupf.recipeapp.dao.RecipeDAOImpl; // 新增导入

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

        if (name.isEmpty() || servingsStr.isEmpty()) {
            showError("Recipe name and servings are required.");
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

        // 收集 ingredientsBox 中的输入项
        for (Node node : ingredientsBox.getChildren()) {
            if (node instanceof HBox hbox && hbox.getChildren().size() == 3) {
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

        // 收集 instructionsBox 中的输入项
        int stepNumber = 1;
        for (Node node : instructionsBox.getChildren()) {
            if (node instanceof TextField field) {
                String stepDescription = field.getText().trim();
                recipe.addInstruction(new Instruction(stepNumber++, stepDescription));
            }
        }

        // 插入数据库（你应确保有对应的方法）
        RecipeDAO recipeDAO = new RecipeDAOImpl();
        boolean success = recipeDAO.insertRecipe(recipe);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Recipe Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Recipe \"" + name + "\" submitted successfully!");
            alert.showAndWait();
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
        System.out.println("左侧导航按钮：Search Recipes 被点击");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/groupf/recipeapp/fxml/MainView.fxml"));
            Parent root = loader.load();  // 加载FXML文件

            // 获取当前窗口Stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 创建一个新的Scene并设置到Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Recipe List");

        } catch (IOException e) {
            e.printStackTrace();
            // 可选: 弹出错误提示
        }
    }

}