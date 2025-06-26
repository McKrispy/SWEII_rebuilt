# GroupF Digital Cookbook

本项目是一个基于 JavaFX 与 Maven 构建的桌面应用，用于管理和浏览食谱数据。应用通过 MySQL 数据库持久化存储信息，并允许用户创建、编辑及查询食谱。

`App` 类在启动时会将主窗口标题设置为 *GroupF Digital Cookbook*。

## 目录

  - [环境要求](https://www.google.com/search?q=%23%E7%8E%AF%E5%A2%83%E8%A6%81%E6%B1%82)
  - [快速开始](https://www.google.com/search?q=%23%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B)
  - [运行方式](https://www.google.com/search?q=%23%E8%BF%90%E8%A1%8C%E6%96%B9%E5%BC%8F)
  - [核心功能](https://www.google.com/search?q=%23%E6%A0%B8%E5%BF%83%E5%8A%9F%E8%83%BD)
  - [项目结构](https://www.google.com/search?q=%23%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84)
  - [技术栈](https://www.google.com/search?q=%23%E6%8A%80%E6%9C%AF%E6%A0%88)

-----

## 环境要求

确保已安装以下软件：

  - Git
  - JDK 17 或更高版本
  - Apache Maven 3.8+
  - MySQL 8.0+
  - 一款支持 Maven 的 IDE（推荐 IntelliJ IDEA）

-----

## 快速开始

1.  **克隆仓库**

    ```bash
    git clone https://github.com/McKrispy/SWEII_rebuilt.git
    cd SWEII_rebuilt
    ```

2.  **创建数据库**

    ```sql
    CREATE DATABASE refactored_se2;
    ```

    将 `database` 目录中的 `Dump20250616.sql` 导入：

    ```bash
    mysql -u your_username -p refactored_se2 < database/Dump20250616.sql
    ```

3.  **配置数据库连接**
    在 `src/main/resources/db_configuration/` 下创建 `db.properties`：

    ```properties
    db.url=jdbc:mysql://localhost:3306/refactored_se2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    db.user=root
    db.password=YOUR_SECRET_PASSWORD_HERE
    ```

4.  **构建项目**
    在 IDE 中导入项目根目录，Maven 会自动下载依赖；若未自动完成，可手动刷新 Maven 项目。

-----

## 运行方式

### 在 IDE 中运行

进入 `src/main/java/groupf/recipeapp/App.java`，运行 `main` 方法即可启动应用。

### 命令行运行

```bash
mvn javafx:run
```

Maven 将编译并启动 JavaFX 应用。

-----

## 核心功能

  - **食谱搜索与浏览**：主界面提供搜索框和列表，可查看食谱概要及图片。
  - **食谱创建**：通过 `CreateRecipeView` 录入名称、份量、描述、地区与图片，并动态添加食材与步骤。
  - **世界地图筛选**：使用 `WorldMapView` 选择地区后，可按地域过滤食谱。
  - **完整食谱查看与编辑**：在 `FullRecipeView` 中查看详情、修改内容并调整份量。
  - **数据持久化**：所有操作通过 DAO 层与 MySQL 数据库交互。

-----

## 项目结构

```bash
.
├── database/              # SQL 转储文件
├── src/
│   ├── main/
│   │   ├── java/              # Java 源代码
│   │   └── resources/
│   │       ├── db_configuration/  # 数据库配置文件 (需自行创建)
│   │       └── groupf/recipeapp/  # FXML、HTML 与图片资源
│   └── test/                # 测试目录（当前为空）
├── pom.xml                # Maven 配置
└── README.md
```

-----

## 技术栈

  - Jdk 24
  - JavaFX 20
  - Maven
  - MySQL 8.0
