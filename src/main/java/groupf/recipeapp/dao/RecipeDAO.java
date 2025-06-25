package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Recipe;
import java.util.List;
import java.sql.SQLException;

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

    /**
     * 根据地区ID获取食谱列表。
     * @param regionId 地区ID。
     * @return 该地区的所有食谱列表。
     */
    List<Recipe> getRecipesByRegion(int regionId);

    /**
     * 根据食谱名称和地区ID搜索食谱。
     * @param name 食谱名称的关键字。
     * @param regionId 地区ID。
     * @return 符合条件的食谱列表。
     */
    List<Recipe> searchRecipesByNameAndRegion(String name, int regionId);

    /**
     * 更新现有食谱的信息。
     * @param recipe 包含更新信息的食谱对象。
     * @return 如果更新成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean updateRecipe(Recipe recipe) throws SQLException;

    /**
     * 根据食谱ID删除食谱。
     * @param recipeId 要删除的食谱的ID。
     * @return 如果删除成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean deleteRecipe(int recipeId) throws SQLException;
}