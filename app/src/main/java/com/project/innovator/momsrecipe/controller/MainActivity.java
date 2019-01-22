package com.project.innovator.momsrecipe.controller;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private final long FINISH_INTERVAL_TIME=2000;
    private long backPressedTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);
        adapter.addFragment(new RecipeFragment(0, "Recipe"));
        adapter.addFragment(new RefrigeratorFragment(1, "Refrigerator"));
        adapter.addFragment(new MyPageFragment(2, "MyPage"));
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed(){
        long tempTime =System.currentTimeMillis();
        long intervalTime = tempTime-backPressedTime;

        if(0<=intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            super.onBackPressed();
        }
        else {
            backPressedTime=tempTime;
            Toast.makeText(this,"한번 더 뒤로가기를 누르면 꺼집니다.", Toast.LENGTH_SHORT).show();
        }

    }
}