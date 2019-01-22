package com.project.innovator.momsrecipe.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.innovator.momsrecipe.controller.BaseFragment;
import com.project.innovator.momsrecipe.controller.MyPageFragment;
import com.project.innovator.momsrecipe.controller.RecipeFragment;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<BaseFragment> fragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addFragment(BaseFragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragmentList.get(position);
            case 1:
                return fragmentList.get(position);
            case 2:
                return fragmentList.get(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getTitle();
    }
}
