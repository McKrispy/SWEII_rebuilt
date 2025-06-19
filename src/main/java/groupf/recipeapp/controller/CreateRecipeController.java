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
        String name = recipeNameField.getText();
        String servings = servingsField.getText();
        String description = descriptionArea.getText();

        // 你可以在这里添加逻辑来收集ingredients和instructions
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recipe Submitted");
        alert.setHeaderText(null);
        alert.setContentText("Recipe \"" + name + "\" submitted successfully!");
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