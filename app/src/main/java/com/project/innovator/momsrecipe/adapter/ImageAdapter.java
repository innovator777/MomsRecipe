package com.project.innovator.momsrecipe.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.innovator.momsrecipe.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private ArrayList<Bitmap> mDataset;

    public ImageAdapter(ArrayList<Bitmap> mDataset){
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_picture_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mImage.setImageBitmap(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView)itemView.findViewById(R.id.imgView);
        }
    }
}
