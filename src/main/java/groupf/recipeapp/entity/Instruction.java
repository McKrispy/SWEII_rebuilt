package groupf.recipeapp.entity;

public class Instruction {
    private int stepNumber;
    private String description;
    private Recipe recipe; // Reference to the Recipe object
    private int recipeId; // For convenience, if needed

    
    //constructor
	public Instruction() {

	}

	// 更新构造函数，移除 id 参数
    public Instruction(int stepNumber, String description, Recipe recipe) {
        this.stepNumber = stepNumber;
        this.description = description;
        this.recipe = recipe;
        if (recipe != null) {
            this.recipeId = recipe.getId();
        }
    }

    // 新增：Recipe 对象的 getter 和 setter
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        // 如果旧代码依赖于 getRecipeId()，这里可以同时设置 recipeId，但推荐直接使用 Recipe 对象
        // if (recipe != null) {
        //     this.recipeId = recipe.getId();
        // }
    }


    public int getStepNumber() {
        return stepNumber;
    }

    public String getDescription() {
        return description;
    }

    // 移除原有的 getRecipeId() 和 setRecipeId()，现在通过 Recipe 对象来管理
    // 如果您的一些旧代码确实需要 int 类型的 recipeId，可以保留一个包装方法
    @Deprecated // 标记为不推荐使用，建议修改调用方直接使用 getRecipe().getId()
    public int getRecipeId() {
        return (recipe != null) ? recipe.getId() : 0; // 返回0或抛出异常，取决于业务逻辑
    }

    @Deprecated // 标记为不推荐使用，建议修改调用方直接使用 setRecipe(Recipe recipe)
    public void setRecipeId(int recipeId) {
        // 警告：这种方式不推荐，因为它没有设置实际的 Recipe 对象。
        // 最好是通过 setRecipe(Recipe recipe) 来设置关联。
        // 为了兼容性，这里可以暂时留空或进行简单的设置。
        System.err.println("警告: 不推荐直接通过 int setRecipeId(int) 方法设置 Recipe ID。请考虑使用 setRecipe(Recipe recipe)。");
        if (this.recipe == null) {
            this.recipe = new Recipe(); // 创建一个空的 Recipe 对象
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
