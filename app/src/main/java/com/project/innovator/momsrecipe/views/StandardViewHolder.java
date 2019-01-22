package com.project.innovator.momsrecipe.views;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.innovator.momsrecipe.R;


public class StandardViewHolder extends RecyclerView.ViewHolder {

    private TextView standardNameText;
    private ImageView standardImage;

    private ImageButton deleteButton;


    public StandardViewHolder(View itemView){
        //view 전달받아서 제공하기
        super(itemView);
        standardNameText = itemView.findViewById(R.id.standard_text);
        standardImage=itemView.findViewById(R.id.standard_image);
        deleteButton=itemView.findViewById(R.id.deleteButton);
    }

    public TextView getStandardNameText() {
        return standardNameText;
    }

    public ImageView getStandardImage() {
        return standardImage;
    }

    public ImageButton getDeleteButton() {
        return deleteButton;
    }
}
