package groupf.recipeapp.entity;

public class InstructionEntry {
    private Recipe recipe; 
    private Ingredient ingredient;
    private double quantity;
    private String unit;

	//constructor
    public InstructionEntry() {
        // no-arg constructor, to support the call of `new InstructionEntry()`
    }
    
    //constructor
	public InstructionEntry(Ingredient ingredient, double quantity, String unit) {
		this.ingredient = ingredient;
		this.quantity = quantity;
		this.unit = unit;
	}

	// add a constructor with ID, Recipe and basic information
    public InstructionEntry(Integer id, Recipe recipe, Ingredient ingredient, double quantity, String unit) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

		
	public void scaleQuantity(double ratio) {
	    this.quantity *= ratio;
	}

    // getter and setter for Recipe object
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // because DAO layer needs recipeId, here is temporarily retained.
    public int getRecipeId() {
        return (recipe != null) ? recipe.getId() : 0; // if recipe is null, return 0 or default value
    }

    public void setRecipeId(int recipeId) {
        if (this.recipe == null) {
            this.recipe = new Recipe(); // create an empty Recipe object
        }
        this.recipe.setId(recipeId); // set the ID of the Recipe object
    }


    @Override
    public String toString() {
        return quantity + " " + unit + " " + ingredient.getName();
    }
	
 
}
