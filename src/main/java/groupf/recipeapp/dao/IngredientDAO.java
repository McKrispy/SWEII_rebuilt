package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Ingredient;
import java.util.List;
import java.sql.SQLException;

public interface IngredientDAO {
    Ingredient getIngredientById(int id);
    /**
     * get the ingredient by the name.
     * @param name the name of the ingredient.
     * @return the ingredient object, if not found, return null.
     * @throws SQLException if an error occurs during database access.
     */
    Ingredient getIngredientByName(String name) throws SQLException;

    /**
     * insert a new ingredient.
     * @param ingredient the ingredient object.
     * @return true if the insertion is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean insertIngredient(Ingredient ingredient) throws SQLException;

    List<Ingredient> getAllIngredients();

}
