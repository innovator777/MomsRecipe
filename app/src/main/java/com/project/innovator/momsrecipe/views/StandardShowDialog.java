package com.project.innovator.momsrecipe.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.adapter.StandardDialogAdapter;
import com.project.innovator.momsrecipe.models.Standard;

import java.util.List;


public class StandardShowDialog extends Dialog {

    private Context context;
    private RecyclerView.Adapter adapter;
    private List<Standard> items;


    private RecyclerView recyclerView;
    private Button closeButton;

    public StandardShowDialog(@NonNull Context context) {
        super(context);
    }

    public StandardShowDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected StandardShowDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public StandardShowDialog(Context context, List<Standard> items) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.items = items;
        Log.i("StandardDialog", "size : " + items.size());
    }

    public StandardShowDialog(Context context, RecyclerView.Adapter adapter) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams windowLayoutParams = new WindowManager.LayoutParams();
        windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        windowLayoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(windowLayoutParams);

        setContentView(R.layout.dialog_standard);

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        recyclerView = findViewById(R.id.dialog_standard_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if(adapter == null)
            if(items != null && !items.isEmpty())
                adapter = new StandardDialogAdapter(context, items);

        recyclerView.setAdapter(adapter);
    }

    public void reload() {
        adapter.notifyDataSetChanged();
    }
}
