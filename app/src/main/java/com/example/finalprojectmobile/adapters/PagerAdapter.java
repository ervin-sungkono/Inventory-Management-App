package com.example.finalprojectmobile.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalprojectmobile.models.Page;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStateAdapter {
    private ArrayList<Page> pagesArrayList;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Page> pagesArrayList) {
        super(fragmentActivity);
        this.pagesArrayList = pagesArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return pagesArrayList.get(position).getFragment();
    }

    @Override
    public int getItemCount() {
        return pagesArrayList.size();
    }
}
