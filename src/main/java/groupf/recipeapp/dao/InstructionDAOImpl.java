package groupf.recipeapp.dao;

import groupf.recipeapp.dao.InstructionDAO;
import groupf.recipeapp.entity.Instruction;
import groupf.recipeapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructionDAOImpl implements InstructionDAO {
    @Override
    public List<Instruction> getInstructionsByRecipeId(int recipeId) {
        List<Instruction> instructions = new ArrayList<>();

        // 移除 'id' 列的查询
        String sql = "SELECT stepNumber, description, recipe_id FROM instruction WHERE recipe_id = ? ORDER BY stepNumber";

        System.out.println("🔍 查询指令步骤，recipeId = " + recipeId);
        System.out.println("🔍 执行 SQL: " + sql);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);

            ResultSet rs = ps.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                // 移除 int id = rs.getInt("id");
                int stepNumber = rs.getInt("stepNumber");
                String description = rs.getString("description");
                // int retrievedRecipeId = rs.getInt("recipe_id"); // 可选：如果需要验证 recipe_id

                System.out.println("➡️ 找到步骤: stepNumber=" + stepNumber + ", description=" + description);

                Instruction instruction = new Instruction();
                // 移除 instruction.setId(id);
                instruction.setStepNumber(stepNumber);
                instruction.setDescription(description);
                instruction.setRecipeId(recipeId); // 或者使用 retrievedRecipeId

                instructions.add(instruction);
            }

            if (!hasResults) {
                System.out.println("⚠️ 查询结果为空，没有找到任何步骤！");
            }

        } catch (Exception e) {
            System.err.println("❌ 查询步骤时发生异常: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("🔚 查询结束，找到步骤总数: " + instructions.size());

        return instructions;
    }

    @Override
    public boolean insertInstruction(Instruction instruction) throws SQLException {
        // 移除 Statement.RETURN_GENERATED_KEYS，因为我们不依赖一个单独的生成ID
        String sql = "INSERT INTO instruction (recipe_id, stepNumber, description) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { // 移除 Statement.RETURN_GENERATED_KEYS

            stmt.setInt(1, instruction.getRecipe().getId()); // 使用 Recipe 对象的 ID
            stmt.setInt(2, instruction.getStepNumber()); // 应用程序提供 stepNumber
            stmt.setString(3, instruction.getDescription());
            
            int affectedRows = stmt.executeUpdate();
            // 移除获取生成的主键ID的代码
            return affectedRows > 0;
        }
    }

    @Override
    public boolean updateInstruction(Instruction instruction) throws SQLException {
        // 根据复合主键 recipe_id 和 stepNumber 更新，只更新 description
        String sql = "UPDATE instruction SET description = ? WHERE recipe_id = ? AND stepNumber = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, instruction.getDescription());
            ps.setInt(2, instruction.getRecipe().getId()); // Recipe 的 ID
            ps.setInt(3, instruction.getStepNumber()); // Step Number

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean deleteInstruction(int recipeId, int intStepNumber) throws SQLException {
        String sql = "DELETE FROM instruction WHERE recipe_id = ? AND stepNumber = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recipeId);
            stmt.setInt(2, intStepNumber);
            System.out.println("🗑️ 删除步骤：Recipe ID: " + recipeId + ", Step Number: " + intStepNumber);
            return stmt.executeUpdate() > 0;
        }
    }
}
