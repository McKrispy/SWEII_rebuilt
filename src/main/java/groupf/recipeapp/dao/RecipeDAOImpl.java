package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Instruction;
import groupf.recipeapp.entity.InstructionEntry;
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
 * RecipeDAO interface implementation class, responsible for interacting with the database.
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
            ps.setString(1, "%" + name + "%"); // fuzzy matching
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
     * create a Recipe object from ResultSet.
     * use this method to map ResultSet to Recipe.
     */
    private Recipe createRecipeFromResultSet(ResultSet rs) throws SQLException {
        // use the no-arg constructor
        Recipe recipe = new Recipe();
        // ensure the field names match the aliases or actual column names in the SQL query
        recipe.setId(rs.getInt("id")); // previously was "recipe_id"
        recipe.setName(rs.getString("name"));
        recipe.setDescription(rs.getString("description"));
        recipe.setServings(rs.getInt("servings")); // fix: setServingSize -> setServings, "serving_size" -> "servings"
        recipe.setImagePath(rs.getString("imagePath"));

        int regionId = rs.getInt("region_id");
        if (!rs.wasNull()) { // check if region_id is NULL
            String regionName = rs.getString("region_name");
            String regionCode = rs.getString("region_code");
            Region region = new Region(regionId, regionName, regionCode);
            recipe.setRegion(region);
        }

        return recipe;
    }

    @Override
    public List<Recipe> getRecipesByRegion(int regionId) {
        List<Recipe> recipes = new ArrayList<>();
        // ensure the column names match the ones used in createRecipeFromResultSet
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
                recipes.add(createRecipeFromResultSet(rs)); // use createRecipeFromResultSet to map ResultSet to Recipe
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
        // ensure the column names match the ones used in createRecipeFromResultSet
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
                recipes.add(createRecipeFromResultSet(rs)); // use createRecipeFromResultSet to map ResultSet to Recipe
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
                ps.setNull(5, java.sql.Types.INTEGER); // if no region, set to NULL
            }
            ps.setInt(6, recipe.getId()); // update by ID

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } finally {
            DBUtil.closeConnection(connection);
            closeResources(ps, null); // here ResultSet is null
        }
    }

    @Override
    public boolean insertRecipe(Recipe recipe) {
        String insertRecipeSQL = "INSERT INTO recipe (name, description, servings, imagePath, region_id) VALUES (?, ?, ?, ?, ?)";
        String insertInstructionSQL = "INSERT INTO instruction (recipe_id, stepNumber, description) VALUES (?, ?, ?)";
        String insertInstructionEntrySQL = "INSERT INTO instructionentry (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
        String findIngredientSQL = "SELECT id FROM ingredient WHERE name = ?";
        String insertIngredientSQL = "INSERT INTO ingredient (name) VALUES (?)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // manually control the transaction

            // insert recipe
            int recipeId;
            try (PreparedStatement ps = conn.prepareStatement(insertRecipeSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, recipe.getName());
                ps.setString(2, recipe.getDescription());
                ps.setInt(3, recipe.getServings());
                ps.setString(4, recipe.getImagePath());
                ps.setInt(5, recipe.getRegion().getId());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        recipeId = rs.getInt(1);
                        recipe.setId(recipeId); // update the id in the object
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // insert instructions
            try (PreparedStatement ps = conn.prepareStatement(insertInstructionSQL)) {
                for (Instruction ins : recipe.getInstructions()) {
                    ps.setInt(1, recipeId);
                    ps.setInt(2, ins.getStepNumber());
                    ps.setString(3, ins.getDescription());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // insert InstructionEntry (including ingredient lookup or creation)
            try (
                    PreparedStatement findIngredientStmt = conn.prepareStatement(findIngredientSQL);
                    PreparedStatement insertIngredientStmt = conn.prepareStatement(insertIngredientSQL, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement insertEntryStmt = conn.prepareStatement(insertInstructionEntrySQL)
            ) {
                for (InstructionEntry entry : recipe.getInstructionEntries()) {
                    int ingredientId = -1;
                    String ingredientName = entry.getIngredient().getName().trim();

                    // 1. find ingredient
                    findIngredientStmt.setString(1, ingredientName);
                    try (ResultSet rs = findIngredientStmt.executeQuery()) {
                        if (rs.next()) {
                            ingredientId = rs.getInt("id");
                        } else {
                            // 2. if not found, insert new ingredient
                            insertIngredientStmt.setString(1, ingredientName);
                            insertIngredientStmt.executeUpdate();
                            try (ResultSet rs2 = insertIngredientStmt.getGeneratedKeys()) {
                                if (rs2.next()) {
                                    ingredientId = rs2.getInt(1);
                                } else {
                                    conn.rollback();
                                    return false;
                                }
                            }
                        }
                    }

                    // 3. insert instructionentry
                    insertEntryStmt.setInt(1, recipeId);
                    insertEntryStmt.setInt(2, ingredientId);
                    insertEntryStmt.setDouble(3, entry.getQuantity());
                    insertEntryStmt.setString(4, entry.getUnit());
                    insertEntryStmt.addBatch();
                }
                insertEntryStmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteRecipe(int recipeId) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        boolean success = false;

        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false); // start transaction

            // 1. delete all InstructionEntry related to this recipe
            String deleteEntriesSql = "DELETE FROM instructionentry WHERE recipe_id = ?";
            ps = connection.prepareStatement(deleteEntriesSql);
            ps.setInt(1, recipeId);
            ps.executeUpdate();
            ps.close(); // close the current PreparedStatement

            // 2. delete all Instruction related to this recipe
            String deleteInstructionsSql = "DELETE FROM instruction WHERE recipe_id = ?";
            ps = connection.prepareStatement(deleteInstructionsSql);
            ps.setInt(1, recipeId);
            ps.executeUpdate();
            ps.close(); // close the current PreparedStatement

            // 3. delete the recipe itself
            String deleteRecipeSql = "DELETE FROM recipe WHERE id = ?";
            ps = connection.prepareStatement(deleteRecipeSql);
            ps.setInt(1, recipeId);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                connection.commit(); // commit transaction
                success = true;
            } else {
                connection.rollback(); // rollback transaction
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // rollback transaction when error occurs
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            System.err.println("Error deleting recipe with ID " + recipeId + ": " + e.getMessage());
            throw e; // rethrow the exception, let the upper layer handle it
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // restore
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
     * close PreparedStatement and ResultSet.
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