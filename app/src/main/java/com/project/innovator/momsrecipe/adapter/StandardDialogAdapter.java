package com.project.innovator.momsrecipe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.innovator.momsrecipe.FirebaseUtils;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.models.Standard;
import com.project.innovator.momsrecipe.views.StandardDialogViewHolder;

import java.util.List;

public class StandardDialogAdapter extends RecyclerView.Adapter {

    private static String TAG = StandardDialogAdapter.class.getName();

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Standard> items;

    private ImageView imageView;
    private TextView textView;

    public StandardDialogAdapter(Context context, List<Standard> items) {
        this.context = context;
        this.items = items;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.dialog_standard_item, parent, false);
        return new StandardDialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(items != null) {
            ((StandardDialogViewHolder)holder).getNameTextView().setText(items.get(position).getName());
            FirebaseUtils.getTargetBitmap(items.get(position).getImage(), new FirebaseUtils.ImageCallback() {
                @Override
                public void getImageDataCallback(boolean success, Bitmap bitmap) {
                    if(success)
                        ((StandardDialogViewHolder)holder).getImageView().setImageBitmap(bitmap);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return 0;
    }



}
