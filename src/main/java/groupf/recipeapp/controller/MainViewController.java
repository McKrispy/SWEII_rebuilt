package groupf.recipeapp.controller;

import groupf.recipeapp.dao.RecipeDAO; // 新增导入
import groupf.recipeapp.dao.RecipeDAOImpl; // 新增导入
import groupf.recipeapp.entity.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List; // 修改为 java.util.List
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import groupf.recipeapp.App;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent; // 新增：用于handleRecipeListClick方法
import groupf.recipeapp.entity.Ingredient; // 新增：用于Recipe中的食材
import groupf.recipeapp.entity.Instruction; // 新增：用于Recipe中的步骤
import groupf.recipeapp.entity.Region; // 新增：用于Recipe中的区域
import javafx.scene.image.ImageView; // 新增导入
import javafx.scene.image.Image; // 新增导入


/**
 * 主视图的控制器。
 * 负责处理用户交互，如搜索和列表选择，并协调与后端服务的通信。
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
    private ScrollPane previewScrollPane; // 可以引用，但可能不直接用于内容操作
    @FXML
    private VBox recipeDetailsVBox;// 预览内容的VBox，用于清空或动态添加内容
    @FXML
    private ImageView previewImageView;

    // --- 后端服务接口 (现在使用真实的实现) ---
    private RecipeDAO recipeDAO; // 声明为接口类型

    
    
    @FXML
    private void handleRecommendedDishes(ActionEvent event) {
        System.out.println("左侧导航按钮：World Cuisines 被点击，尝试跳转到世界地图页面。");
        try {
            App.setRoot("WorldMapView"); // 调用App类的setRoot方法跳转到WorldMapView
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("页面跳转失败", "无法加载 WorldMapView.fxml。请检查文件是否存在且路径正确。");
        }
    }

    /**
     * 处理recipeListView的鼠标点击事件，区分单机和双击。
     * 单击：显示食谱预览。
     * 双击：跳转到食谱详情页面。
     */
    @FXML
    private void handleRecipeListClick(javafx.scene.input.MouseEvent event) {
        // 获取当前选中的食谱
        Recipe selectedRecipe = recipeListView.getSelectionModel().getSelectedItem();

        if (selectedRecipe != null) {
            if (event.getClickCount() == 1) { // 单击事件
                System.out.println("单击食谱：" + selectedRecipe.getName());
                displayRecipePreview(selectedRecipe);
            } else if (event.getClickCount() == 2) { // 双击事件
                System.out.println("双击食谱，准备跳转到详情页：" + selectedRecipe.getName());
                navigateToRecipeDetail(selectedRecipe, event); // 传入event以获取当前Stage
            }
        }
    }

    /**
     * 根据传入的Recipe对象更新右侧的预览区域。
     */
    private void displayRecipePreview(Recipe recipe) {
        if (recipe != null) {
            previewRecipeName.setText(recipe.getName() != null ? recipe.getName() : "N/A");
            previewDescription.setText(recipe.getDescription() != null ? recipe.getDescription() : "无描述。");
    
            // 设置图片
            if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
                try {
                    // 假设图片路径是相对于资源目录的
                    // 确保图片文件存在于 src/main/resources/groupf/recipeapp/images/ 或其他可访问路径
                    Image image = new Image(getClass().getResourceAsStream(recipe.getImagePath()));
                    previewImageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("无法加载图片: " + recipe.getImagePath() + " - " + e.getMessage());
                    previewImageView.setImage(null); // 加载失败时清空图片
                }
            } else {
                previewImageView.setImage(null); // 没有图片路径时清空图片
            }
    
            // 移除了其他多余的 Label 赋值
        } else {
            // 清空预览区域
            previewRecipeName.setText("Recipe Name");
            previewDescription.setText("Description goes here.");
            previewImageView.setImage(null); // 清空图片
            // 移除了其他多余的 Label 清空
        }
    }

    /**
     * 跳转到食谱详情页面的方法 (待实现)
     * @param recipe 选中的食谱对象
     * @param event 鼠标事件，用于获取当前舞台
     */
    private void navigateToRecipeDetail(Recipe recipe, javafx.scene.input.MouseEvent event) {
        try {
            // 假设您有一个 RecipeDetailView.fxml 和 RecipeDetailController
            // 您需要根据实际的详情页面FXML路径进行修改
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/groupf/recipeapp/fxml/RecipeDetailView.fxml"));
            Parent detailRoot = loader.load();

            // 如果您的详情页面需要接收Recipe对象，可以在这里设置控制器
            // RecipeDetailController controller = loader.getController();
            // controller.setRecipe(recipe); // 假设有一个setRecipe方法

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene detailScene = new Scene(detailRoot);
            currentStage.setScene(detailScene);
            currentStage.setTitle("Recipe Details: " + recipe.getName());
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("无法打开食谱详情页面", e.getMessage());
        }
    }
    
    
    /**
     * 当 FXML 文件加载完成，所有 @FXML 成员被注入后，此方法会自动调用。
     * 用于初始化视图，设置监听器和加载初始数据。
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("MainViewController is initializing...");

        // 实例化真正的DAO实现
        this.recipeDAO = new RecipeDAOImpl();

        // 1. 配置ListView如何显示Recipe对象
        setupListViewCellFactory();

        // 2. 配置ListView的项目选择事件
        // Deprecated: 
        // setupListViewSelectionListener();

        // 3. 加载初始的食谱列表
        loadAllRecipes();
    }

    /**
     * 处理“搜索”按钮的点击事件。
     * 从文本框获取输入，并调用相应的数据加载方法。
     */
    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchText = searchTextField.getText().trim();

        if (searchText.isEmpty()) {
            // 如果搜索框为空，重新加载所有食谱
            loadAllRecipes();
        } else {
            // 否则，根据关键字进行搜索
            searchRecipes(searchText);
        }
    }

    // --- 数据加载与显示方法 ---

    /**
     * 加载并显示所有食谱。
     */
    private void loadAllRecipes() {
        System.out.println("Loading all recipes...");
        List<Recipe> recipes = recipeDAO.getAllRecipes(); // 使用DAO获取数据

        recipeListView.getItems().clear();
        recipeListView.getItems().addAll(recipes);
    }

    /**
     * 根据名称搜索食谱并显示结果。
     */
    private void searchRecipes(String name) {
        System.out.println("Searching for recipes named: " + name);
        List<Recipe> recipes = recipeDAO.searchRecipesByName(name); // 使用DAO获取数据

        recipeListView.getItems().clear();
        recipeListView.getItems().addAll(recipes);
    }


    // --- UI 配置辅助方法 ---

    /**
     * 设置 ListView 的 CellFactory。
     * 这决定了每个 Recipe 对象在列表中的显示方式（只显示名称）。
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

    /**
     * 为 ListView 添加选择监听器。
     * 当用户点击列表中的项目时触发。
     
    private void setupListViewSelectionListener() {
        recipeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Recipe selectedRecipe = newValue;
                System.out.println("User selected recipe: " + selectedRecipe.getName());

                // TODO: 在这里实现导航到食谱详情页的逻辑
                // navigateToRecipeDetail(selectedRecipe);
            }
        });
    }
        */
    @FXML
    private void handleSearchRecipes(ActionEvent event) {
        System.out.println("左侧导航按钮：Search Recipes 被点击");

        try {
            // 加载 CreateRecipeView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("groupf/recipeapp/fxml/MainView.fxml"));
            Parent createRecipeRoot = loader.load();
            System.out.println("FXML 加载成功");

            // 获取当前按钮所在的窗口
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 创建新场景并设置到当前舞台
            Scene createRecipeScene = new Scene(createRecipeRoot);
            currentStage.setScene(createRecipeScene);
            currentStage.setTitle("Create New Recipe");
            System.out.println("跳转成功");

        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("无法打开创建食谱页面", e.getMessage());
        }

        loadAllRecipes();
    }

    @FXML
    private void handleCreateNewRecipe(ActionEvent event) {
        System.out.println("左侧导航按钮：Create New Recipe 被点击");

        try {
            // 加载 CreateRecipeView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/groupf/recipeapp/fxml/CreateRecipeView.fxml"));
            Parent createRecipeRoot = loader.load();
            System.out.println("FXML 加载成功");

            // 获取当前按钮所在的窗口
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 创建新场景并设置到当前舞台
            Scene createRecipeScene = new Scene(createRecipeRoot);
            currentStage.setScene(createRecipeScene);
            currentStage.setTitle("Create New Recipe");
            System.out.println("跳转成功");

        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("无法打开创建食谱页面", e.getMessage());
        }

    }

    @FXML
    private void handleShowFullRecipe() {
        try {
            // 诊断代码开始
            System.out.println("=== 开始诊断 FXML 加载问题 ===");
            String resourcePath = "groupf/recipeapp/fxml/FullRecipeView.fxml";

// 方法1：使用 ClassLoader
            URL url1 = getClass().getClassLoader().getResource(resourcePath);
            System.out.println("ClassLoader 加载路径: " + url1);

// 方法2：使用 Class.getResource
            URL url2 = getClass().getResource("/" + resourcePath);
            System.out.println("Class.getResource 加载路径: " + url2);



            if (url1 == null && url2 == null) {
                System.err.println("❌ 所有加载方式都失败，请检查：");
                System.err.println("1. 确认执行过 mvn clean compile");
                System.err.println("2. 检查 target/classes/" + resourcePath + " 是否存在");
                System.err.println("3. 如果使用模块化，检查 module-info.java 配置");
                return;
            }
// 诊断代码结束
            // 加载 FullRecipeView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/groupf/recipeapp/fxml/FullRecipeView.fxml"));
            Parent root = loader.load();

            // 传递当前选中的菜谱数据（假设有 getSelectedRecipe 方法）
            //FullRecipeController controller = loader.getController();
           // controller.setRecipe(currentRecipe); // 你需要定义 currentRecipe 变量

            // 显示新窗口
            Stage stage = new Stage();
            stage.setTitle("Full Recipe");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("无法加载 FullRecipeView.fxml");
        }
    }



    /**
     * 显示错误对话框的通用方法（可选）
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}