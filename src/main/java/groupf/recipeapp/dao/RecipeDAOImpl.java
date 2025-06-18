package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Recipe;
import groupf.recipeapp.entity.Region;
import groupf.recipeapp.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RecipeDAO 接口的具体实现类，负责与数据库进行交互。
 */
public class RecipeDAOImpl implements RecipeDAO {

    @Override
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.id, r.name, r.description, r.servings, r.imagePath, " +
                     "reg.id AS region_id, reg.name AS region_name, reg.code AS region_code " +
                     "FROM recipe r LEFT JOIN region reg ON r.region_id = reg.id";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                recipes.add(createRecipeFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading all recipes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(connection);
            closeResources(ps, rs);
        }
        return recipes;
    }

    @Override
    public List<Recipe> searchRecipesByName(String name) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.id, r.name, r.description, r.servings, r.imagePath, " +
                     "reg.id AS region_id, reg.name AS region_name, reg.code AS region_code " +
                     "FROM recipe r LEFT JOIN region reg ON r.region_id = reg.id " +
                     "WHERE r.name LIKE ?";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + name + "%"); // 模糊匹配
            rs = ps.executeQuery();

            while (rs.next()) {
                recipes.add(createRecipeFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching recipes by name: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(connection);
            closeResources(ps, rs);
        }
        return recipes;
    }

    /**
     * 从 ResultSet 创建 Recipe 对象。
     */
    private Recipe createRecipeFromResultSet(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe(
                rs.getString("name"),
                rs.getInt("servings")
        );
        recipe.setId(rs.getInt("id"));
        recipe.setDescription(rs.getString("description"));
        recipe.setImagePath(rs.getString("imagePath"));

        int regionId = rs.getInt("region_id");
        if (!rs.wasNull()) { // 正确调用

            String regionName = rs.getString("region_name");
            String regionCode = rs.getString("region_code");
            Region region = new Region(regionId, regionName, regionCode);
            region.setId(regionId);
            region.setName(rs.getString("region_name"));
            region.setCode(rs.getString("region_code"));
            recipe.setRegion(region);
        }

        return recipe;
    }


    /**
     * 关闭 PreparedStatement 和 ResultSet。
     */
    private void closeResources(PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
}