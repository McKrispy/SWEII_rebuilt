package groupf.recipeapp.controller;

import groupf.recipeapp.entity.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FullRecipeController {

    @FXML private Label recipeNameLabel;
    @FXML private ImageView recipeImageView;
    @FXML private Label descriptionLabel;
    @FXML private Label regionLabel;
    @FXML private Label servingLabel;
    @FXML private Label ingredientsLabel;
    @FXML private Label instructionsLabel;

    public void setRecipe(Recipe recipe) {
        recipeNameLabel.setText(recipe.getName());
        descriptionLabel.setText(recipe.getDescription());
       // regionLabel.setText(recipe.getRegion());
        servingLabel.setText(String.valueOf(recipe.getServings()));
       // ingredientsLabel.setText(String.join("\n", recipe.getIngredients()));
        //instructionsLabel.setText(String.join("\n", recipe.getInstructions()));

        if (recipe.getImagePath() != null) {
            recipeImageView.setImage(new Image("file:" + recipe.getImagePath()));
        }
    }
}
