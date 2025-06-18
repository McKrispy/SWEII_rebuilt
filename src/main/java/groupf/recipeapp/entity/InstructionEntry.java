package groupf.recipeapp.entity;
//store how much ingredients is needed e.g. 400 grams beef
public class InstructionEntry {
	private int recipeId;
    private Ingredient ingredient;
    private double quantity;
    private String unit;
    
    //constructor
	public InstructionEntry(Ingredient ingredient, double quantity, String unit) {
		this.ingredient = ingredient;
		this.quantity = quantity;
		this.unit = unit;
	}

		
	public void scaleQuantity(double ratio) {
	    this.quantity *= ratio;
	}


	//getters&setters
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

	public int getRecipeId() {
		return recipeId;
	}
	
	@Override
	public String toString() {
	    return quantity + " " + unit + " " + ingredient.getName();
	}
	
 
}
