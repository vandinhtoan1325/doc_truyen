package com.example.doc_truyen.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doc_truyen.CategoryFragment;
import com.example.doc_truyen.HomeFragment;
import com.example.doc_truyen.SearchFragment;
import com.example.doc_truyen.SeeMoreFragment;
import com.example.doc_truyen.UseFragment;

public class MyViewPager extends FragmentStateAdapter {

    public MyViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SeeMoreFragment();
            case 2:
                return new CategoryFragment();
            case 3:
                return new SearchFragment();
            case 4:
                return new UseFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
