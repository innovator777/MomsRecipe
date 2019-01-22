package com.project.innovator.momsrecipe.views;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.project.innovator.momsrecipe.R;

public class StandardFooterViewHolder extends RecyclerView.ViewHolder {

    public ImageButton addButton;
    public StandardFooterViewHolder(final View itemView) {
        super(itemView);
        addButton = itemView.findViewById(R.id.standard_footer);

    }
}
