package com.project.innovator.momsrecipe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.views.MaterialDialogViewHolder;

import java.util.List;

public class MaterialDialogAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView.ViewHolder viewHolder;
    private int selectResourceIndex = -1;

    private List<Integer> resources;

    public MaterialDialogAdapter(Context context, List<Integer> resources) {
        this.context = context;
        this.resources = resources;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.dialog_material_item, parent, false);
        return new MaterialDialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ((MaterialDialogViewHolder)holder).getImageView().setImageResource(resources.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSelectState(viewHolder);
                changeSelectState(holder);
                viewHolder = holder;
                selectResourceIndex = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public int getSelectResourceIndex() {
        return selectResourceIndex;
    }

    private void changeSelectState(RecyclerView.ViewHolder holder) {
        if (holder != null) {
            ImageView checkView = ((MaterialDialogViewHolder) holder).getCheckView();
            if (checkView.getVisibility() == View.VISIBLE) {
                checkView.setVisibility(View.INVISIBLE);
            } else if (checkView.getVisibility() == View.INVISIBLE) {
                checkView.setVisibility(View.VISIBLE);
            }
        }
    }
}
