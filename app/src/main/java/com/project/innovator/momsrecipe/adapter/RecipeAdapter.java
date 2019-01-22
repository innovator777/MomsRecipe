package com.project.innovator.momsrecipe.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.innovator.momsrecipe.FirebaseUtils;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.callback.RecipeAdapterListener;
import com.project.innovator.momsrecipe.models.Recipe;
import com.project.innovator.momsrecipe.views.RecipeFooterViewHolder;
import com.project.innovator.momsrecipe.views.RecipeViewHolder;

import java.util.HashMap;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private static final int FOOTERSIZE = 1;

    private List<Recipe> recipes;
    private RecipeAdapterListener listener;

    public RecipeAdapter( RecipeAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recipe_item, parent, false);
            RecipeViewHolder vh = new RecipeViewHolder(v);
            return vh;
        }
        else if(viewType == FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recipe_footer, parent, false);
            RecipeFooterViewHolder vh = new RecipeFooterViewHolder(v);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder) {
            if(position < recipes.size()) {
                final Recipe recipe = recipes.get(position);
                HashMap<String, String> images = recipe.getImages();
                final RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
                recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        listener.clickRecipeItem(recipe);
                        listener.removeRecipeItem(recipe);
                    }
                });
                recipeViewHolder.rListName.setText(recipe.getName());
                recipeViewHolder.rListIng.setText(recipe.getIngredients());
                if (recipe.getImages().size() != 0) {
                    FirebaseUtils.getTargetBitmap(images.get("image1"), new FirebaseUtils.ImageCallback() {
                        @Override
                        public void getImageDataCallback(boolean success, Bitmap bitmap) {
                            if (success == true) {
                                recipeViewHolder.rListImage.setImageBitmap(bitmap);
                            } else {
                                recipeViewHolder.rListImage.setImageResource(R.drawable.recipebaseimage);
                            }
                        }
                    });
                }
            }
        }
        else if (holder instanceof RecipeFooterViewHolder) {
            RecipeFooterViewHolder recipeFooterViewHolder = (RecipeFooterViewHolder) holder;
            recipeFooterViewHolder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.addRecipeItem();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (recipes != null)
            return recipes.size() + FOOTERSIZE;
        else
            return FOOTERSIZE;
    }

    @Override
    public int getItemViewType(int position) {
        if (checkFooterPosition(position))
            return FOOTER;
        else
            return ITEM;
    }

    private boolean checkFooterPosition(int position) {
        if(recipes != null)
            return position == recipes.size();
        else
            return true;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
