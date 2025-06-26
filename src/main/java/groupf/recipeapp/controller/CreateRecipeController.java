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
import javafx.stage.FileChooser; // 导入 FileChooser
import java.io.File; // 导入 File
import java.io.IOException; // 导入 IOException
import java.nio.file.Files; // 导入 Files
import java.nio.file.Path; // 导入 Path
import java.nio.file.StandardCopyOption; // 导入 StandardCopyOption
import java.util.List; // 导入 List
import javafx.collections.FXCollections; // 导入 FXCollections
import javafx.collections.ObservableList; // 导入 ObservableList


import groupf.recipeapp.entity.Recipe;
import groupf.recipeapp.entity.Ingredient;
import groupf.recipeapp.entity.Instruction;
import groupf.recipeapp.entity.InstructionEntry;
import groupf.recipeapp.entity.Region;
import groupf.recipeapp.dao.RecipeDAO;
import groupf.recipeapp.dao.RecipeDAOImpl;
import groupf.recipeapp.dao.RegionDAO; // 导入 RegionDAO
import groupf.recipeapp.dao.RegionDAOImpl; // 导入 RegionDAOImpl
import groupf.recipeapp.App; // 导入 App 类用于页面跳转


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
    private ComboBox<Region> regionComboBox; // FXML 注解的地区选择下拉框

    @FXML
    private Label imagePathLabel; // FXML 注解的图片路径标签

    private String selectedImagePath; // 存储选定图片文件的相对路径

    private RegionDAO regionDAO; // 地区 DAO 实例

    @FXML
    public void initialize() {
        regionDAO = new RegionDAOImpl(); // 实例化 RegionDAO
        loadRegions();
    }

    private void loadRegions() {
        try {
            List<Region> regions = regionDAO.getAllRegions();
            ObservableList<Region> regionObservableList = FXCollections.observableArrayList(regions);
            regionComboBox.setItems(regionObservableList);
            // 可以设置一个默认值，或者让用户选择
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
        fileChooser.setTitle("选择图片文件");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                // 定义图片存储的相对路径 (src/main/resources/images/)
                // 你需要在 resources 目录下手动创建 images 文件夹
                String resourceDir = "src/main/resources/groupf/recipeapp/images/";
                File destDir = new File(resourceDir);
                if (!destDir.exists()) {
                    destDir.mkdirs(); // 如果目录不存在则创建
                }

                // 生成唯一的文件名，防止重复
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path destinationPath = new File(destDir, fileName).toPath();

                // 复制文件
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // 存储相对路径，这是我们将保存到数据库的路径
                // 从 "src/main/resources/" 之后开始算作资源路径
                this.selectedImagePath = "/groupf/recipeapp/images/" + fileName;
                imagePathLabel.setText(selectedFile.getName()); // 显示文件名在UI上

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("图片上传成功");
                alert.setHeaderText(null);
                alert.setContentText("图片已成功上传到本地资源。");
                alert.showAndWait();

            } catch (IOException e) {
                showError("无法复制图片文件: " + e.getMessage());
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
        Region selectedRegion = regionComboBox.getSelectionModel().getSelectedItem(); // 获取选定的地区

        if (name.isEmpty() || servingsStr.isEmpty() || selectedRegion == null) { // 检查地区是否选择
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
        recipe.setRegion(selectedRegion); // 设置地区

        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            try {
                // 1. 获取当前图片的实际文件路径 (因为selectedImagePath是资源路径)
                // 假设图片存储在项目 workspace 的 src/main/resources/ 目录下
                String projectRoot = System.getProperty("user.dir"); // 获取项目根目录
                // 构建完整的旧文件路径
                // selectedImagePath 类似 "/groupf/recipeapp/images/时间戳_文件名.png"
                String currentFilePathOnDisk = projectRoot + File.separator + "src" + File.separator + "main" +
                                               File.separator + "resources" + selectedImagePath.replace("/", File.separator);
                File currentImageFile = new File(currentFilePathOnDisk);

                if (currentImageFile.exists()) {
                    // 2. 提取原始文件的扩展名
                    String currentFileName = currentImageFile.getName();
                    String fileExtension = "";
                    int dotIndex = currentFileName.lastIndexOf('.');
                    if (dotIndex > 0 && dotIndex < currentFileName.length() - 1) {
                        fileExtension = currentFileName.substring(dotIndex); // 包含点，例如 ".png"
                    }

                    // 3. 构造新的文件名：食谱名称_image.扩展名
                    // 清理食谱名称，使其适合作为文件名（替换特殊字符为空格或下划线）
                    String sanitizedRecipeName = name.replaceAll("[^a-zA-Z0-9\\s]", "").trim().replaceAll("\\s+", "_");
                    String newFileName = sanitizedRecipeName + "_image" + fileExtension;
                    
                    // 构造新的目标文件路径
                    Path newDestinationPath = new File(currentImageFile.getParentFile(), newFileName).toPath();

                    // 4. 重命名（移动）文件
                    Files.move(currentImageFile.toPath(), newDestinationPath, StandardCopyOption.REPLACE_EXISTING);

                    // 5. 更新 selectedImagePath 为新的相对路径
                    this.selectedImagePath = "/groupf/recipeapp/images/" + newFileName;
                    System.out.println("图片已重命名为: " + this.selectedImagePath); // 调试信息
                } else {
                    System.err.println("旧图片文件未找到: " + currentFilePathOnDisk);
                    // 警告用户图片未找到，但仍继续提交食谱（不阻止提交）
                }

            } catch (IOException e) {
                showError("无法重命名图片文件: " + e.getMessage());
                e.printStackTrace();
                return; // 如果重命名失败，则停止食谱提交
            }
        }
        recipe.setImagePath(selectedImagePath); // 设置图片路径
        

        // 收集 ingredientsBox 中的输入项
        for (Node node : ingredientsBox.getChildren()) {
            if (node instanceof HBox hbox) { // 检查是否为 HBox
                // 确保 hbox 至少有3个TextFields (quantity, unit, name)
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

        // 收集 instructionsBox 中的输入项
        int stepNumber = 1;
        for (Node node : instructionsBox.getChildren()) {
            // 确保是 HBox 并且包含 TextField
            if (node instanceof HBox hbox && hbox.getChildren().size() >= 1 && hbox.getChildren().get(0) instanceof TextField) {
                TextField field = (TextField) hbox.getChildren().get(0);
                String stepDescription = field.getText().trim();
                if (!stepDescription.isEmpty()) { // 避免添加空步骤
                    // 这里需要传递 recipe 对象
                    recipe.addInstruction(new Instruction(stepNumber++, stepDescription, recipe));
                }
            }
        }


        // 插入数据库
        RecipeDAO recipeDAO = new RecipeDAOImpl();
        boolean success = recipeDAO.insertRecipe(recipe);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Recipe Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Recipe \"" + name + "\" submitted successfully!");
            alert.showAndWait();
            // 提交成功后清空表单
            recipeNameField.clear();
            servingsField.clear();
            descriptionArea.clear();
            ingredientsBox.getChildren().clear();
            instructionsBox.getChildren().clear();
            regionComboBox.getSelectionModel().clearSelection();
            imagePathLabel.setText("No image selected");
            selectedImagePath = null; // 清空已选图片路径

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
            App.setRoot("MainView"); // 使用 App 类的 setRoot 方法跳转到 MainView
        } catch (IOException e) {
            e.printStackTrace();
            // 可选: 弹出错误提示
        }
    }

    /**
     * 处理"世界食谱"按钮的点击事件，跳转到世界地图页面。
     */
    @FXML
    private void handleWorldCuisines() {
        System.out.println("左侧导航按钮：World Cuisines 被点击，尝试跳转到世界地图页面。");
        try {
            App.setRoot("WorldMapView"); // 调用App类的setRoot方法跳转到WorldMapView
        } catch (IOException e) {
            e.printStackTrace();
            showError("无法加载 WorldMapView.fxml。请检查文件是否存在且路径正确。");
        }
    }

}