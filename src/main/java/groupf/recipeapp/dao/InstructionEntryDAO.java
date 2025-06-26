package groupf.recipeapp.dao;

import groupf.recipeapp.entity.InstructionEntry;
import java.util.List;
import java.sql.SQLException;

public interface InstructionEntryDAO {
    List<InstructionEntry> getInstructionEntriesByRecipeId(int recipeId);

    /**
     * 插入新的食材条目。
     * @param entry 食材条目对象。
     * @return 如果插入成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean insertInstructionEntry(InstructionEntry entry) throws SQLException;

    /**
     * 更新现有食材条目。
     * @param entry 食材条目对象。
     * @return 如果更新成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean updateInstructionEntry(InstructionEntry entry) throws SQLException;

    /**
     * 根据食谱ID和食材ID删除食材条目。
     * @param recipeId 食谱的ID。
     * @param ingredientId 食材的ID。
     * @return 如果删除成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean deleteInstructionEntriesByRecipeId(int recipeId) throws SQLException;
    boolean deleteInstructionEntry(int recipeId, int ingredientId) throws SQLException;
}
