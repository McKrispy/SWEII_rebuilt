package groupf.recipeapp.entity;
//store how much ingredients is needed e.g. 400 grams beef
public class InstructionEntry {
    private Recipe recipe; // 修改：从 int recipeId 变为 Recipe 对象
    private Ingredient ingredient;
    private double quantity;
    private String unit;

	//constructor
    public InstructionEntry() {
        // 新增：无参构造函数，以支持 `new InstructionEntry()` 的调用
    }
    
    //constructor
	public InstructionEntry(Ingredient ingredient, double quantity, String unit) {
		this.ingredient = ingredient;
		this.quantity = quantity;
		this.unit = unit;
	}

	// 新增一个包含ID、Recipe和基本信息的构造函数 (可选，但推荐)
    public InstructionEntry(Integer id, Recipe recipe, Ingredient ingredient, double quantity, String unit) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

		
	public void scaleQuantity(double ratio) {
	    this.quantity *= ratio;
	}

    // 新增：Recipe 对象的 getter 和 setter
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

   // 将 getRecipeId() 和 setRecipeId() 恢复到它们原来的 int 类型，但仍然建议迁移到使用 Recipe 对象
    // 但是，由于DAO层需要 recipeId，这里暂时保留。
    public int getRecipeId() {
        return (recipe != null) ? recipe.getId() : 0; // 如果 recipe 为 null，返回0或默认值
    }

    public void setRecipeId(int recipeId) {
        if (this.recipe == null) {
            this.recipe = new Recipe(); // 创建一个空的 Recipe 对象
        }
        this.recipe.setId(recipeId); // 设置 Recipe 对象的 ID
    }


    @Override
    public String toString() {
        return quantity + " " + unit + " " + ingredient.getName();
    }
	
 
}
