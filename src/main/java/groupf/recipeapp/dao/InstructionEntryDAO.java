package groupf.recipeapp.dao;

import groupf.recipeapp.entity.InstructionEntry;
import java.util.List;
import java.sql.SQLException;

public interface InstructionEntryDAO {
    List<InstructionEntry> getInstructionEntriesByRecipeId(int recipeId);

    /**
     * insert a new instruction entry.
     * @param entry the instruction entry object.
     * @return true if the insertion is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean insertInstructionEntry(InstructionEntry entry) throws SQLException;

    /**
     * update an existing instruction entry.
     * @param entry the instruction entry object.
     * @return true if the update is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean updateInstructionEntry(InstructionEntry entry) throws SQLException;

    /**
     * delete an existing instruction entry.
     * @param recipeId the ID of the recipe.
     * @param ingredientId the ID of the ingredient.
     * @return true if the deletion is successful, otherwise return false.
     * @throws SQLException if an error occurs during database access.
     */
    boolean deleteInstructionEntriesByRecipeId(int recipeId) throws SQLException;
    boolean deleteInstructionEntry(int recipeId, int ingredientId) throws SQLException;
}
