package com.project.innovator.momsrecipe.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.innovator.momsrecipe.callback.MaterialViewListener;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.models.Material;

public class MaterialView extends RelativeLayout {

    private static String TAG = MaterialView.class.getName();

    private Context context;
    private MaterialViewListener materialViewEditListener;

    private ImageView backgroundVIew;
    private ImageButton removeButton,
                        editButton;
    private TextView nameTextView;
    private TextView amountTextView;

    private Material material;
    private int widthSize,
                heightSize;

    public MaterialView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MaterialView(Context context, Material material) {
        super(context);
        this.context = context;
        this.material = material;
        init();
    }



    public MaterialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MaterialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = getMeasuredWidth();
        heightSize = getMeasuredHeight();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_material, this, false);

        backgroundVIew = view.findViewById(R.id.material_background_view);
        backgroundVIew.setBackgroundColor(Color.BLUE);

        removeButton = view.findViewById(R.id.material_remove_button);
        removeButton.setOnClickListener(removeButtonClickListener);
        removeButton.setBackgroundColor(Color.RED);
        removeButton.setVisibility(View.INVISIBLE);

        editButton = view.findViewById(R.id.material_edit_button);
        editButton.setOnClickListener(editButtonClickListener);
        editButton.setBackgroundColor(Color.YELLOW);
        editButton.setVisibility(View.INVISIBLE);



        nameTextView = view.findViewById(R.id.material_name_text);
        amountTextView = view.findViewById(R.id.material_amout_text);
        insertDataViews();

        addView(view);
    }

    private View.OnClickListener removeButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            materialViewEditListener.removeView(MaterialView.this);
        }
    };

    private View.OnClickListener editButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            materialViewEditListener.editView(MaterialView.this);
        }
    };

    public void insertDataViews() {
//        backgroundVIew.setBackgroundResource(material.getResource());
        if (material.getName() != null && !material.getName().isEmpty())
            nameTextView.setText(material.getName());

        if (material.getAmount() != null && !material.getAmount().isEmpty())
            amountTextView.setText(material.getAmount());
    }

    public void switchVisibleState(boolean visibleMode) {
        if(visibleMode) {
            removeButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.INVISIBLE);
        }
        else {
            removeButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
        }
    }

    public Rect getBounds() {
        int x = (int)getX();
        int y = (int)getY();
        return new Rect(x, y, x + widthSize, y + heightSize);
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setMaterialViewEditListener(MaterialViewListener listener) {
        materialViewEditListener = listener;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
