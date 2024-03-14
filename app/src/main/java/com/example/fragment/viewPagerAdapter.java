package com.example.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapter extends FragmentStateAdapter {
    public viewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    //    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return new HomeFragment();
//            case 1:
//                return new ProductFragment();
//            case 2:
//                return new OrderFragment();
//            case 3:
//                return new stactisticalFragment();
//            case 4:
//                return new ProfileFragment();
//            default:
//                return new HomeFragment();
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return 5;
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new ProductFragment();
            case 2:
                return new OrderFragment();
            case 3:
                return new stactisticalFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
