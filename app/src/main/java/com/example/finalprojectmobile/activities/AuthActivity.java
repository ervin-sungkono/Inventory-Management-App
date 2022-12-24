package com.example.finalprojectmobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.adapters.PagerAdapter;
import com.example.finalprojectmobile.fragments.LoginFragment;
import com.example.finalprojectmobile.fragments.RegisterFragment;
import com.example.finalprojectmobile.models.Page;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class AuthActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    PagerAdapter pagerAdapter;
    ArrayList<Page> pagesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        viewPager2 = findViewById(R.id.view_pager2);
        tabLayout = findViewById(R.id.tab_layout);

        pagesArrayList = new ArrayList<>();
        pagesArrayList.add(new Page(new LoginFragment(), "Login"));
        pagesArrayList.add(new Page(new RegisterFragment(), "Register"));

        setViewPager2(viewPager2);
        new TabLayoutMediator(tabLayout, viewPager2,
                ((tab, position) -> tab.setText(pagesArrayList.get(position).getTitle()))
        ).attach();
    }

    public void setViewPager2(ViewPager2 viewPager2){
        if(pagerAdapter == null){
            pagerAdapter = new PagerAdapter(this, pagesArrayList);
        }
        viewPager2.setAdapter(pagerAdapter);
    }
}