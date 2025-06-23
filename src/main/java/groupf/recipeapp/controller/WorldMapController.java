package groupf.recipeapp.controller;

import groupf.recipeapp.App; // 假设App类用于设置和切换场景

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

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


        // 等待页面加载完成
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                // 当HTML页面加载成功时，将Java对象注入到JavaScript环境中
                // 'app' 是HTML中JavaScript将用来调用Java方法的对象名
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("app", new JavaScriptReceiver());
                System.out.println("JavaFX对象已注入到JavaScript环境中。");
            }
        });
    }

    /**
     * JavaScriptReceiver 是一个内部类，用于从JavaScript接收回调。
     * 它的方法可以在JavaScript中通过 'app.methodName()' 调用。
     */
    public class JavaScriptReceiver {
        public void getRecipeByCountryCode(String countryCode) {
            System.out.println("从JavaScript接收到国家代码: " + countryCode);
            // 在这里，你可以根据国家代码加载并显示食谱数据
            // 例如，可以查询数据库或调用API
            // 为了演示，我们只更新一个Label
            javafx.application.Platform.runLater(() -> {
                countryCodeLabel.setText(countryCode);
                // 实际应用中，你可能会在这里导航到另一个视图或加载食谱列表
                // 例如：loadRecipesForCountry(countryCode);
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