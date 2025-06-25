package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Ingredient;
import java.util.List;
import java.sql.SQLException;

public interface IngredientDAO {
    Ingredient getIngredientById(int id);
    /**
     * 根据食材名称获取食材。
     * @param name 食材名称。
     * @return 匹配的食材对象，如果没有找到则返回 null。
     * @throws SQLException 如果发生数据库访问错误。
     */
    Ingredient getIngredientByName(String name) throws SQLException;

    /**
     * 插入新的食材。
     * @param ingredient 食材对象。
     * @return 如果插入成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean insertIngredient(Ingredient ingredient) throws SQLException;

    List<Ingredient> getAllIngredients();

}
