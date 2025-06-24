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

        String sql = "SELECT stepNumber, description FROM instruction WHERE recipe_id = ? ORDER BY stepNumber";

        System.out.println("🔍 查询指令步骤，recipeId = " + recipeId);
        System.out.println("🔍 执行 SQL: " + sql);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);

            ResultSet rs = ps.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                int stepNumber = rs.getInt("stepNumber");
                String description = rs.getString("description");
                System.out.println("➡️ 找到步骤: stepNumber=" + stepNumber + ", description=" + description);

                Instruction instruction = new Instruction();
                instruction.setStepNumber(stepNumber);
                instruction.setDescription(description);
                instruction.setRecipeId(recipeId);

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
    public boolean addInstruction(Instruction instruction) {
        String sql = "INSERT INTO instruction (recipe_id, step_number, description) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, instruction.getRecipeId());
            stmt.setInt(2, instruction.getStepNumber());
            stmt.setString(3, instruction.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteInstructionsByRecipeId(int recipeId) {
        String sql = "DELETE FROM instruction WHERE recipe_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recipeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
