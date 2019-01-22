package com.project.innovator.momsrecipe.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.project.innovator.momsrecipe.FirebaseUtils;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.callback.RecipeAdapterListener;
import com.project.innovator.momsrecipe.adapter.RecipeAdapter;
import com.project.innovator.momsrecipe.models.Recipe;

import java.util.List;

@SuppressLint("ValidFragment")
public class RecipeFragment extends BaseFragment {

    public static Activity recipeFragmentActivity;
    private Button searchBtn;
    private EditText searchEdt;
    private RecyclerView rRecyclerView;
    private RecyclerView.LayoutManager rLayoutManager;
    private RecipeAdapter rAdapter;

    public RecipeFragment(int page, String title) {
        super(page, title);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, null);

        searchEdt = (EditText) view.findViewById(R.id.searchEdt);
        /*
        searchBtn = (Button)view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make searching later
            }
        });
        */

        recipeFragmentActivity = getActivity();

        rRecyclerView = view.findViewById(R.id.rRecyclerView);
        rRecyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rRecyclerView.setLayoutManager(rLayoutManager);

        rAdapter = new RecipeAdapter(recipeAdapterListener);
        rRecyclerView.setAdapter(rAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUtils.getAllRecipe(user.getUid(), new FirebaseUtils.RecipeCallback() {
            @Override
            public void getRecipeDataCallback(List<Recipe> recipes) {
                if (recipes != null) {
                    rAdapter.setRecipes(recipes);
                }
            }

            @Override
            public void cancelledCallback(DatabaseError error) {

            }
        });
        return view;
    }

    private RecipeAdapterListener recipeAdapterListener = new RecipeAdapterListener() {
        @Override
        public void addRecipeItem() {
            Intent intent = new Intent(getActivity(), RecipeInputActivity.class);
            startActivity(intent);
        }

        @Override
        public void modifyRecipeItem(Recipe recipe) {

        }

        @Override
        public void removeRecipeItem(Recipe recipe) {
        }

        @Override
        public void clickRecipeItem(Recipe recipe) {

        }
    };
}


