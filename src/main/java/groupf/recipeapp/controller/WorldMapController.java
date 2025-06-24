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
    private Label selectedRegionPromptLabel;

    private WebEngine webEngine;
    private String currentSelectedRegion = null; 


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

                // 更新UI显示
                javafx.application.Platform.runLater(() -> {
                    selectedRegionPromptLabel.setText("您选择了 " + regionCode + " 地区。");
                    currentSelectedRegion = regionCode; // 存储当前选定的地区
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
        public void getRecipeByRegionCode(String regionCode) {
            System.out.println("从JavaScript接收到地区代码: " + regionCode);
            // 这里可以根据地区代码加载并显示食谱数据
            javafx.application.Platform.runLater(() -> {
                selectedRegionPromptLabel.setText("接收到的区域代码 (JS): " + regionCode);
                currentSelectedRegion = regionCode;
            });
        }
    }

    /**
     * 处理“查看该地区详情”按钮的点击事件。
     * 将选定的地区代码传递给 MainView。
     */
    @FXML
    private void handleViewRegionDetails() {
        if (currentSelectedRegion != null) {
            try {
                // 假设 App 类有一个方法可以传递参数
                App.setRoot("MainView", currentSelectedRegion);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("无法加载MainView.fxml 并传递地区代码。");
            }
        } else {
            System.out.println("请先选择一个地区。");
            selectedRegionPromptLabel.setText("请点击地图区域选择一个地区！");
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