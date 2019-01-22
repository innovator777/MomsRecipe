package com.project.innovator.momsrecipe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.innovator.momsrecipe.FirebaseUtils;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.callback.StandardAdapterListener;
import com.project.innovator.momsrecipe.models.Standard;
import com.project.innovator.momsrecipe.views.StandardFooterViewHolder;
import com.project.innovator.momsrecipe.views.StandardViewHolder;

import java.util.List;

//item 정의
public class StandardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private static final int FOOTERSIZE = 1;

    private Context context;
    private LayoutInflater layoutInflater;
    //아이템 리스트
    private List<Standard> standards;
    private StandardAdapterListener listener;

    public StandardAdapter(Context context, StandardAdapterListener listener) {
        this.context = context;
        this.listener = listener;

        //부분 화면 위해 메모리에 객체화 하기위해 인플레이션 객체 필요
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    //onCreateViewHolder로 viewholder생성 view타입당 1개씩 생성
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //view 표현
        if (viewType == ITEM) {
            View view = layoutInflater.inflate(R.layout.view_standard_item, parent, false);
            return new StandardViewHolder(view);
        } else if (viewType == FOOTER) {
            View view = layoutInflater.inflate(R.layout.view_standard_footer, parent, false);
            return new StandardFooterViewHolder(view);
        }
        return null;
    }

    //holer이 View데이터 노출 정의
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StandardViewHolder) {
            if (position < standards.size()) {
                final Standard standard = standards.get(position);
                //data 노출
                if (standard != null) {
                    final StandardViewHolder standardViewHolder = (StandardViewHolder) holder;
                    standardViewHolder.getStandardNameText().setText(standard.getName());
                    standardViewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.removeStandard(standard);
                        }
                    });
                    FirebaseUtils.getTargetBitmap(standard.getImage(), new FirebaseUtils.ImageCallback() {
                        @Override
                        public void getImageDataCallback(boolean success, Bitmap bitmap) {
                            if (success == true) {
                                standardViewHolder.getStandardImage().setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            }
        } else if (holder instanceof StandardFooterViewHolder) {
            StandardFooterViewHolder standardFooterViewHolder = (StandardFooterViewHolder) holder;
            standardFooterViewHolder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.addStandard();
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        if (standards != null)
            return standards.size() + FOOTERSIZE;
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
        if (standards != null)
            return position == standards.size();
        else
            return true;
    }

    public void setStandards(List<Standard> standards) {
        this.standards = standards;
        notifyDataSetChanged();
    }
}
