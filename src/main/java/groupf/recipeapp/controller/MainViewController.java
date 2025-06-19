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


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;

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

    // --- 后端服务接口 (现在使用真实的实现) ---
    private RecipeDAO recipeDAO; // 声明为接口类型

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
        setupListViewSelectionListener();

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
     */
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
    @FXML
    private void handleSearchRecipes(ActionEvent event) {
        System.out.println("左侧导航按钮：Search Recipes 被点击");

        try {
            // 加载 CreateRecipeView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
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


    @FXML
    private void handleRecommendedDishes(ActionEvent event) {
        System.out.println("左侧导航按钮：Recommended Dishes 被点击");
        // TODO: 实现推荐算法或跳转页面
    }

}