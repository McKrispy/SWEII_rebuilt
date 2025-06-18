package groupf.recipeapp.entity;

public class EntityTest {
    public static void EntityTest(String[] args) {
        // 1. Create regions
        Region china = new Region(1, "China", "CN");
        Region italy = new Region(2, "Italy", "IT");
        Region international = new Region(3, "International", "INT");

        // 2. Create ingredients
        Ingredient chicken = new Ingredient(1, "Chicken Breast");
        Ingredient peanut = new Ingredient(2, "Peanut");
        Ingredient chili = new Ingredient(3, "Dried Chili");
        Ingredient pasta = new Ingredient(4, "Pasta");
        Ingredient tomato = new Ingredient(5, "Tomato");
        Ingredient lettuce = new Ingredient(6, "Lettuce");
        Ingredient cucumber = new Ingredient(7, "Cucumber");

        // 3. Create three recipes

        // Recipe 1: Kung Pao Chicken
        Recipe kungPaoChicken = new Recipe("Kung Pao Chicken", 2);
        kungPaoChicken.setRegion(china);
        kungPaoChicken.setDescription("Classic Sichuan dish, spicy and flavorful");
        
        // Add ingredients
        kungPaoChicken.addIngredient(new InstructionEntry(chicken, 300, "grams"));
        kungPaoChicken.addIngredient(new InstructionEntry(peanut, 100, "grams"));
        kungPaoChicken.addIngredient(new InstructionEntry(chili, 10, "pieces"));
        
        // Add instructions
        kungPaoChicken.addInstruction(new Instruction(1, "Dice chicken breast, marinate with cooking wine and starch for 10 mins"));
        kungPaoChicken.addInstruction(new Instruction(2, "Fry peanuts until golden, set aside"));
        kungPaoChicken.addInstruction(new Instruction(3, "Stir-fry dried chili, add chicken until color changes"));
        kungPaoChicken.addInstruction(new Instruction(4, "Add seasonings and peanuts, stir well"));

        // Recipe 2: Spaghetti
        Recipe spaghetti = new Recipe("Classic Spaghetti", 4);
        spaghetti.setRegion(italy);
        spaghetti.setDescription("Simple and delicious traditional Italian pasta");
        
        // Add ingredients
        spaghetti.addIngredient(new InstructionEntry(pasta, 400, "grams"));
        spaghetti.addIngredient(new InstructionEntry(tomato, 500, "grams"));
        
        // Add instructions
        spaghetti.addInstruction(new Instruction(1, "Cook pasta in boiling water for 8-10 minutes"));
        spaghetti.addInstruction(new Instruction(2, "Chop tomatoes, sauté with olive oil to make sauce"));
        spaghetti.addInstruction(new Instruction(3, "Mix cooked pasta with tomato sauce"));

        // Recipe 3: Vegetable Salad
        Recipe salad = new Recipe("Healthy Vegetable Salad", 2);
        salad.setRegion(international);
        salad.setDescription("Refreshing and healthy light meal option");
        
        // Add ingredients
        salad.addIngredient(new InstructionEntry(lettuce, 200, "grams"));
        salad.addIngredient(new InstructionEntry(cucumber, 1, "piece"));
        salad.addIngredient(new InstructionEntry(tomato, 2, "pieces"));
        
        // Add instructions
        salad.addInstruction(new Instruction(1, "Wash and chop all vegetables"));
        salad.addInstruction(new Instruction(2, "Add olive oil, lemon juice and salt to taste"));
        salad.addInstruction(new Instruction(3, "Toss gently and serve"));

        // 4. Test functionality (same as before)
        System.out.println("========== All Recipes ==========");
        System.out.println(kungPaoChicken);
        System.out.println(spaghetti);
        System.out.println(salad);
        salad.scaleIngredients(4);
        System.out.println("After scaling:\n" + salad);
}

}
