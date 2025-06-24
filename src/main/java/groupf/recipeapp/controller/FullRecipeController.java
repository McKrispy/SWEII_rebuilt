package groupf.recipeapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import groupf.recipeapp.entity.Recipe;
import groupf.recipeapp.entity.InstructionEntry;
import groupf.recipeapp.entity.Instruction;


import groupf.recipeapp.dao.InstructionEntryDAO;
import groupf.recipeapp.dao.InstructionEntryDAOImpl;
import groupf.recipeapp.dao.InstructionDAO;
import groupf.recipeapp.dao.InstructionDAOImpl;

import java.util.List;

public class FullRecipeController {

    @FXML
    private Label previewRecipeName;

    @FXML
    private Label previewDescription;

    @FXML
    private ImageView previewImageView;

    @FXML
    private VBox ingredientListBox;

    @FXML
    private VBox stepsListBox;

    @FXML
    private Label previewServing;



    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        if (recipe == null) {
            System.err.println("❌ 传入的 recipe 是 null！");
            return;
        }

        System.out.println("📝 加载食谱：" + recipe.getName() + "，ID = " + recipe.getId());

        // 显示基本信息
        previewRecipeName.setText(recipe.getName());
        previewDescription.setText(recipe.getDescription());

        // 显示图片
        if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(recipe.getImagePath()));
                previewImageView.setImage(image);
                System.out.println("🖼️ 图片加载成功：" + recipe.getImagePath());
            } catch (Exception e) {
                previewImageView.setImage(null);
                System.err.println("⚠️ 图片加载失败：" + recipe.getImagePath() + "，错误信息：" + e.getMessage());
            }
        }

        // 加载指令条目
        InstructionEntryDAO instructionEntryDAO = new InstructionEntryDAOImpl();
        List<InstructionEntry> entries = instructionEntryDAO.getInstructionEntriesByRecipeId(recipe.getId());

        ingredientListBox.getChildren().clear();

        if (entries == null || entries.isEmpty()) {
            ingredientListBox.getChildren().add(new Label("No ingredients found."));
            System.out.println("⚠️ 没有找到配料指令！");
        } else {
            int step = 1;
            for (InstructionEntry entry : entries) {
                String ingredientName = entry.getIngredient().getName();
                String stepText = "Ingredient " + step++ + ": " + entry.getQuantity() + " " + entry.getUnit() + " " + ingredientName;
                ingredientListBox.getChildren().add(new Label(stepText));
                System.out.println("➡️ 显示指令：" + stepText);
            }
        }

        // **显示制作步骤**
        InstructionDAO instructionDAO = new InstructionDAOImpl();
        List<Instruction> instructions = instructionDAO.getInstructionsByRecipeId(recipe.getId());

        stepsListBox.getChildren().clear();

        if (instructions == null || instructions.isEmpty()) {
            stepsListBox.getChildren().add(new Label("No instructions found."));
            System.out.println("⚠️ 没有找到制作步骤！");
        } else {
            for (Instruction instruction : instructions) {
                String stepText = "Step " + instruction.getStepNumber() + ": " + instruction.getDescription();
                stepsListBox.getChildren().add(new Label(stepText));
                System.out.println("➡️ 显示步骤：" + stepText);
            }
        }
        previewServing.setText(String.valueOf(recipe.getServings()));

    }

    @FXML
    private void handleCloseWindow() {
        previewRecipeName.getScene().getWindow().hide();
    }
}
