package com.project.innovator.momsrecipe.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.adapter.MaterialDialogAdapter;
import com.project.innovator.momsrecipe.callback.MaterialViewCreateListener;
import com.project.innovator.momsrecipe.callback.MaterialViewModifyListener;
import com.project.innovator.momsrecipe.models.Material;

import java.util.List;

public class MaterialViewEditDialog extends Dialog {

    private static String TAG = MaterialViewEditDialog.class.getName();

    private Context context;
    private MaterialViewCreateListener materialViewCreateListener;
    private MaterialViewModifyListener materialViewModifyListener;
    private List<Integer> resources;
    private MaterialView materialView;

    private TextView titleTextView;
    private EditText nameEditText,
                     amoutEditText;
    private RecyclerView recyclerView;
    private MaterialDialogAdapter adapter;
    private Button doneButton,
                   cancelButton;

    public MaterialViewEditDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public MaterialViewEditDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public MaterialViewEditDialog(@NonNull Context context, int themeResId, MaterialViewCreateListener materialViewCreateListener) {
        super(context, themeResId);
        this.context = context;
        this.materialViewCreateListener = materialViewCreateListener;
    }

    public MaterialViewEditDialog(@NonNull Context context, int themeResId, MaterialViewCreateListener listener, List<Integer> resources) {
        super(context, themeResId);
        this.context = context;
        this.materialViewCreateListener = listener;
        this.resources = resources;
    }

    public MaterialViewEditDialog(@NonNull Context context, int themeResId, MaterialViewModifyListener listener, List<Integer> resources, MaterialView materialView) {
        super(context, themeResId);
        this.context = context;
        this.materialViewModifyListener = listener;
        this.resources = resources;
        this.materialView = materialView;
    }

    protected MaterialViewEditDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams windowLayoutParams = new WindowManager.LayoutParams();
        windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        windowLayoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(windowLayoutParams);

        setContentView(R.layout.dialog_material);

        titleTextView = findViewById(R.id.dialog_material_title_text);
        titleTextView.setText("식재료 추가");

        nameEditText = findViewById(R.id.dialog_material_name_edit_text);
        amoutEditText = findViewById(R.id.dialog_material_amout_edit_text);

        if(materialView != null) {
            nameEditText.setText(materialView.getMaterial().getName());
            amoutEditText.setText(materialView.getMaterial().getAmount());
        }

        recyclerView = findViewById(R.id.dialog_material_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if(materialView != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.findViewHolderForAdapterPosition(materialView.getMaterial().getResource()).itemView.callOnClick();
                }
            }, 200);
        }

        adapter = new MaterialDialogAdapter(context, resources);
        recyclerView.setAdapter(adapter);

        doneButton = findViewById(R.id.dialog_material_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectResourceIndex = -1;
                String materialName = nameEditText.getText().toString();
                String materialAmout = amoutEditText.getText().toString();
                if (adapter != null) {
                    selectResourceIndex = adapter.getSelectResourceIndex();
                }
                if (materialName.isEmpty()) {
                    Toast.makeText(context, "식재료명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (materialAmout.isEmpty()) {
                    Toast.makeText(context, "수량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (selectResourceIndex == -1) {
                    Toast.makeText(context, "식재료명 대표 이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Material material = new Material(materialName, materialAmout, selectResourceIndex);
                    if (materialViewCreateListener != null && materialViewModifyListener == null) {
                        materialViewCreateListener.createView(material);
                    }
                    else if (materialViewCreateListener == null && materialViewModifyListener != null && materialView != null) {
                        materialView.setMaterial(material);
                        materialViewModifyListener.modifyView(materialView);
                    }
                    dismiss();
                }
            }
        });

        cancelButton = findViewById(R.id.dialog_material_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
