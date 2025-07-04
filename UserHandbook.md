## Digital Cookbook User Manual

Welcome to Digital Cookbook! This application is designed to help you easily manage and explore a variety of recipes. You can browse recipes, search by region, create your own recipes, and even edit and delete existing ones.

### 1. Application Overview (Main Interface)

When you launch Digital Cookbook, you will see the main interface (`MainView`). This interface is your primary hub for interacting with the application.

**Main Interface Layout:**

-   **Top Area:**
    *   **Welcome Title:** Displays "Welcome to Digital Cookbook".
    *   **Search Bar:** A text field for entering keywords to search for recipes.
    *   **Search Button:** Click this button to perform the search.
-   **Left Navigation Bar:**
    *   **Homepage:** Returns to the main interface and loads all recipes (reapplies region filter if one was active previously).
    *   **Create Recipe:** Navigates to the page for creating a new recipe.
    *   **World Cuisines:** Navigates to the world map, where you can select a specific region to filter recipes.
    *   **Refresh:** Clears any active region filters and reloads all recipes.
-   **Center Area:**
    *   **Recipe List View:** On the left, displays a list of all available recipes.
    *   **Recipe Preview Area:** On the right, displays a preview of the currently selected recipe, including the recipe name, description, and image.
    *   **Full Recipe Button:** Below the preview area, click this button or double-click a recipe in the list to open a new window with full recipe details.

### 2. Browsing and Searching Recipes

**2.1 Browsing All Recipes**

-   By default, the main interface will display all recipes from the database.
-   If the recipe list doesn't seem to update or you want to clear any filters, you can click the "**Refresh**" button in the left navigation bar.

**2.2 Searching Recipes by Keyword**

1.  Enter the keywords for the recipe you want to search for (e.g., "chicken", "pasta") in the **Search Bar** at the top area.
2.  Click the "**Search**" button next to it.
3.  The recipe list will update to show recipes matching your keywords.
4.  If the search bar is empty and you click "Search", the application will reload all recipes (or filter by the current region).

**2.3 Viewing Recipe Preview**

1.  In the **Recipe List View** on the left, **single-click** a recipe you are interested in.
2.  The recipe's name, description, and image will be displayed in the **Recipe Preview Area** on the right.

### 3. Creating a New Recipe

1.  Click the "**Create Recipe**" button in the left navigation bar. You will be taken to the "Create Your New Recipe" interface.

2.  **Fill in Basic Recipe Information:**
    *   **Recipe Name:** Enter the name of the recipe (required).
    *   **Serving Amount:** Enter the serving amount for the recipe (required, must be a number).
    *   **Description:** Enter a detailed description of the recipe.
    *   **Region:** Select the region the recipe belongs to from the dropdown menu (required).
    *   **Image:** Click the "**Upload Image**" button. In the file chooser that appears, select an image file (supports .png, .jpg, .jpeg, .gif). The image will be copied to the application's resources directory, and its filename will be displayed.

3.  **Adding Ingredients:**
    *   Click the "**+ Add Ingredient**" button.
    *   A new row of input fields will appear, including "Weight", "Unit", and "Ingredient Name".
    *   Fill in the corresponding information.
    *   To remove an ingredient row, click the "**Delete**" button next to that row.
    *   Repeat this step to add all ingredients.

4.  **Adding Instructions:**
    *   Click the "**+ Add Instruction**" button.
    *   A new row with an input field for "Step" description will appear.
    *   Fill in the detailed instructions for each step.
    *   To remove an instruction row, click the "**Delete**" button next to that row.
    *   Repeat this step to add all cooking instructions.

5.  **Submitting or Returning:**
    *   After filling in all information, click the "**Commit**" button to save the new recipe to the database. If the submission is successful, you will receive a confirmation message, and the form will be cleared. If the information is incomplete or in an incorrect format, an error message will be displayed.
    *   If you wish to abandon creation and return to the main interface, click the "**Return**" button.

### 4. Viewing, Editing, and Deleting Recipes (Full Recipe Details)

**4.1 Viewing Full Recipe Details**

1.  In the recipe list view on the main interface, **double-click** the recipe you wish to view.
2.  Alternatively, after selecting a recipe in the preview area, click the "**Full Recipe**" button.
3.  A new window will open, displaying all the detailed information of the recipe, including its name, servings, region, description, image, ingredient list, and cooking instructions.

**4.2 Editing a Recipe**

1.  In the full recipe details window, click the "**Edit**" button at the bottom.
2.  The interface will switch to edit mode:
    *   The recipe name, servings, description, image path, and region will become editable input fields or dropdown menus.
    *   Ingredients and instructions will be displayed as editable rows, each with a "Delete" button.
    *   "**+ Add Ingredient**" and "**+ Add Instruction**" buttons will appear.
3.  You can modify any field:
    *   **Modify Ingredients:** Directly edit the weight, unit, and name of ingredients.
    *   **Add New Ingredients:** Click "**+ Add Ingredient**" and fill in the information for the new row.
    *   **Delete Ingredients:** Click the "**Delete**" button next to the ingredient row.
    *   **Modify Instructions:** Directly edit the description of the steps.
    *   **Add New Instructions:** Click "**+ Add Instruction**" and fill in the information for the new row.
    *   **Delete Instructions:** Click the "**Delete**" button next to the instruction row.
    *   **Upload New Image:** In edit mode, you can click the "**Upload**" button to select and upload a new image.
4.  **Scaling Servings:**
    *   Click the "**Calculate**" button.
    *   In the input box that appears, enter a multiplier (e.g., enter `2` to double the servings and ingredient quantities, or `0.5` to halve them).
    *   Click the "**Confirm**" button, and the ingredient quantities will be scaled according to the multiplier.
5.  After making your edits, click the "**Commit**" button at the bottom. The application will save all changes to the database, and the interface will revert to read-only display mode.

**4.3 Deleting a Recipe**

1.  In the full recipe details window, click the "**Delete Recipe**" button at the bottom.
2.  A confirmation dialog will appear.
3.  Click "**OK**" to confirm the deletion. The recipe will be permanently removed from the database, and the current window will close. Click "Cancel" to abort the operation.

**4.4 Closing the Window**

1.  Click the "**Close**" button at the bottom to close the full recipe details window.

### 5. Exploring World Cuisines (Filtering by Region)

1.  Click the "**World Cuisines**" button in the left navigation bar. You will enter an interface displaying a world map.
2.  On the map, **click** a region you are interested in.
3.  The name of the selected region will appear below the map (e.g., "You selected: Asia region").
4.  Click the "**View Region Details**" button. The application will return to the main interface, and the recipe list will only display recipes belonging to that region.
5.  Click the "**Back Home**" button to return directly to the main interface without applying any region filter.
