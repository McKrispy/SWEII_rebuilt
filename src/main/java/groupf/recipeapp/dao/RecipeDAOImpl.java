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
import java.sql.Statement;


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
     * 统一使用这个方法来映射 ResultSet 到 Recipe。
     */
    private Recipe createRecipeFromResultSet(ResultSet rs) throws SQLException {
        // 使用无参构造函数
        Recipe recipe = new Recipe();
        // 确保字段名与 SQL 查询中的别名或实际列名匹配
        recipe.setId(rs.getInt("id")); // 之前是 "recipe_id"
        recipe.setName(rs.getString("name"));
        recipe.setDescription(rs.getString("description"));
        recipe.setServings(rs.getInt("servings")); // 修复：setServingSize -> setServings, "serving_size" -> "servings"
        recipe.setImagePath(rs.getString("imagePath"));

        int regionId = rs.getInt("region_id");
        if (!rs.wasNull()) { // 检查 region_id 是否为 NULL
            String regionName = rs.getString("region_name");
            String regionCode = rs.getString("region_code");
            Region region = new Region(regionId, regionName, regionCode);
            // 这里不需要重复设置 region.setId/setName/setCode，因为构造函数已经设置了
            recipe.setRegion(region);
        }

        return recipe;
    }

    /**
     *
     * 插入菜谱，传入数据库
     * */
    @Override
    public boolean insertRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipe (name, description, servings, imagePath, region_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, recipe.getName());
            ps.setString(2, recipe.getDescription());
            ps.setInt(3, recipe.getServings());
            ps.setString(4, recipe.getImagePath());
            if (recipe.getRegion() != null) {
                ps.setInt(5, recipe.getRegion().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER); // 没有地区则设置为NULL
            }

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Recipe> getRecipesByRegion(int regionId) {
        List<Recipe> recipes = new ArrayList<>();
        // 确保这里的列名与 createRecipeFromResultSet 中使用的匹配
        String sql = "SELECT r.id, r.name, r.description, r.servings, r.imagePath, " +
                     "reg.id AS region_id, reg.name AS region_name, reg.code AS region_code " +
                     "FROM recipe r LEFT JOIN region reg ON r.region_id = reg.id WHERE r.region_id = ?";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, regionId);
            rs = ps.executeQuery();
            while (rs.next()) {
                recipes.add(createRecipeFromResultSet(rs)); // 统一调用 createRecipeFromResultSet
            }
        } catch (SQLException e) {
            System.err.println("Error getting recipes by region: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(connection);
            closeResources(ps, rs);
        }
        return recipes;
    }

    @Override
    public List<Recipe> searchRecipesByNameAndRegion(String name, int regionId) {
        List<Recipe> recipes = new ArrayList<>();
        // 确保这里的列名与 createRecipeFromResultSet 中使用的匹配
        String sql = "SELECT r.id, r.name, r.description, r.servings, r.imagePath, " +
                     "reg.id AS region_id, reg.name AS region_name, reg.code AS region_code " +
                     "FROM recipe r LEFT JOIN region reg ON r.region_id = reg.id " +
                     "WHERE r.name LIKE ? AND r.region_id = ?";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ps.setInt(2, regionId);
            rs = ps.executeQuery();
            while (rs.next()) {
                recipes.add(createRecipeFromResultSet(rs)); // 统一调用 createRecipeFromResultSet
            }
        } catch (SQLException e) {
            System.err.println("Error searching recipes by name and region: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(connection);
            closeResources(ps, rs);
        }
        return recipes;
    }

    @Override
    public boolean updateRecipe(Recipe recipe) throws SQLException {
        String sql = "UPDATE recipe SET name = ?, description = ?, servings = ?, imagePath = ?, region_id = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(sql);

            ps.setString(1, recipe.getName());
            ps.setString(2, recipe.getDescription());
            ps.setInt(3, recipe.getServings());
            ps.setString(4, recipe.getImagePath());
            if (recipe.getRegion() != null) {
                ps.setInt(5, recipe.getRegion().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER); // 没有地区则设置为NULL
            }
            ps.setInt(6, recipe.getId()); // 根据ID更新

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DBUtil.closeConnection(connection);
            closeResources(ps, null); // 这里 ResultSet 为 null
        }
    }

    @Override
    public boolean deleteRecipe(int recipeId) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        boolean success = false;

        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false); // 开始事务

            // 1. 删除与此食谱相关的所有 InstructionEntry
            String deleteEntriesSql = "DELETE FROM instruction_entry WHERE recipe_id = ?";
            ps = connection.prepareStatement(deleteEntriesSql);
            ps.setInt(1, recipeId);
            ps.executeUpdate();
            ps.close(); // 关闭当前的 PreparedStatement

            // 2. 删除与此食谱相关的所有 Instruction
            String deleteInstructionsSql = "DELETE FROM instruction WHERE recipe_id = ?";
            ps = connection.prepareStatement(deleteInstructionsSql);
            ps.setInt(1, recipeId);
            ps.executeUpdate();
            ps.close(); // 关闭当前的 PreparedStatement

            // 3. 删除食谱本身
            String deleteRecipeSql = "DELETE FROM recipe WHERE id = ?";
            ps = connection.prepareStatement(deleteRecipeSql);
            ps.setInt(1, recipeId);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                connection.commit(); // 提交事务
                success = true;
            } else {
                connection.rollback(); // 回滚事务
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // 发生错误时回滚事务
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            System.err.println("Error deleting recipe with ID " + recipeId + ": " + e.getMessage());
            throw e; // 重新抛出异常，让上层处理
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // 恢复自动提交模式
                } catch (SQLException e) {
                    System.err.println("Error setting auto commit to true: " + e.getMessage());
                }
            }
            closeResources(ps, null);
            DBUtil.closeConnection(connection);
        }
        return success;
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