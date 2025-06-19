package groupf.recipeapp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/groupf/recipeapp/fxml/Mainview.fxml"));
        primaryStage.setTitle("GroupF Digital Cookbook");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // 加上main方法
    public static void main(String[] args) {
        launch(args);
    }
}