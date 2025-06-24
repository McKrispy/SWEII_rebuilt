package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Ingredient;
import java.util.List;

public interface IngredientDAO {
    Ingredient getIngredientById(int id);
    Ingredient getIngredientByName(String name);
    List<Ingredient> getAllIngredients();
    boolean addIngredient(Ingredient ingredient);
}
