package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Instruction;
import java.util.List;
import java.sql.SQLException;

public interface InstructionDAO {
    List<Instruction> getInstructionsByRecipeId(int recipeId);

    /**
     * insert a new instruction.
     * @param instruction the instruction object.
     * @return true if the insertion is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean insertInstruction(Instruction instruction) throws SQLException;

    /**
     * update an existing instruction.
     * @param instruction the instruction object.
     * @return true if the update is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean updateInstruction(Instruction instruction) throws SQLException;

    /**
     * delete an existing instruction.
     * @param recipeId the ID of the recipe.
     * @param stepNumber the step number.
     * @return true if the deletion is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean deleteInstruction(int recipeId, int stepNumber) throws SQLException;
}
