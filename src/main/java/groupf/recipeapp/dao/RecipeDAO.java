package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Recipe;
import java.util.List;

/**
 * RecipeDAO 接口定义了对 Recipe 数据访问的操作。
 */
public interface RecipeDAO {
    /**
     * 获取所有食谱。
     * @return 包含所有食谱的列表。
     */
    List<Recipe> getAllRecipes();

    /**
     * 根据名称搜索食谱。
     * @param name 食谱名称关键字。
     * @return 匹配名称的食谱列表。
     */
    List<Recipe> searchRecipesByName(String name);

    boolean insertRecipe(Recipe recipe);
}