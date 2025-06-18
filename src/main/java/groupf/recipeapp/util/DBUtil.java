package groupf.recipeapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接工具类。
 * 提供了获取和关闭数据库连接的方法。
 * 连接配置从 "resources/db_configuration/db.properties" 文件加载。
 */
public class DBUtil {

    // 1. 用于存储从 .properties 文件中加载的配置信息
    private static final Properties properties = new Properties();

    // 2. 静态初始化块：在类加载时自动执行，仅执行一次
    static {
        // 定义配置文件的路径 (相对于 resources 目录)
        String resourceName = "db_configuration/db.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        // 使用 try-with-resources 自动管理 InputStream
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            if (resourceStream == null) {
                System.err.println("配置文件未找到: " + resourceName);
                throw new IllegalStateException("Database configuration file not found: " + resourceName);
            }
            // 加载配置信息到 properties 对象
            properties.load(resourceStream);
        } catch (IOException e) {
            System.err.println("读取数据库配置文件时出错: " + resourceName);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库连接。
     * 连接信息从配置文件中动态获取。
     * @return 数据库连接对象。
     * @throws SQLException 如果数据库连接失败。
     */
    public static Connection getConnection() throws SQLException {
        // 3. 从 properties 对象中获取连接信息，而不是使用硬编码的常量
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }

    /**
     * 关闭数据库连接。
     * (此方法从你的原始代码中完整保留)
     * @param connection 要关闭的连接对象。
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}