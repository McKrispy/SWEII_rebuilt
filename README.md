# 食谱管理桌面应用 (Recipe Management Desktop App)

这是一个使用 JavaFX 和 Maven 构建的桌面应用程序，用于管理和浏览食谱。应用程序连接到 MySQL 数据库来存储和检索数据。

该项目遵循标准的 Maven 项目结构，便于团队协作和依赖管理。

## 目录
- [环境要求](#环境要求)
- [配置与设置步骤](#配置与设置步骤)
- [运行项目](#运行项目)
- [项目结构](#项目结构)
- [技术栈](#技术栈)

## 环境要求

在开始之前，请确保你的开发环境中已安装以下软件：

- **Git**: 用于克隆本仓库。
- **JDK (Java Development Kit)**: **`版本 17`** 或更高版本。
- **Apache Maven**: **`版本 3.8+`**，用于项目构建和依赖管理。
- **MySQL 数据库**: **`版本 8.0+`**，用于存储数据。
- **IDE (集成开发环境)**: 推荐使用 **IntelliJ IDEA**，但 Eclipse 或 VS Code (配合 Java 和 Maven 插件) 同样适用。

---

## 配置与设置步骤

请严格按照以下步骤操作，以确保项目可以顺利运行。

### 1. 克隆项目仓库

打开你的终端或 Git Bash，运行以下命令：
```bash
git clone [https://github.com/McKrispy/SWEII_rebuilt.git](https://github.com/McKrispy/SWEII_rebuilt.git)
cd SWEII_rebuilt
```

### 2. 设置数据库

应用程序需要一个名为 `refactored_se2` 的数据库。

a. **启动你的 MySQL 服务**。

b. **登录到 MySQL 并创建数据库**。
   打开终端或 MySQL 客户端，运行以下 SQL 命令：
   ```sql
   CREATE DATABASE refactored_se2;
   ```

c. **导入初始数据**。
   在**项目根目录**下打开终端，运行以下命令将 `database` 文件夹中的数据导入到你刚刚创建的数据库中。
   
   > **注意**: 请将 `your_username` 替换为你的 MySQL 用户名。执行后会提示你输入密码。

   ```bash
   mysql -u your_username -p refactored_se2 < database/Dump20250616.sql
   ```

### 3. 配置数据库连接

为了让 Java 程序能够连接到你的本地数据库，你需要提供用户名和密码。

a. 导航到 `src/main/resources/db_configuration/` 目录。

b. 将 `db.properties.example` 文件**复制并重命名**为 `db.properties`。

c. 打开新建的 `db.properties` 文件，修改其中的内容以匹配你的本地 MySQL 配置。
   
   > **提示**: `db.url` 中的数据库名和端口号通常不需要修改。你只需要填写正确的 `db.user` 和 `db.password`。如果你的 root 用户没有密码，请将 `db.password` 留空。

   ```properties
   # 示例配置
   db.url=jdbc:mysql://localhost:3306/refactored_se2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.user=root
   db.password=YOUR_SECRET_PASSWORD_HERE
   ```

### 4. 在 IDE 中打开并构建项目

a. 打开 IntelliJ IDEA，选择 `File` -> `Open...`，然后选择你克隆下来的 `SWEII_rebuilt` 文件夹。

b. IntelliJ IDEA 会自动识别 `pom.xml` 文件并将其识别为 Maven 项目。等待它自动下载所有必需的依赖项（如 JavaFX, MySQL Connector 等）。

c. 如果依赖没有自动下载，请打开右侧的 **Maven** 工具栏，点击**刷新按钮** ("Reload All Maven Projects")。

---

## 运行项目

你有两种推荐的方式来运行此应用程序：

### 方式一：通过 IntelliJ IDEA (最简单)

1.  在项目视图中，导航到 `src/main/java/groupf/recipeapp/App.java`。
2.  找到 `main` 方法。
3.  点击方法旁边的绿色三角形**运行按钮**，并选择 `Run 'App.main()'`。

### 方式二：通过 Maven 命令行

1.  在项目根目录下打开终端。
2.  运行以下 Maven 命令：
    ```bash
    mvn javafx:run
    ```
    Maven 会编译项目并启动 JavaFX 应用程序。

---

## 项目结构

```
.
├── database/              # 存放数据库 .sql 转储文件
├── src/
│   ├── main/
│   │   ├── java/          # Java 源代码
│   │   └── resources/     # 存放非代码资源
│   │       ├── db_configuration/  # 数据库配置文件
│   │       └── groupf/recipeapp/fxml/ # FXML 视图文件
│   └── test/              # 测试代码 (当前为空)
├── .gitignore             # Git 忽略规则
├── pom.xml                # Maven 项目配置文件 (核心)
└── README.md              # 项目说明文件
```

---

## 技术栈

- **Java 17**: 核心编程语言
- **JavaFX 21**: 用于构建图形用户界面 (GUI)
- **Maven**: 项目构建和依赖管理工具
- **MySQL 8.0**: 关系型数据库
