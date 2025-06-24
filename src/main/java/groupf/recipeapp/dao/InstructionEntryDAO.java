package groupf.recipeapp.dao;

import groupf.recipeapp.entity.InstructionEntry;
import java.util.List;

public interface InstructionEntryDAO {
    List<InstructionEntry> getInstructionEntriesByRecipeId(int recipeId);

}
