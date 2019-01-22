package com.project.innovator.momsrecipe.views;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.innovator.momsrecipe.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder{

    public ImageView rListImage;
    public TextView rListName;
    public TextView rListIng;
    public RecipeViewHolder(View itemView){
        super(itemView);
        rListImage = (ImageView)itemView.findViewById(R.id.rListImage);
        rListName = (TextView)itemView.findViewById(R.id.rListName);
        rListIng = (TextView)itemView.findViewById(R.id.rListIng);
    }
}