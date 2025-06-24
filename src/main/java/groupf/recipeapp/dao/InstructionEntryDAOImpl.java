package groupf.recipeapp.dao;

import groupf.recipeapp.entity.InstructionEntry;
import groupf.recipeapp.entity.Ingredient;
import groupf.recipeapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructionEntryDAOImpl implements InstructionEntryDAO {

    private final IngredientDAO ingredientDAO = new IngredientDAOImpl();
    @Override
    public List<InstructionEntry> getInstructionEntriesByRecipeId(int recipeId) {
        System.out.println("🛠️ 正在从 instruction_entries 表中查询，使用的 recipe_id = " + recipeId); // ✅ 打印 recipe_id

        List<InstructionEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM instructionentry WHERE recipe_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recipeId);

            System.out.println("📥 SQL 即将执行: " + sql + " 参数: recipe_id = " + recipeId); // ✅ SQL 执行前打印

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int ingredientId = rs.getInt("ingredient_id");
                double quantity = rs.getDouble("quantity");
                String unit = rs.getString("unit");

                System.out.println("📦 查得一条记录: ingredient_id = " + ingredientId + ", quantity = " + quantity + ", unit = " + unit);

                Ingredient ingredient = ingredientDAO.getIngredientById(ingredientId);
                if (ingredient == null) {
                    System.err.println("⚠️ ingredient_id = " + ingredientId + " 在 ingredient 表中未找到！");
                    continue;
                }

                InstructionEntry entry = new InstructionEntry(ingredient, quantity, unit);
                entry.setRecipeId(recipeId);
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("❌ 查询 instruction_entries 时出错: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("✅ 共加载 " + entries.size() + " 条 instruction entry");
        return entries;
    }

}
