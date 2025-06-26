package groupf.recipeapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea; // 新增导入
import javafx.scene.control.TextField; // 新增导入
import javafx.scene.layout.HBox; // 新增导入 HBox
import javafx.geometry.Pos; // 新增导入 Pos

import groupf.recipeapp.entity.Recipe;
import groupf.recipeapp.entity.InstructionEntry;
import groupf.recipeapp.entity.Instruction;
import groupf.recipeapp.entity.Ingredient; 


import groupf.recipeapp.dao.InstructionEntryDAO;
import groupf.recipeapp.dao.InstructionEntryDAOImpl;
import groupf.recipeapp.dao.InstructionDAO;
import groupf.recipeapp.dao.InstructionDAOImpl;
import groupf.recipeapp.dao.RecipeDAO; // 新增导入
import groupf.recipeapp.dao.RecipeDAOImpl; // 新增导入
import groupf.recipeapp.dao.IngredientDAO; // 新增导入 IngredientDAO
import groupf.recipeapp.dao.IngredientDAOImpl;

import java.util.*;

import javafx.event.ActionEvent; // 新增导入
import javafx.scene.control.Button; // 新增导入
import javafx.scene.control.Alert; // 新增导入
import java.sql.SQLException; // 新增导入
import java.util.regex.Matcher; // 新增导入
import java.util.regex.Pattern; // 新增导入

public class FullRecipeController {

    @FXML
    private Label previewRecipeName;

    @FXML
    private TextField editRecipeNameField; // 新增 FXML 字段


    @FXML
    private Label previewDescription;

    @FXML
    private TextArea editDescriptionArea; // 新增 FXML 字段

    @FXML
    private ImageView previewImageView;

    @FXML
    private VBox ingredientListBox;

    @FXML
    private VBox stepsListBox;

    @FXML
    private Label previewServing;

    @FXML
    private TextField editServingField; // 新增 FXML 字段


    @FXML
    private Button editCommitButton; // 新增 FXML 字段

    @FXML
    private HBox addIngredientButtonBox; // 新增 FXML 字段
    @FXML
    private HBox addInstructionButtonBox; // 新增 FXML 字段

    @FXML
    private Button deleteButton; // 新增 FXML 字段

    @FXML
    private Button scaleServingButton; // 新增 FXML 字段

    @FXML
    private HBox scaleInputBox; // 新增 FXML 字段

    @FXML
    private TextField scaleMultiplierField; // 新增 FXML 字段

    private Recipe recipe;
    private boolean isEditing = false; // 新增：跟踪是否处于编辑模式
    private RecipeDAO recipeDAO; // 新增：用于数据库操作
    private IngredientDAO ingredientDAO; // 新增：用于食材数据库操作

    // 新增：用于存储动态创建的食材编辑行和步骤编辑行的引用
    private List<IngredientEditRow> ingredientEditRows = new ArrayList<>();
    private List<InstructionEditRow> instructionEditRows = new ArrayList<>();
    private List<InstructionEntry> originalIngredients; // 新增：存储原始食材列表

    // 内部类：用于封装食材编辑行的UI控件和数据
    private static class IngredientEditRow {
        InstructionEntry originalEntry; // 关联的原始 InstructionEntry 对象 (如果存在)
        TextField quantityField;
        TextField unitField;
        TextField ingredientNameField; // 存储食材名称的 TextField
        // Button removeButton; // 引用按钮，如果需要对按钮本身进行操作

        public IngredientEditRow(InstructionEntry originalEntry, TextField quantityField, TextField unitField, TextField ingredientNameField) {
            this.originalEntry = originalEntry;
            this.quantityField = quantityField;
            this.unitField = unitField;
            this.ingredientNameField = ingredientNameField;
        }
    }

    // 内部类：用于封装步骤编辑行的UI控件和数据
    private static class InstructionEditRow {
        Instruction originalInstruction; // 关联的原始 Instruction 对象 (如果存在)
        Label stepNumberLabel; // 步骤编号的Label
        TextArea descriptionArea; // 步骤描述的TextArea
        // Button removeButton; // 引用按钮，如果需要对按钮本身进行操作

        public InstructionEditRow(Instruction originalInstruction, Label stepNumberLabel, TextArea descriptionArea) {
            this.originalInstruction = originalInstruction;
            this.stepNumberLabel = stepNumberLabel;
            this.descriptionArea = descriptionArea;
        }
    }

    // 构造函数：初始化DAO
    public FullRecipeController() {
        this.recipeDAO = new RecipeDAOImpl();
        this.ingredientDAO = new IngredientDAOImpl(); // 初始化 IngredientDAO
    }

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
        previewServing.setText(String.valueOf(recipe.getServings()));

        // 初始化时处于显示模式
        setEditingMode(false);

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

        // 加载并显示食材和步骤（初始为只读模式）
        loadAndDisplayIngredients();
        loadAndDisplayInstructions();
    }

     /**
     * Helper to load and display ingredients (read-only labels).
     */
    private void loadAndDisplayIngredients() {
        ingredientListBox.getChildren().clear();
        InstructionEntryDAO instructionEntryDAO = new InstructionEntryDAOImpl();
        originalIngredients = instructionEntryDAO.getInstructionEntriesByRecipeId(recipe.getId()); // 加载并存储原始数据

        if (originalIngredients == null || originalIngredients.isEmpty()) {
            ingredientListBox.getChildren().add(new Label("No ingredients found."));
            System.out.println("⚠️ 没有找到配料指令！");
        } else {
            displayIngredientsWithScale(1); // 默认显示原始数量（缩放倍数为1）
        }
    }

    /**
     * Helper to display ingredients with a given scale factor.
     * @param scaleFactor 缩放倍数。
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
            // 乘以缩放倍数并显示
            String displayText = "Ingredient " + index++ + ": " + (entry.getQuantity() * scaleFactor) + " " + entry.getUnit() + " " + ingredientName;
            ingredientListBox.getChildren().add(new Label(displayText));
            System.out.println("➡️ 显示指令 (缩放后)：" + displayText);
        }
    }

    /**
     * Helper to load and display instructions (read-only labels).
     */
    private void loadAndDisplayInstructions() {
        stepsListBox.getChildren().clear();
        InstructionDAO instructionDAO = new InstructionDAOImpl();
        List<Instruction> instructions = instructionDAO.getInstructionsByRecipeId(recipe.getId());

        // 按照步骤编号排序
        instructions.sort(Comparator.comparingInt(Instruction::getStepNumber));

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
    }

    /**
     * 切换编辑和显示模式的UI状态。
     * @param editing true表示进入编辑模式，false表示进入显示模式。
     */
    private void setEditingMode(boolean editing) {
        this.isEditing = editing;

        // 切换食谱名称的显示/编辑
        previewRecipeName.setVisible(!editing);
        editRecipeNameField.setVisible(editing);
        if (editing) {
            editRecipeNameField.setText(recipe.getName());
        }

        // 切换描述的显示/编辑
        previewDescription.setVisible(!editing);
        editDescriptionArea.setVisible(editing);
        if (editing) {
            editDescriptionArea.setText(recipe.getDescription());
        }

        // 切换份量的显示/编辑
        previewServing.setVisible(!editing);
        editServingField.setVisible(editing);
        if (editing) {
            editServingField.setText(String.valueOf(recipe.getServings()));
        }

        // 切换按钮文本
        editCommitButton.setText(editing ? "Commit" : "Edit");

        // 切换“添加”按钮容器的可见性
        addIngredientButtonBox.setVisible(editing);
        addInstructionButtonBox.setVisible(editing);

        // 缩放按钮和输入框的可见性：编辑模式下隐藏，显示模式下显示
        scaleServingButton.setVisible(!editing);
        scaleInputBox.setVisible(false); // 无论如何，切换模式时隐藏缩放输入框

        // 处理食材和步骤的动态内容
        if (editing) {
            // 清空当前显示，并用可编辑字段填充
            ingredientListBox.getChildren().clear();
            ingredientEditRows.clear(); // 清除现有引用，避免重复
            loadIngredientsForEditing();

            stepsListBox.getChildren().clear();
            instructionEditRows.clear(); // 清除现有引用
            loadInstructionsForEditing();

        } else {
            // 恢复到只读标签显示
            loadAndDisplayIngredients();
            loadAndDisplayInstructions();
            ingredientEditRows.clear(); // 清除编辑行引用
            instructionEditRows.clear(); // 清除编辑行引用
        }
    }

    /**
     * 加载现有食材并以可编辑字段显示。
     */
  /*  private void loadIngredientsForEditing() {
        InstructionEntryDAO instructionEntryDAO = new InstructionEntryDAOImpl();
        List<InstructionEntry> entries = instructionEntryDAO.getInstructionEntriesByRecipeId(recipe.getId());

        if (entries != null) {
            for (InstructionEntry entry : entries) {
                addIngredientEditRow(entry); // 为现有食材添加编辑行
            }
        }
    }*/
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
        System.out.println("🔢 加载食材编辑行数: " + ingredientEditRows.size());

    }

    /**
     * 加载现有步骤并以可编辑字段显示。
     */
    private void loadInstructionsForEditing() {
        InstructionDAO instructionDAO = new InstructionDAOImpl();
        List<Instruction> instructions = instructionDAO.getInstructionsByRecipeId(recipe.getId());

        // 按照步骤编号排序
        instructions.sort(Comparator.comparingInt(Instruction::getStepNumber));

        if (instructions != null) {
            for (Instruction instruction : instructions) {
                addInstructionEditRow(instruction); // 为现有步骤添加编辑行
            }
        }
    }

    /**
     * 动态添加一个可编辑的食材行。
     * 如果 entry 为 null，则添加一个空的行（用于新增食材）。
     */
    private void addIngredientEditRow(InstructionEntry entry) {
        HBox row = new HBox(10); // 控件之间的间距
        row.setAlignment(Pos.CENTER_LEFT);

        TextField quantityField = new TextField(entry != null ? String.valueOf(entry.getQuantity()) : "");
        quantityField.setPromptText("数量");
        quantityField.setPrefWidth(60);

        TextField unitField = new TextField(entry != null ? entry.getUnit() : "");
        unitField.setPromptText("单位");
        unitField.setPrefWidth(80);

        TextField ingredientNameField = new TextField(entry != null && entry.getIngredient() != null ? entry.getIngredient().getName() : "");
        ingredientNameField.setPromptText("食材名称");
        ingredientNameField.setPrefWidth(150);

        Button removeButton = new Button("移除");
        removeButton.setOnAction(e -> {
            ingredientListBox.getChildren().remove(row); // 从UI移除
            ingredientEditRows.removeIf(r -> r.quantityField == quantityField && r.unitField == unitField && r.ingredientNameField == ingredientNameField); // 从列表中移除引用
        });

        row.getChildren().addAll(quantityField, unitField, ingredientNameField, removeButton);
        ingredientListBox.getChildren().add(row);

        // 存储对这个编辑行的引用
        ingredientEditRows.add(new IngredientEditRow(entry, quantityField, unitField, ingredientNameField));
    }

    /**
     * 动态添加一个可编辑的步骤行。
     * 如果 instruction 为 null，则添加一个空的行（用于新增步骤）。
     */
    private void addInstructionEditRow(Instruction instruction) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.TOP_LEFT);

        Label stepNumberLabel = new Label(); // 步骤编号会在recalculateStepNumbers中设置
        stepNumberLabel.setStyle("-fx-font-weight: bold;");

        TextArea descriptionArea = new TextArea(instruction != null ? instruction.getDescription() : "");
        descriptionArea.setPromptText("步骤描述");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(2);
        descriptionArea.setPrefWidth(350);

        Button removeButton = new Button("移除");
        removeButton.setOnAction(e -> {
            stepsListBox.getChildren().remove(row); // 从UI移除
            instructionEditRows.removeIf(r -> r.descriptionArea == descriptionArea); // 从列表中移除引用
            recalculateStepNumbers(); // 移除后重新计算步骤编号
        });

        row.getChildren().addAll(stepNumberLabel, descriptionArea, removeButton);
        stepsListBox.getChildren().add(row);

        // 存储对这个编辑行的引用
        instructionEditRows.add(new InstructionEditRow(instruction, stepNumberLabel, descriptionArea));
        recalculateStepNumbers(); // 添加后重新计算步骤编号
    }

    /**
     * 在添加/移除步骤后重新计算并更新步骤编号。
     */
    private void recalculateStepNumbers() {
        for (int i = 0; i < instructionEditRows.size(); i++) {
            instructionEditRows.get(i).stepNumberLabel.setText((i + 1) + ".");
        }
    }


    @FXML
    private void handleAddIngredient(ActionEvent event) {
        addIngredientEditRow(null); // 添加一个新的空食材行
    }

    @FXML
    private void handleAddInstruction(ActionEvent event) {
        addInstructionEditRow(null); // 添加一个新的空步骤行
    }

    @FXML
    private void handleEditCommit(ActionEvent event) {
        if (isEditing) {
            // 从编辑模式切换到提交模式
            commitChanges();
        } else {
            // 从显示模式切换到编辑模式
            setEditingMode(true);
        }
    }

    /**
     * 提交更改到数据库并更新UI。
     */
    private void commitChanges() {
        if (recipe == null) {
            showErrorDialog("错误", "没有选中的食谱可以提交更改。");
            return;
        }

        // 1. 从编辑字段获取主食谱的新值
        String newName = editRecipeNameField.getText();
        String newDescription = editDescriptionArea.getText();
        int newServings;
        try {
            newServings = Integer.parseInt(editServingField.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("输入错误", "份量必须是有效的数字。");
            return;
        }

        // === ✅ 加入比例缩放逻辑 ===
        int oldServings = recipe.getServings(); // 原始份量
        if (oldServings > 0 && newServings > 0 && oldServings != newServings) {
            double scale = (double) newServings / oldServings;
            System.out.println("🔁 份量变化比例：" + scale);

            for (IngredientEditRow row : ingredientEditRows) {
                String quantityStr = row.quantityField.getText();
                if (quantityStr == null || quantityStr.isEmpty()) continue;

                try {
                    int originalQuantity = Integer.parseInt(quantityStr);
                    int scaledQuantity = (int) Math.round(originalQuantity * scale);
                    row.quantityField.setText(String.valueOf(scaledQuantity));
                } catch (NumberFormatException e) {
                    System.err.println("无法缩放数量：" + quantityStr);
                }
            }
        }

        // 2. 更新 Recipe 对象
        recipe.setName(newName);
        recipe.setDescription(newDescription);
        recipe.setServings(newServings);

        // 3. 将 Recipe 的更改保存到数据库
        try {
            recipeDAO.updateRecipe(recipe); // 调用 RecipeDAO 的更新方法
            System.out.println("✅ 食谱基本信息更新成功：" + recipe.getName());

            // 4. 处理食材和步骤的更新
            updateIngredientsAndInstructions();

            showInfoDialog("成功", "食谱 '" + recipe.getName() + "' 已成功更新！");

            // 5. 更新UI到显示模式
            setEditingMode(false);
            // 重新显示更新后的数据
            previewRecipeName.setText(recipe.getName());
            previewDescription.setText(recipe.getDescription());
            previewServing.setText(String.valueOf(recipe.getServings()));

        } catch (SQLException e) {
            showErrorDialog("数据库错误", "更新食谱时发生错误：" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { // 捕获其他可能的异常
            showErrorDialog("错误", "处理食材或步骤时发生未知错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新食材和步骤到数据库。
     * 这是一个更复杂的方法，需要处理增、删、改。
     */
   /* private void updateIngredientsAndInstructions() throws SQLException {
        InstructionEntryDAO instructionEntryDAO = new InstructionEntryDAOImpl();
        InstructionDAO instructionDAO = new InstructionDAOImpl();

        // 1. 处理食材 (InstructionEntry)
        // 获取当前数据库中的所有食材（旧数据）
        List<InstructionEntry> oldEntries = instructionEntryDAO.getInstructionEntriesByRecipeId(recipe.getId());
        
        // 用于跟踪在编辑模式下被修改或新增的食材
        List<InstructionEntry> newOrUpdatedEntries = new ArrayList<>();
        
        // 遍历 ingredientEditRows (用户在UI中看到和操作的食材)
        for (IngredientEditRow row : ingredientEditRows) {
            String quantityStr = row.quantityField.getText();
            String unit = row.unitField.getText();
            String ingredientName = row.ingredientNameField.getText();

            // 验证输入
            if (quantityStr.isEmpty() || unit.isEmpty() || ingredientName.isEmpty()) {
                System.err.println("跳过无效的食材行：数量、单位或食材名称为空。");
                continue; // 跳过无效行
            }
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                System.err.println("无效的食材数量：" + quantityStr);
                continue; // 跳过无效行
            }

            // 获取或创建 Ingredient 对象
            Ingredient ingredient = ingredientDAO.getIngredientByName(ingredientName);
            if (ingredient == null) {
                // 如果食材不存在，则创建新食材
                ingredient = new Ingredient(ingredientName);
                boolean inserted = ingredientDAO.insertIngredient(ingredient);
                if (!inserted) {
                    System.err.println("无法插入新食材：" + ingredientName);
                    continue;
                }
                // 重新获取包含ID的食材对象
                ingredient = ingredientDAO.getIngredientByName(ingredientName);
                if (ingredient == null) {
                    System.err.println("无法获取新插入食材的ID：" + ingredientName);
                    continue;
                }
            }

            if (row.originalEntry != null) {
                // 这是现有食材的更新
                row.originalEntry.setQuantity(quantity);
                row.originalEntry.setUnit(unit);
                row.originalEntry.setIngredient(ingredient); // 更新食材对象
                newOrUpdatedEntries.add(row.originalEntry);
            } else {
                // 这是新增食材
                InstructionEntry newEntry = new InstructionEntry();
                newEntry.setRecipe(recipe);
                newEntry.setQuantity(quantity);
                newEntry.setUnit(unit);
                newEntry.setIngredient(ingredient);
                newOrUpdatedEntries.add(newEntry);
            }
        }

        // 删除在旧数据中存在但在新数据中不存在的食材
        if (oldEntries != null) {
            for (InstructionEntry oldEntry : oldEntries) {
                boolean foundInNew = false;
                for (InstructionEntry newEntry : newOrUpdatedEntries) {
                    // 使用复合主键 (recipe_id 和 ingredient_id) 来判断是否是同一个条目
                    // oldEntry 和 newEntry 的 recipe 都应该指向当前菜谱，所以主要比较 ingredient
                    if (oldEntry.getIngredient() != null && newEntry.getIngredient() != null &&
                        oldEntry.getRecipe().getId() == newEntry.getRecipe().getId() && // 修正：从 .equals() 改为 ==
                        oldEntry.getIngredient().getId() == newEntry.getIngredient().getId()) { // 修正：从 .equals() 改为 ==
                        foundInNew = true;
                        break;
                    }
                }
                if (!foundInNew) {
                    // 旧条目不在新列表中，说明它被删除了
                    // 调用新的 deleteInstructionEntry 方法，传入 recipeId 和 ingredientId
                    instructionEntryDAO.deleteInstructionEntry(oldEntry.getRecipe().getId(), oldEntry.getIngredient().getId());
                    System.out.println("🗑️ 删除食材条目：Recipe ID: " + oldEntry.getRecipe().getId() + ", Ingredient ID: " + oldEntry.getIngredient().getId());
                }
            }
        }
        
        // 插入或更新所有新数据和修改过的数据
        for (InstructionEntry entry : newOrUpdatedEntries) {
            // 对于 InstructionEntry，我们没有 ID 字段来判断是新增还是更新
            // 简单地尝试更新，如果更新失败（Affected Rows = 0），则说明记录不存在，执行插入
            boolean updated = instructionEntryDAO.updateInstructionEntry(entry);
            if (!updated) {
                // 如果更新失败，说明这是新条目，尝试插入
                instructionEntryDAO.insertInstructionEntry(entry);
                System.out.println("➕ 插入新食材条目：" + entry.getIngredient().getName());
            } else {
                System.out.println("🔄 更新食材条目：" + entry.getIngredient().getName() + " (Recipe ID: " + entry.getRecipe().getId() + ", Ingredient ID: " + entry.getIngredient().getId() + ")");
            }
        }*/

/*
测试后旧的食材数据会全删掉，上面注释掉的是会全删掉
* 为了试验改serving功能以下代码只能成功新增食材，不对旧食材进行操作
* */
    private void updateIngredientsAndInstructions() throws SQLException {
        InstructionEntryDAO instructionEntryDAO = new InstructionEntryDAOImpl();
        IngredientDAO ingredientDAO = new IngredientDAOImpl();
        InstructionDAO instructionDAO = new InstructionDAOImpl();

        // === 1. 更新食材（InstructionEntry） ===
        List<InstructionEntry> existingEntries = instructionEntryDAO.getInstructionEntriesByRecipeId(recipe.getId());
        Map<String, InstructionEntry> existingMap = new HashMap<>();
        for (InstructionEntry entry : existingEntries) {
            String key = entry.getIngredient().getId() + "_" + entry.getUnit().trim().toLowerCase();
            existingMap.put(key, entry);
        }

        for (IngredientEditRow row : ingredientEditRows) {
            String quantityStr = row.quantityField.getText();
            String unit = row.unitField.getText().trim().toLowerCase();
            String ingredientName = row.ingredientNameField.getText().trim();

            if (quantityStr.isEmpty() || unit.isEmpty() || ingredientName.isEmpty()) {
                System.err.println("跳过无效的食材行：数量、单位或名称为空。");
                continue;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                System.err.println("无效的数量：" + quantityStr);
                continue;
            }

            // 获取或插入 Ingredient
            Ingredient ingredient = ingredientDAO.getIngredientByName(ingredientName);
            if (ingredient == null) {
                ingredient = new Ingredient(ingredientName);
                if (!ingredientDAO.insertIngredient(ingredient)) {
                    System.err.println("无法插入新食材：" + ingredientName);
                    continue;
                }
                ingredient = ingredientDAO.getIngredientByName(ingredientName);
                if (ingredient == null) {
                    System.err.println("插入后无法获取ID：" + ingredientName);
                    continue;
                }
            }

            String key = ingredient.getId() + "_" + unit;

            InstructionEntry entry = new InstructionEntry();
            entry.setRecipe(recipe);
            entry.setIngredient(ingredient);
            entry.setUnit(unit);
            entry.setQuantity(quantity);

            if (existingMap.containsKey(key)) {
                // 更新
                instructionEntryDAO.updateInstructionEntry(entry);
                System.out.println("🔄 更新食材：" + ingredient.getName());
            } else {
                // 插入
                instructionEntryDAO.insertInstructionEntry(entry);
                System.out.println("➕ 新增食材：" + ingredient.getName());
            }
        }




    // 2. 处理步骤 (Instruction)
        InstructionDAO InstructionDAO = new InstructionDAOImpl();
        List<Instruction> oldInstructions = instructionDAO.getInstructionsByRecipeId(recipe.getId());
        List<Instruction> newOrUpdatedInstructions = new ArrayList<>();

        // 遍历 instructionEditRows
        int currentStepNumber = 1;
        for (InstructionEditRow row : instructionEditRows) {
            String description = row.descriptionArea.getText();
            if (description.isEmpty()) {
                System.err.println("跳过无效的步骤行：描述为空。");
                continue;
            }

            if (row.originalInstruction != null) {
                // 现有步骤的更新
                row.originalInstruction.setDescription(description);
                row.originalInstruction.setStepNumber(currentStepNumber); // 更新步骤编号
                newOrUpdatedInstructions.add(row.originalInstruction);
            } else {
                // 新增步骤
                Instruction newInstruction = new Instruction();
                newInstruction.setRecipe(recipe);
                newInstruction.setStepNumber(currentStepNumber);
                newInstruction.setDescription(description);
                newOrUpdatedInstructions.add(newInstruction);
            }
            currentStepNumber++;
        }

        // 删除在旧数据中存在但在新数据中不存在的步骤
        if (oldInstructions != null) {
            for (Instruction oldInst : oldInstructions) {
                boolean foundInNew = false;
                for (Instruction newInst : newOrUpdatedInstructions) {
                    // 使用复合主键 (recipe_id 和 stepNumber) 来判断是否是同一个步骤
                    if (oldInst.getRecipe().getId() == newInst.getRecipe().getId() && // 修正：从 .equals() 改为 ==
                        oldInst.getStepNumber() == newInst.getStepNumber()) { // 已经使用 ==
                        foundInNew = true;
                        break;
                    }
                }
                if (!foundInNew) {
                    // 旧步骤不在新列表中，说明它被删除了
                    // 调用新的 deleteInstruction 方法，传入 recipeId 和 stepNumber
                    instructionDAO.deleteInstruction(oldInst.getRecipe().getId(), oldInst.getStepNumber());
                    System.out.println("🗑️ 删除步骤：Recipe ID: " + oldInst.getRecipe().getId() + ", Step Number: " + oldInst.getStepNumber());
                }
            }
        }

        // 插入或更新所有新数据和修改过的数据
        for (Instruction instruction : newOrUpdatedInstructions) {
            // 对于 Instruction，我们没有独立的 ID 字段来判断是新增还是更新
            // 简单地尝试更新，如果更新失败（Affected Rows = 0），则说明记录不存在，执行插入
            boolean updated = instructionDAO.updateInstruction(instruction);
            if (!updated) {
                // 如果更新失败，说明这是新条目，尝试插入
                instructionDAO.insertInstruction(instruction);
                System.out.println("➕ 插入新步骤：" + instruction.getDescription());
            } else {
                System.out.println("🔄 更新步骤：" + instruction.getDescription() + " (Recipe ID: " + instruction.getRecipe().getId() + ", Step Number: " + instruction.getStepNumber() + ")");
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
            showErrorDialog("错误", "没有选中的食谱可以删除。");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("确认删除");
        confirmAlert.setHeaderText("您确定要删除此食谱吗？");
        confirmAlert.setContentText("删除后无法恢复：" + recipe.getName());

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    boolean deleted = recipeDAO.deleteRecipe(recipe.getId());
                    if (deleted) {
                        showInfoDialog("删除成功", "食谱 '" + recipe.getName() + "' 已成功删除。");
                        previewRecipeName.getScene().getWindow().hide(); // 关闭当前窗口
                    } else {
                        showErrorDialog("删除失败", "无法删除食谱 '" + recipe.getName() + "'。");
                    }
                } catch (SQLException e) {
                    showErrorDialog("数据库错误", "删除食谱时发生错误：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleScaleServingButton() {
        // 切换缩放输入框的可见性
        scaleInputBox.setVisible(!scaleInputBox.isVisible());
        // 每次点击缩放按钮，重置输入框内容
        scaleMultiplierField.setText("");
        // 如果隐藏了，确保显示回原始数据
        if (!scaleInputBox.isVisible()) {
            displayIngredientsWithScale(1);
        }
    }

    @FXML
    private void handleConfirmScale() {
        String multiplierText = scaleMultiplierField.getText();
        if (multiplierText == null || multiplierText.trim().isEmpty()) {
            showErrorDialog("输入错误", "请输入一个有效的缩放倍数。");
            return;
        }

        try {
            int scaleFactor = Integer.parseInt(multiplierText.trim());
            if (scaleFactor <= 0) {
                showErrorDialog("输入错误", "缩放倍数必须是正整数。");
                return;
            }
            displayIngredientsWithScale(scaleFactor);
            scaleInputBox.setVisible(false); // 确认后隐藏输入框
        } catch (NumberFormatException e) {
            showErrorDialog("输入错误", "缩放倍数必须是有效的整数。");
        }
    }

    /**
     * 显示信息对话框的通用方法。
     */
    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 显示错误对话框的通用方法。
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
