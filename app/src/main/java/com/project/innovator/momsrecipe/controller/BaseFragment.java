package com.project.innovator.momsrecipe.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

@SuppressLint("ValidFragment")
public class BaseFragment extends Fragment {

    protected int page;
    protected String title;

    public BaseFragment(int page, String title) {
        this.page = page;
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public String getTitle() {
        Log.i("BaseFragment", title);
        return title;
    }
}
