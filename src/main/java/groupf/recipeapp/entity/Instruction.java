package groupf.recipeapp.entity;

public class Instruction {
    private int stepNumber;
    private String description;
    private Recipe recipe; 
    private int recipeId; 

    
    //constructor
	public Instruction() {

	}

	// update the constructor, remove the id parameter
    public Instruction(int stepNumber, String description, Recipe recipe) {
        this.stepNumber = stepNumber;
        this.description = description;
        this.recipe = recipe;
        if (recipe != null) {
            this.recipeId = recipe.getId();
        }
    }

    // getter and setter for Recipe object
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


    public int getStepNumber() {
        return stepNumber;
    }

    public String getDescription() {
        return description;
    }


    @Deprecated 
    public int getRecipeId() {
        return (recipe != null) ? recipe.getId() : 0; // return 0 or throw an exception, depending on the business logic
    }

    @Deprecated 
    public void setRecipeId(int recipeId) {
        // for compatibility, here is temporarily retained. 
        System.err.println("Warning: it is not recommended to set the Recipe ID directly through the int setRecipeId(int) method. Please consider using setRecipe(Recipe recipe).");
        if (this.recipe == null) {
            this.recipe = new Recipe(); // create an empty Recipe object
        }
        this.recipe.setId(recipeId);
    }


    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

    @Override
    public String toString() {
        return "Step " + stepNumber + ": " + description;
    }

}
