package groupf.recipeapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException; // 导入IOException

public class App extends Application {

    private static Scene scene; // 声明一个静态Scene变量
    private static Stage primaryStage; // 声明一个静态Stage变量，以便在setRoot中使用

    @Override
    public void start(Stage stage) throws Exception { // 将参数名从primaryStage改为stage
        App.primaryStage = stage; // 保存主舞台
        scene = new Scene(loadFXML("redesigned_MainView")); // 初始化静态Scene，加载主视图
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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/groupf/recipeapp/fxml/" + fxml + ".fxml"));
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