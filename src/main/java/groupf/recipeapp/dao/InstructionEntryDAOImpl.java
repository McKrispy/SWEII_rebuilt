package groupf.recipeapp.dao;

import groupf.recipeapp.entity.InstructionEntry;
import groupf.recipeapp.entity.Ingredient;
import groupf.recipeapp.entity.Recipe;
import groupf.recipeapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructionEntryDAOImpl implements InstructionEntryDAO {

    private final IngredientDAO ingredientDAO = new IngredientDAOImpl();
    @Override
    public List<InstructionEntry> getInstructionEntriesByRecipeId(int recipeId) {
        System.out.println("query instruction entries, recipeId = " + recipeId); 

        List<InstructionEntry> entries = new ArrayList<>();
        String sql = "SELECT recipe_id, ingredient_id, quantity, unit FROM instructionentry WHERE recipe_id = ?"; 

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recipeId);

            System.out.println("execute SQL: " + sql + " 参数: recipe_id = " + recipeId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                InstructionEntry entry = new InstructionEntry();
                
                int retrievedRecipeId = rs.getInt("recipe_id");
                int ingredientId = rs.getInt("ingredient_id");
                double quantity = rs.getDouble("quantity");
                String unit = rs.getString("unit");

                System.out.println("found one record: recipe_id = " + retrievedRecipeId + ", ingredient_id = " + ingredientId + ", quantity = " + quantity + ", unit = " + unit);

                // create a temporary Recipe object, only set the ID, for InstructionEntry association
                Recipe tempRecipe = new Recipe();
                tempRecipe.setId(retrievedRecipeId);
                entry.setRecipe(tempRecipe);

                Ingredient ingredient = ingredientDAO.getIngredientById(ingredientId);
                if (ingredient == null) {
                    System.err.println("ingredient_id = " + ingredientId + " not found in ingredient table");
                    entry.setIngredient(null);
                } else {
                    entry.setIngredient(ingredient);
                }
                
                entry.setQuantity(quantity);
                entry.setUnit(unit);
                
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("error when querying instructionentry: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("loaded " + entries.size() + " instruction entries");
        return entries;
    }

    @Override
    public boolean insertInstructionEntry(InstructionEntry entry) throws SQLException {
        String sql = "INSERT INTO instructionentry (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entry.getRecipe().getId()); // use the ID of the Recipe object
            ps.setInt(2, entry.getIngredient().getId());
            ps.setDouble(3, entry.getQuantity());
            ps.setString(4, entry.getUnit());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean updateInstructionEntry(InstructionEntry entry) throws SQLException {
        // update the instruction entry by the composite primary key recipe_id and ingredient_id
        String sql = "UPDATE instructionentry SET quantity = ?, unit = ? WHERE recipe_id = ? AND ingredient_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, entry.getQuantity());
            ps.setString(2, entry.getUnit());
            ps.setInt(3, entry.getRecipe().getId()); // the ID of the Recipe object
            ps.setInt(4, entry.getIngredient().getId()); // the ID of the Ingredient object

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean deleteInstructionEntry(int recipeId, int ingredientId) throws SQLException {
        String sql = "DELETE FROM instructionentry WHERE recipe_id = ? AND ingredient_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);
            ps.setInt(2, ingredientId);
            int affectedRows = ps.executeUpdate();
            System.out.println("delete instruction entry: recipeId=" + recipeId + ", ingredientId=" + ingredientId);
            return affectedRows > 0;
        }
    }

    @Override
    public boolean deleteInstructionEntriesByRecipeId(int recipeId) throws SQLException {
        String sql = "DELETE FROM instructionentry WHERE recipe_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recipeId);
            int affectedRows = ps.executeUpdate();
            return affectedRows >= 0; // delete 0 or more rows is successful
        }
    }


}
