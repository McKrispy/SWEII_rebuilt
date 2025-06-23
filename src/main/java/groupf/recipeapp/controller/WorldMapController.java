package groupf.recipeapp.controller;

import groupf.recipeapp.App; // 假设App类用于设置和切换场景

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class WorldMapController implements Initializable {

    @FXML
    private WebView worldMapWebView;

    @FXML
    private Label countryCodeLabel;

    private WebEngine webEngine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = worldMapWebView.getEngine();

        // 启用JavaScript调试，有助于在开发过程中查看WebView内部的错误
        webEngine.setJavaScriptEnabled(true);

        // 加载HTML文件
        // 使用getResource()方法确保无论JAR文件在哪里运行，都能正确找到HTML文件
        URL htmlFileUrl = getClass().getResource("/groupf/recipeapp/html/worldMap.html");
        if (htmlFileUrl != null) {
            webEngine.load(htmlFileUrl.toExternalForm());
        } else {
            System.err.println("错误：无法找到 worldMap.html 文件。请检查路径。");
        }


        // 监听WebEngine的URL变化
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            // 将 "countryCode" 改为 "regionCode"
            if (newValue != null && newValue.startsWith("app://regionCode/")) {
                // 解析地区代码
                String regionCode = newValue.substring("app://regionCode/".length());
                System.out.println("从URL接收到地区代码: " + regionCode);

                // 在这里，你可以根据地区代码加载并显示食谱数据
                // 例如，查询数据库或调用API
                // 确保在JavaFX应用程序线程上更新UI
                javafx.application.Platform.runLater(() -> {
                    // 更新Label以显示地区代码
                    countryCodeLabel.setText("最近点击的区域代码: " + regionCode); // 可以考虑把 countryCodeLabel 改名
                    // 实际应用中，你可能会在这里导航到另一个视图或加载食谱列表
                    // 例如：loadRecipesForRegion(regionCode);
                });

                // 为了防止WebView试图真正导航到这个“假”URL，
                // 可以重新加载原始HTML或设置一个空URL
                // 这里我们重新加载原始HTML，保持地图视图的稳定
                if (htmlFileUrl != null) {
                    webEngine.load(htmlFileUrl.toExternalForm());
                }
            }
        });
    }

    /**
     * JavaScriptReceiver 是一个内部类，用于从JavaScript接收回调。
     * 它的方法可以在JavaScript中通过 'app.methodName()' 调用。
     */
    public class JavaScriptReceiver {
        // 将 "getRecipeByCountryCode" 改为 "getRecipeByRegionCode"，并修改参数
        public void getRecipeByRegionCode(String regionCode) {
            System.out.println("从JavaScript接收到地区代码: " + regionCode);
            // 在这里，你可以根据地区代码加载并显示食谱数据
            // 例如，可以查询数据库或调用API
            // 为了演示，我们只更新一个Label
            javafx.application.Platform.runLater(() -> {
                countryCodeLabel.setText("接收到的区域代码 (JS): " + regionCode); // 可以考虑把 countryCodeLabel 改名
                // 实际应用中，你可能会在这里导航到另一个视图或加载食谱列表
                // 例如：loadRecipesForRegion(regionCode);
            });
        }
    }

    /**
     * 处理返回主菜单按钮的点击事件。
     */
    @FXML
    private void handleBackToMain() {
        try {
            App.setRoot("MainView"); // 假设App类有一个setRoot方法用于切换FXML视图
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("无法加载MainView.fxml。");
        }
    }
}