package groupf.recipeapp.entity;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String description;
    private int servings;
    private List<InstructionEntry> ingredients = new ArrayList<>();
    private List<Instruction> instructions = new ArrayList<>();
    private String imagePath;
    private Region region;
    
    //constructor

    public Recipe(String name, int servings) {
		super();
		this.name = name;
		this.servings = servings;
	}

    public void addIngredient(InstructionEntry entry) {
        ingredients.add(entry);
    }


	public void removeIngredient(InstructionEntry entry) {
        ingredients.remove(entry);
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void removeInstruction(int stepNumber) {
        instructions.removeIf(ins -> ins.getStepNumber() == stepNumber);
    }

    public void updateInstruction(int stepNumber, String newDescription) {
        for (Instruction ins : instructions) {
            if (ins.getStepNumber() == stepNumber) {
                ins.setDescription(newDescription);
                return;
            }
        }
    }

    public void scaleIngredients(int newServings) {
        double ratio = (double) newServings / servings;
        for (InstructionEntry entry : ingredients) {
            entry.scaleQuantity(ratio);
        }
        this.servings = newServings;
    }

    public boolean matchesName(String query) {
        return name.toLowerCase().contains(query.toLowerCase());
    }

    //getters&setters
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getServings() {
		return servings;
	}


	public void setServings(int servings) {
		this.servings = servings;
	}


	public List<InstructionEntry> getIngredients() {
		return ingredients;
	}


	public void setIngredients(List<InstructionEntry> ingredients) {
		this.ingredients = ingredients;
	}


	public List<Instruction> getInstructions() {
		return instructions;
	}


	public void setInstructions(List<Instruction> instructions) {
		this.instructions = instructions;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public Region getRegion() {
		return region;
	}


	public void setRegion(Region region) {
		this.region = region;
	}
    
 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Recipe: " + name + "\n");
        sb.append("Servings: ").append(servings).append("\n");
        sb.append("Region: ").append(region != null ? region.getName() : "N/A").append("\n");
        sb.append("Ingredients:\n");
        for (InstructionEntry e : ingredients) {
            sb.append("- ").append(e).append("\n");
        }
        sb.append("Instructions:\n");
        for (Instruction i : getInstructions()) {
            sb.append(i).append("\n");
        }
        return sb.toString();
    }    



    
}
