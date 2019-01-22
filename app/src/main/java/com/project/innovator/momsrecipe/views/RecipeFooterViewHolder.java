package com.project.innovator.momsrecipe.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.project.innovator.momsrecipe.R;

public class RecipeFooterViewHolder extends RecyclerView.ViewHolder{

    public ImageButton addButton;
    public RecipeFooterViewHolder(final View itemView) {
        super(itemView);
        addButton = itemView.findViewById(R.id.recipe_footer);
    }
}
