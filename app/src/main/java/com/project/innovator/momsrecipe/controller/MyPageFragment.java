package com.project.innovator.momsrecipe.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.project.innovator.momsrecipe.FirebaseUtils;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.callback.StandardAdapterListener;
import com.project.innovator.momsrecipe.adapter.StandardAdapter;
import com.project.innovator.momsrecipe.models.Standard;

import java.util.List;

@SuppressLint("ValidFragment")
public class MyPageFragment extends BaseFragment {
    private StandardAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private Button logoutButton;
    private TextView userIdText;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private RecyclerView.LayoutManager mLayoutManager;

    public MyPageFragment(int page, String title) {
        super(page, title);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, null);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        logoutButton = view.findViewById(R.id.logout_Button);
        userIdText = view.findViewById(R.id.userIdText);

        userIdText.setText(user.getEmail());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        recyclerView = view.findViewById(R.id.mypage_recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerAdapter = new StandardAdapter(getContext(), standardAdapterListener);
        recyclerView.setAdapter(recyclerAdapter);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUtils.getAllStandard(user.getUid(), new FirebaseUtils.StandardCallback() {
            @Override
            public void getStandardDataCallback(List<Standard> standards) {
                recyclerAdapter.setStandards(standards);
            }

            @Override
            public void cancelledCallback(DatabaseError error) {

            }
        });
        return view;
    }

    private StandardAdapterListener standardAdapterListener = new StandardAdapterListener() {
        @Override
        public void addStandard() {
            Intent intent = new Intent(getActivity(), StandardUpdateActivity.class);
            startActivity(intent);
        }

        @Override
        public void removeStandard(Standard standard) {

        }
    };
}
