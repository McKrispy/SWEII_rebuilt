module com.recipeapp { // 这里的名字要和你的 pom.xml 中的 groupId 类似
    // 需要的 JavaFX 模块
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // 需要的数据库模块
    requires java.sql;

    // 对 JavaFX FXML 开放你的主包和控制器包
    opens groupf.recipeapp to javafx.fxml;
    opens groupf.recipeapp.controller to javafx.fxml;

    // 开放实体类，如果它们在 TableView 中使用了 PropertyValueFactory
    opens groupf.recipeapp.entity to javafx.base;

    //打开数据库配置文件
    opens db_configuration;

    // 导出你的主包
    exports groupf.recipeapp;
}