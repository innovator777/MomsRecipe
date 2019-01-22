package com.project.innovator.momsrecipe.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.project.innovator.momsrecipe.callback.MaterialViewCreateListener;
import com.project.innovator.momsrecipe.callback.MaterialViewModifyListener;
import com.project.innovator.momsrecipe.callback.MaterialViewListener;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.models.Material;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorLayout extends RelativeLayout {

    private static String TAG = RefrigeratorLayout.class.getName();
    private static boolean VISIBLE_MODE = false;

    private List<MaterialView> materialViews = new ArrayList<>();
    private MaterialView targetMaterialView = null;
    private int offsetX, offsetY;
    private List<Integer> resources = null;

    public RefrigeratorLayout(Context context) {
        super(context);
    }

    public RefrigeratorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefrigeratorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void editMaterialView() {
        if(!VISIBLE_MODE) {
            MaterialViewEditDialog materialAddDialog
                = new MaterialViewEditDialog(getContext(), R.style.MaterialDialogStyle, materialViewCreateListener, resources);
            materialAddDialog.show();
        }
        else {
            Toast.makeText(getContext(), "편집모드를 종료 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void editMaterialView(MaterialView materialView) {
        MaterialViewEditDialog materialAddDialog
            = new MaterialViewEditDialog(getContext(), R.style.MaterialDialogStyle, materialViewModifyListener, resources, materialView);
        materialAddDialog.show();
    }

    public void setResources(List<Integer> resources) {
        this.resources = resources;
    }

    @Override
    public void addView(View child) {
        if(!VISIBLE_MODE) {
            materialViews.add((MaterialView) child);
            super.addView(child);
        }
    }

    @Override
    public void removeView(View view) {
        materialViews.remove(view);
        super.removeView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                resetData();
                setData(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                setMaterialViewPosition(x, y);
                return true;
            case MotionEvent.ACTION_UP:
                resetData();
                return false;
        }
        return super.onTouchEvent(event);
    }

    private MaterialView getTouchMaterialView(int x, int y) {
        for(MaterialView target : materialViews) {
            if(target.getBounds().contains(x, y))
                return target;
        }
        return null;
    }

    private void switchMaterialViewVisibleState() {
        if(materialViews != null && !materialViews.isEmpty()) {
            for (MaterialView target : materialViews) {
                target.switchVisibleState(VISIBLE_MODE);
            }
        }
    }

    public void switchMaterialEditMode() {
        switchMaterialViewVisibleState();
        VISIBLE_MODE = !VISIBLE_MODE;
        if(VISIBLE_MODE) {
            Toast.makeText(getContext(), "편집모드로 전환 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "편집모드가 종료 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getVisibleMode() {
        return VISIBLE_MODE;
    }

    private void setData(int x, int y) {
        Log.i(TAG, "x : " + x);
        Log.i(TAG, "y : " + y);
        targetMaterialView = getTouchMaterialView(x, y);
        if (targetMaterialView != null) {
            Rect bounds = targetMaterialView.getBounds();
            Log.i(TAG, "left : " + bounds.left);
            Log.i(TAG, "top : " + bounds.top);
            offsetX = x - bounds.left;
            offsetY = y - bounds.top;
        }
    }

    private void resetData() {
        targetMaterialView = null;
        offsetX = 0;
        offsetY = 0;
    }


    private void setMaterialViewPosition(int x, int y) {
        if(targetMaterialView != null) {
            targetMaterialView.setPosition(x - offsetX, y - offsetY);
        }
    }



    private MaterialViewCreateListener materialViewCreateListener = new MaterialViewCreateListener() {
        @Override
        public void createView(Material material) {
            final MaterialView materialView = new MaterialView(getContext(), material);
            materialView.setPosition(200, 200);
            materialView.setClickable(false);
            materialView.setMaterialViewEditListener(new MaterialViewListener() {
                @Override
                public void removeView(final MaterialView materialView) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("식재료 삭제");
                    alertDialogBuilder
                        .setMessage("정말로 삭제 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("네",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                RefrigeratorLayout.this.removeView(materialView);
                            }
                        })
                        .setNegativeButton("아니요",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

                @Override
                public void editView(MaterialView materialView) {
                    editMaterialView(materialView);
                }
            });
            addView(materialView);
        }
    };

    private MaterialViewModifyListener materialViewModifyListener = new MaterialViewModifyListener() {
        @Override
        public void modifyView(MaterialView materialView) {
            materialView.insertDataViews();
        }
    };
}



















