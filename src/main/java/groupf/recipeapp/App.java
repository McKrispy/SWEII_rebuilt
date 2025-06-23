package groupf.recipeapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.io.IOException; // 导入IOException

public class App extends Application {

    private static Scene scene; // 声明一个静态Scene变量
    private static Stage primaryStage; // 声明一个静态Stage变量，以便在setRoot中使用

    @Override
    public void start(Stage stage) throws Exception { // 将参数名从primaryStage改为stage
        App.primaryStage = stage; // 保存主舞台
        scene = new Scene(loadFXML("MainView")); // 初始化静态Scene，加载主视图
        stage.setTitle("GroupF Digital Cookbook");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 静态方法用于加载新的FXML文件并更新当前场景的根节点。
     * @param fxml 要加载的FXML文件的名称（不带.fxml扩展名）。
     * @throws IOException 如果FXML文件无法加载。
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * 辅助方法，用于加载指定的FXML文件并返回其根节点。
     * @param fxml 要加载的FXML文件的名称。
     * @return 加载的FXML文件的根Parent节点。
     * @throws IOException 如果FXML文件无法加载。
     */
    private static Parent loadFXML(String fxml) throws IOException {
        String fxmlPath = "/groupf/recipeapp/fxml/" + fxml + ".fxml";
        URL fxmlUrl = App.class.getResource(fxmlPath); // 尝试获取资源URL

        System.out.println("尝试加载 FXML 文件: " + fxmlPath); // 打印尝试加载的路径
        if (fxmlUrl == null) {
            // 如果getResource返回null，说明资源未找到
            System.err.println("错误：FXML 资源未找到。URL 为 null，路径: " + fxmlPath);
            // 打印 App 类是从哪里加载的，这有助于诊断类加载器问题
            try {
                System.err.println("App.class 加载自: " + App.class.getProtectionDomain().getCodeSource().getLocation());
            } catch (SecurityException e) {
                System.err.println("无法获取 App.class 的加载位置: " + e.getMessage());
            }
            // 抛出更具体的异常，包含找不到的路径
            throw new IOException("FXML 文件未找到: " + fxmlPath);
        } else {
            System.out.println("FXML 资源已找到。URL: " + fxmlUrl); // 打印找到的URL
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        return fxmlLoader.load();
    }
    /**
     * JavaFX应用程序的主入口点。
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        launch(args);
    }
}