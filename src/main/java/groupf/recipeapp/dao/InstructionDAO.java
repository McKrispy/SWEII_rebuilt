package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Instruction;
import java.util.List;

public interface InstructionDAO {
    List<Instruction> getInstructionsByRecipeId(int recipeId);
    boolean addInstruction(Instruction instruction);
    boolean deleteInstructionsByRecipeId(int recipeId);
}

