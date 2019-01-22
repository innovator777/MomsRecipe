package com.project.innovator.momsrecipe.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.views.RefrigeratorLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("ValidFragment")
public class RefrigeratorFragment extends BaseFragment{

    private List<Integer> resources;

    private RefrigeratorLayout refrigeratorLayout;
    private FloatingActionButton floatingMainButton,
                                 floatingAddButton,
                                 floatingEditButton;

    public RefrigeratorFragment(int page, String title) {
        super(page, title);
        resources = new ArrayList<>(Arrays.asList(R.drawable.searchbtn, R.drawable.searchbtn,
            R.drawable.searchbtn, R.drawable.searchbtn, R.drawable.searchbtn, R.drawable.searchbtn, R.drawable.searchbtn, R.drawable.searchbtn));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refrigerator, null);

        refrigeratorLayout = view.findViewById(R.id.refrigerator);
        refrigeratorLayout.setResources(resources);
        floatingMainButton = view.findViewById(R.id.fab);
        floatingAddButton = view.findViewById(R.id.material_add);
        floatingAddButton.setOnClickListener(addButtonClickListener);
        floatingEditButton = view.findViewById(R.id.material_edit);
        floatingEditButton.setOnClickListener(editButtonClickListener);
        return view;
    }


    //RefrigeratorLayout 에서 추가와 삭제를 하는 구조가 더 깔끔할것 같음
    private View.OnClickListener addButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            refrigeratorLayout.editMaterialView();
        }
    };

    private View.OnClickListener editButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            refrigeratorLayout.switchMaterialEditMode();
        }
    };

}
