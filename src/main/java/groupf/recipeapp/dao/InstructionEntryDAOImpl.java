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
        System.out.println("🛠️ 正在从 instructionentry 表中查询，使用的 recipe_id = " + recipeId);

        List<InstructionEntry> entries = new ArrayList<>();
        // 明确选择所有列，但不再选择 'id'
        String sql = "SELECT recipe_id, ingredient_id, quantity, unit FROM instructionentry WHERE recipe_id = ?"; 

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recipeId);

            System.out.println("📥 SQL 即将执行: " + sql + " 参数: recipe_id = " + recipeId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                InstructionEntry entry = new InstructionEntry();
                // 移除 entry.setId(rs.getInt("id"));
                
                int retrievedRecipeId = rs.getInt("recipe_id");
                int ingredientId = rs.getInt("ingredient_id");
                double quantity = rs.getDouble("quantity");
                String unit = rs.getString("unit");

                System.out.println("📦 查得一条记录: recipe_id = " + retrievedRecipeId + ", ingredient_id = " + ingredientId + ", quantity = " + quantity + ", unit = " + unit);

                // 创建一个临时的 Recipe 对象，只设置 ID，用于 InstructionEntry 关联
                Recipe tempRecipe = new Recipe();
                tempRecipe.setId(retrievedRecipeId);
                entry.setRecipe(tempRecipe);

                Ingredient ingredient = ingredientDAO.getIngredientById(ingredientId);
                if (ingredient == null) {
                    System.err.println("⚠️ ingredient_id = " + ingredientId + " 在 ingredient 表中未找到！");
                    entry.setIngredient(null);
                } else {
                    entry.setIngredient(ingredient);
                }
                
                entry.setQuantity(quantity);
                entry.setUnit(unit);
                
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("❌ 查询 instructionentry 时出错: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("✅ 共加载 " + entries.size() + " 条 instruction entry");
        return entries;
    }

    @Override
    public boolean insertInstructionEntry(InstructionEntry entry) throws SQLException {
        String sql = "INSERT INTO instructionentry (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { // 移除 Statement.RETURN_GENERATED_KEYS

            ps.setInt(1, entry.getRecipe().getId()); // 使用 Recipe 对象的 ID
            ps.setInt(2, entry.getIngredient().getId());
            ps.setDouble(3, entry.getQuantity());
            ps.setString(4, entry.getUnit());

            int affectedRows = ps.executeUpdate();
            // 移除获取生成的主键ID的代码
            return affectedRows > 0;
        }
    }

    @Override
    public boolean updateInstructionEntry(InstructionEntry entry) throws SQLException {
        // 使用复合主键 recipe_id 和 ingredient_id 来更新
        String sql = "UPDATE instructionentry SET quantity = ?, unit = ? WHERE recipe_id = ? AND ingredient_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, entry.getQuantity());
            ps.setString(2, entry.getUnit());
            ps.setInt(3, entry.getRecipe().getId()); // Recipe 的 ID
            ps.setInt(4, entry.getIngredient().getId()); // Ingredient 的 ID

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
            System.out.println("🗑️ 删除食材条目：Recipe ID: " + recipeId + ", Ingredient ID: " + ingredientId);
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
            return affectedRows >= 0; // 删除0条或多条都算成功
        }
    }


}
