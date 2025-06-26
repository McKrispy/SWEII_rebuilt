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

        // remove the query of 'id' column
        String sql = "SELECT stepNumber, description, recipe_id FROM instruction WHERE recipe_id = ? ORDER BY stepNumber";

        System.out.println("query instructions, recipeId = " + recipeId);
        System.out.println("execute SQL: " + sql);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);

            ResultSet rs = ps.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                int stepNumber = rs.getInt("stepNumber");
                String description = rs.getString("description");

                System.out.println("found step: stepNumber=" + stepNumber + ", description=" + description);

                Instruction instruction = new Instruction();
                instruction.setStepNumber(stepNumber);
                instruction.setDescription(description);
                instruction.setRecipeId(recipeId);

                instructions.add(instruction);
            }

            if (!hasResults) {
                System.out.println("query result is empty, no steps found!");
            }

        } catch (Exception e) {
            System.err.println("error when querying instructions: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("query completed, found " + instructions.size() + " steps");

        return instructions;
    }

    @Override
    public boolean insertInstruction(Instruction instruction) throws SQLException {
        String sql = "INSERT INTO instruction (recipe_id, stepNumber, description) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, instruction.getRecipe().getId()); // use the ID of the Recipe object
            stmt.setInt(2, instruction.getStepNumber()); // use the step number provided by the application
            stmt.setString(3, instruction.getDescription());
            
            int affectedRows = stmt.executeUpdate();
            // remove the code to get the generated primary key ID
            return affectedRows > 0;
        }
    }

    @Override
    public boolean updateInstruction(Instruction instruction) throws SQLException {
        // update the instruction by the composite primary key recipe_id and stepNumber, only update the description
        String sql = "UPDATE instruction SET description = ? WHERE recipe_id = ? AND stepNumber = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, instruction.getDescription());
            ps.setInt(2, instruction.getRecipe().getId()); // the ID of the Recipe object
            ps.setInt(3, instruction.getStepNumber()); // the step number

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
            System.out.println("delete step: recipeId=" + recipeId + ", stepNumber=" + intStepNumber);
            return stmt.executeUpdate() > 0;
        }
    }
}
