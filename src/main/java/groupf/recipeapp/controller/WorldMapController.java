package groupf.recipeapp.controller;

import groupf.recipeapp.App; // 假设App类用于设置和切换场景

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker; // 导入 Worker 类

import java.net.URL;
import java.util.ResourceBundle;

public class WorldMapController implements Initializable {

    @FXML
    private WebView worldMapWebView;

    @FXML
    private Label selectedRegionPromptLabel;

    private WebEngine webEngine;
    private String currentSelectedRegion = null; 

    // 调试点：构造函数，检查控制器何时被实例化
    public WorldMapController() {
        System.out.println("DEBUG: WorldMapController: 构造函数被调用。实例哈希: " + this.hashCode());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("DEBUG: WorldMapController: initialize 方法被调用。实例哈希: " + this.hashCode() + ". 初始 currentSelectedRegion: " + currentSelectedRegion);
        webEngine = worldMapWebView.getEngine();

        // 启用JavaScript调试，有助于在开发过程中查看WebView内部的错误
        webEngine.setJavaScriptEnabled(true);

        // 加载HTML文件
        URL htmlFileUrl = getClass().getResource("/groupf/recipeapp/html/worldMap.html");
        if (htmlFileUrl != null) {
            webEngine.load(htmlFileUrl.toExternalForm());
            System.out.println("DEBUG: WorldMapController: HTML 文件从加载: " + htmlFileUrl.toExternalForm());
        } else {
            System.err.println("错误：无法找到 worldMap.html 文件。请检查路径。");
        }

        // --- 核心修复：使用 setPromptHandler 监听 JavaScript 的 prompt 调用 ---
        webEngine.setPromptHandler(promptData -> {
            String message = promptData.getMessage();
            System.out.println("DEBUG: WorldMapController: 从 JavaScript 接收到 prompt 消息: '" + message + "'. 实例哈希: " + this.hashCode());

            // 假设我们约定 prompt 消息格式为 "regionCode:XYZ"
            if (message != null && message.startsWith("regionCode:")) {
                String regionCode = message.substring("regionCode:".length());
                System.out.println("DEBUG: WorldMapController: 从 prompt 消息解析到地区代码: '" + regionCode + "'. 实例哈希: " + this.hashCode());

                javafx.application.Platform.runLater(() -> {
                    selectedRegionPromptLabel.setText("您选择了: " + regionCode + " 地区。");
                    currentSelectedRegion = regionCode; // 存储当前选定的地区
                    System.out.println("DEBUG: WorldMapController: UI 更新。currentSelectedRegion 已设置为 '" + currentSelectedRegion + "'. 实例哈希: " + this.hashCode());
                });
                return ""; // 返回空字符串，表示处理了 prompt
            }
            return null; // 返回 null 表示未处理该 prompt，让 WebView 采用默认行为
        });
    }

    /**
     * JavaScriptReceiver 是一个内部类，用于从JavaScript接收回调。
     * 它的方法可以在JavaScript中通过 'app.methodName()' 调用。
     */
    /** 
    public class JavaScriptReceiver {
        // 这个方法将在 JavaScript 中通过 `app.sendRegionCode(...)` 调用
        public void sendRegionCode(String regionCode) {
            System.out.println("DEBUG: WorldMapController JavaScriptReceiver: 从JavaScript接收到地区代码: " + regionCode);
            // 在这里，你可以根据地区代码加载并显示食谱数据
            // 确保在JavaFX应用程序线程上更新UI
            javafx.application.Platform.runLater(() -> {
                selectedRegionPromptLabel.setText("您选择了: " + regionCode + " 地区。"); // 更新Label以显示地区代码
                currentSelectedRegion = regionCode; // 存储当前选定的地区
                System.out.println("DEBUG: WorldMapController JavaScriptReceiver: UI 更新。currentSelectedRegion 已设置为 '" + currentSelectedRegion + "'. 实例哈希: " + WorldMapController.this.hashCode());
            });
        }
    }
    */

    /**
     * 处理“查看该地区详情”按钮的点击事件。
     * 将选定的地区代码传递给 MainView。
     */
    @FXML
    private void handleViewRegionDetails() {
        System.out.println("DEBUG: WorldMapController: handleViewRegionDetails 方法被调用。实例哈希: " + this.hashCode() + ". 点击时 currentSelectedRegion: '" + currentSelectedRegion + "'");
        if (currentSelectedRegion != null) {
            try {
                System.out.println("DEBUG: WorldMapController: 尝试将 MainView 设置为根视图，并传递地区: " + currentSelectedRegion);
                App.setRoot("MainView", currentSelectedRegion);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("WorldMapController: 无法加载MainView.fxml 并传递地区代码。");
            }
        } else {
            System.out.println("DEBUG: WorldMapController: 请先选择一个地区。(currentSelectedRegion 为 null)");
            selectedRegionPromptLabel.setText("请点击地图区域选择一个地区！"); // 这行代码会重置标签文本
        }
    }

    /**
     * 处理返回主菜单按钮的点击事件。
     */
    @FXML
    private void handleBackToMain() {
        System.out.println("DEBUG: WorldMapController: handleBackToMain 方法被调用。实例哈希: " + this.hashCode());
        try {
            App.setRoot("MainView"); // 假设App类有一个setRoot方法用于切换FXML视图
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("WorldMapController: 无法加载MainView.fxml。");
        }
    }
}