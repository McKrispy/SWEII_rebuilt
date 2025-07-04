# GroupF Digital Cookbook

This project is a desktop application built with JavaFX and Maven, designed for managing and browsing recipe data. The application persists information through a MySQL database and allows users to create, edit, and query recipes.

The `App` class sets the main window title to *GroupF Digital Cookbook* upon startup.

## Table of Contents

  - [Environment Requirements](#environment-requirements)
  - [Quick Start](#quick-start)
  - [How to Run](#how-to-run)
  - [Core Features](#core-features)
  - [Project Structure](#project-structure)
  - [Technology Stack](#technology-stack)

-----

## Environment Requirements

Ensure the following software is installed:

  - Git
  - JDK 24
  - JavaFX 20
  - Apache Maven 3.8+
  - MySQL 8.0+
  - A Maven-compatible IDE (IntelliJ IDEA recommended)

-----

## Quick Start

1.  **Clone the Repository**

    ```bash
    git clone https://github.com/McKrispy/SWEII_rebuilt.git
    cd SWEII_rebuilt
    ```

2.  **Create Database**

    ```sql
    CREATE DATABASE refactored_se2;
    ```

    Import `Final_Version_Mysql_dump.sql` from the `database` directory:

    ```bash
    mysql -u your_username -p refactored_se2 < database/Final_Version_Mysql_dump.sql
    ```

3.  **Configure Database Connection**
    Create `db.properties` under `src/main/resources/db_configuration/` and replace `YOUR_SECRET_PASSWORD_HERE` with your MySQL password:

    ```properties
    db.url=jdbc:mysql://localhost:3306/refactored_se2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    db.user=root
    db.password=YOUR_SECRET_PASSWORD_HERE
    ```

4.  **Build the Project**
    Import the project root directory into your IDE. Maven will automatically download dependencies; if not, manually refresh the Maven project.

-----

## How to Run

### Run in IDE

Navigate to `src/main/java/groupf/recipeapp/App.java` and run the `main` method to start the application.

### Run from Command Line

```bash
mvn javafx:run
```

Maven will compile and launch the JavaFX application.

-----

## Core Features

  - **Recipe Search and Browsing**: The main interface (`MainView.fxml`) provides an intuitive search bar and recipe list. Users can quickly search for and preview recipe summaries and images, and click to view detailed information.
  - **Recipe Creation**: Through the `CreateRecipeView.fxml` interface, users can enter recipe names, serving sizes, descriptions, select regions, and upload images. This feature supports dynamic addition and editing of ingredient and instruction lists to ensure complete recipe information.
  - **World Map Filtering**: Utilizing `WorldMapView.fxml` and `worldMap.html` to display an interactive world map, users can click on regions on the map to filter and display recipes from corresponding areas, establishing a connection between geographical location and recipes.
  - **Full Recipe View and Editing**: In `FullRecipeView.fxml`, users can view all details of a recipe, including ingredients, instructions, and region information. Additionally, it supports modifying, deleting, and adjusting serving sizes of recipe content, ensuring real-time data updates.
  - **Data Persistence**: All user operations (e.g., creating, modifying, deleting recipes) interact with the MySQL database through the DAO (Data Access Object) layer, ensuring secure storage and efficient management of recipe data.

-----

## Project Structure

```bash
.
├── database/              # SQL database dump file, containing table structures and sample data required for database initialization.
├── src/
│   ├── main/
│   │   ├── java/              # Java source code directory
│   │   │   ├── groupf/recipeapp/       # Main application package
│   │   │   │   ├── App.java            # Application entry point, responsible for launching the JavaFX application and main window setup.
│   │   │   │   ├── controller/         # Controller classes for FXML views, handling user interaction and business logic.
│   │   │   │   │   ├── CreateRecipeController.java  # Controller for the create recipe view.
│   │   │   │   │   ├── FullRecipeController.java    # Controller for the full recipe view.
│   │   │   │   │   ├── MainViewController.java      # Controller for the main view.
│   │   │   │   │   └── WorldMapController.java      # Controller for the world map view.
│   │   │   │   ├── dao/                # Data Access Object interfaces and their implementations, responsible for database interaction.
│   │   │   │   │   ├── IngredientDAO.java
│   │   │   │   │   ├── IngredientDAOImpl.java
│   │   │   │   │   ├── InstructionDAO.java
│   │   │   │   │   ├── InstructionDAOImpl.java
│   │   │   │   │   ├── InstructionEntryDAO.java
│   │   │   │   │   ├── InstructionEntryDAOImpl.java
│   │   │   │   │   ├── RecipeDAO.java
│   │   │   │   │   ├── RecipeDAOImpl.java
│   │   │   │   │   ├── RegionDAO.java
│   │   │   │   │   └── RegionDAOImpl.java
│   │   │   │   ├── entity/             # Java entity classes corresponding to database tables.
│   │   │   │   │   ├── Ingredient.java
│   │   │   │   │   ├── Instruction.java
│   │   │   │   │   ├── InstructionEntry.java
│   │   │   │   │   ├── Recipe.java
│   │   │   │   │   └── Region.java
│   │   │   │   └── util/               # Utility classes, such as database connection utility `DBUtil.java`.
│   │   │   └── module-info.java        # Java module declaration file.
│   │   └── resources/          # Application resource files
│   │       ├── db_configuration/   # Database configuration directory, containing `db.properties`.
│   │       └── groupf/recipeapp/   # FXML layouts, HTML pages, and image resources
│   │           ├── fxml/           # JavaFX user interface layout files.
│   │           │   ├── CreateRecipeView.fxml
│   │           │   ├── FullRecipeView.fxml
│   │           │   ├── MainView.fxml
│   │           │   └── WorldMapView.fxml
│   │           ├── html/           # Contains `worldMap.html`, used for world map functionality.
│   │           │   └── worldMap.html
│   │           └── images/         # Stores image resources related to recipes.
│   └── test/                   # Test directory
├── pom.xml                 # Maven Project Object Model file, managing project dependencies, build configurations, and plugins.
└── README.md               # Project documentation.
```

-----

## Technology Stack

  - Jdk 24
  - JavaFX 20
  - Maven
  - MySQL 8.0
  - HTML
