# GroupF Digital Cookbook

本项目是一个基于 JavaFX 与 Maven 构建的桌面应用，用于管理和浏览食谱数据。应用通过 MySQL 数据库持久化存储信息，并允许用户创建、编辑及查询食谱。

`App` 类在启动时会将主窗口标题设置为 *GroupF Digital Cookbook*。

## 目录
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [运行方式](#运行方式)
- [核心功能](#核心功能)
- [项目结构](#项目结构)
- [技术栈](#技术栈)

---

## 环境要求

确保已安装以下软件：
- Git
- JDK 17 或更高版本
- Apache Maven 3.8+
- MySQL 8.0+
- 一款支持 Maven 的 IDE（推荐 IntelliJ IDEA）

---

## 快速开始

1. **克隆仓库**
   ```bash
   git clone https://github.com/McKrispy/SWEII_rebuilt.git
   cd SWEII_rebuilt 
   
2. **创建数据库**
   ```sql
   CREATE DATABASE refactored_se2;
在bash中将 database 目录中的 Dump20250616.sql 导入：
   mysql -u your_username -p refactored_se2 < database/Dump20250616.sql

3. **配置数据库连接**
在 src/main/resources/db_configuration/ 下创建 db.properties：
   ```properties
   db.url=jdbc:mysql://localhost:3306/refactored_se2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.user=root
   db.password=YOUR_SECRET_PASSWORD_HERE
4. **构建项目**
在 IDE 中导入项目根目录，Maven 会自动下载依赖；若未自动完成，可手动刷新 Maven 项目。

## 运行方式
-在 IDE 中运行
进入 src/main/java/groupf/recipeapp/App.java，运行 main 方法即可启动应用。

-命令行运行
mvn javafx:run
Maven 将编译并启动 JavaFX 应用。

## 核心功能
食谱搜索与浏览：主界面提供搜索框和列表，可查看食谱概要及图片。

食谱创建：通过 CreateRecipeView 录入名称、份量、描述、地区与图片，并动态添加食材与步骤。

世界地图筛选：使用 WorldMapView 选择地区后，可按地域过滤食谱。

完整食谱查看与编辑：在 FullRecipeView 中查看详情、修改内容并调整份量。

数据持久化：所有操作通过 DAO 层与 MySQL 数据库交互。

##项目结构
.
├── database/              # SQL 转储文件
├── src/
│   ├── main/
│   │   ├── java/          # Java 源代码
│   │   └── resources/
│   │       ├── db_configuration/      # 数据库配置文件 (需自行创建)
│   │       └── groupf/recipeapp/      # FXML、HTML 与图片资源
│   └── test/              # 测试目录（当前为空）
├── pom.xml                # Maven 配置
└── README.md
技术栈
Jdk 24

JavaFX 20

Maven

MySQL 8.0
