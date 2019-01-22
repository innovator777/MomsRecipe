package com.project.innovator.momsrecipe.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.innovator.momsrecipe.R;

public class StandardDialogViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private ImageView imageView;

    public StandardDialogViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.dialog_standard_name);
        imageView = itemView.findViewById(R.id.dialog_standard_image);
    }


    public TextView getNameTextView() {
        return nameTextView;
    }

    public ImageView getImageView() {
        return imageView;
    }


}
