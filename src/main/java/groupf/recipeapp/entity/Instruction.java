package groupf.recipeapp.entity;

public class Instruction {
	private int recipeId;
    private int stepNumber;
    private String description;

    
    //constructor
	public Instruction(int stepNumber, String description) {
		this.stepNumber = stepNumber;
		this.description = description;
	}

	//getters&setters
	public int getStepNumber() {
		return stepNumber;
	}

	public String getDescription() {
		return description;
	}

	public int getRecipeId() {
		return recipeId;
	}

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
	

    @Override
    public String toString() {
        return "Step " + stepNumber + ": " + description;
    }

}
