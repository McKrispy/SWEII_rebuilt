package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Recipe;
import java.util.List;
import java.sql.SQLException;

/**
 * RecipeDAO interface defines the operations for accessing Recipe data.
 */
public interface RecipeDAO {
    /**
     * get all recipes.
     * @return a list of all recipes.
     */
    List<Recipe> getAllRecipes();

    /**
     * search recipes by name.
     * @param name the keyword of the recipe name.
     * @return a list of recipes that match the name.
     */
    List<Recipe> searchRecipesByName(String name);

    boolean insertRecipe(Recipe recipe);

    /**
     * get recipes by region ID.
     * @param regionId the ID of the region.
     * @return a list of recipes in the region.
     */
    List<Recipe> getRecipesByRegion(int regionId);

    /**
     * search recipes by name and region ID.
     * @param name the keyword of the recipe name.
     * @param regionId the ID of the region.
     * @return a list of recipes that match the name and region.
     */
    List<Recipe> searchRecipesByNameAndRegion(String name, int regionId);

    /**
     * update the information of an existing recipe.
     * @param recipe the recipe object with updated information.
     * @return true if the update is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean updateRecipe(Recipe recipe) throws SQLException;

    /**
     * delete a recipe by its ID.
     * @param recipeId the ID of the recipe to delete.
     * @return true if the deletion is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean deleteRecipe(int recipeId) throws SQLException;
}