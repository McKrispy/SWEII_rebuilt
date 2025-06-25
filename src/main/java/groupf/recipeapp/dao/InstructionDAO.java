package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Instruction;
import java.util.List;
import java.sql.SQLException;

public interface InstructionDAO {
    List<Instruction> getInstructionsByRecipeId(int recipeId);

    // 重命名 addInstruction 为 insertInstruction
    /**
     * 插入新的制作步骤。
     * @param instruction 制作步骤对象。
     * @return 如果插入成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean insertInstruction(Instruction instruction) throws SQLException;

    /**
     * 更新现有制作步骤。
     * @param instruction 制作步骤对象。
     * @return 如果更新成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean updateInstruction(Instruction instruction) throws SQLException;

    // 修正：将 deleteInstruction 方法签名改为接收 recipeId 和 stepNumber
    /**
     * 根据食谱ID和步骤编号删除制作步骤。
     * @param recipeId 食谱ID。
     * @param stepNumber 步骤编号。
     * @return 如果删除成功则返回 true，否则返回 false。
     * @throws SQLException 如果发生数据库访问错误。
     */
    boolean deleteInstruction(int recipeId, int stepNumber) throws SQLException;
}
