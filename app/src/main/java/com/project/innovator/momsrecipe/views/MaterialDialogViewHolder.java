package com.project.innovator.momsrecipe.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.project.innovator.momsrecipe.R;

public class MaterialDialogViewHolder extends RecyclerView.ViewHolder {

    private ImageView checkView;
    private ImageView imageView;

    public MaterialDialogViewHolder(View itemView) {
        super(itemView);
        checkView = itemView.findViewById(R.id.dialog_material_check);
        imageView = itemView.findViewById(R.id.dialog_material_image);
    }

    public ImageView getCheckView() {
        return checkView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
