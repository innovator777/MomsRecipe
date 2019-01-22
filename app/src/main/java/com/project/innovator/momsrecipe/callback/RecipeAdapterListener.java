package com.project.innovator.momsrecipe.callback;

import com.project.innovator.momsrecipe.models.Recipe;

public interface RecipeAdapterListener {
    void addRecipeItem();
    void modifyRecipeItem(Recipe recipe);
    void removeRecipeItem(Recipe recipe);
    void clickRecipeItem(Recipe recipe);
}
