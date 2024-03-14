package com.example.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapterCateDis extends FragmentStateAdapter {

    public viewPagerAdapterCateDis(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//
//    }

//    @Override
//    public int getCount() {
//        return 3;
//    }
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position){
//        String title = "";
//        switch (position) {
//            case 0:
//                title = "Category";
//                break;
//            case 1:
//                title = "Discount";
//                break;
//            case 2:
//                title = "Discount Order";
//                break;
//        }
//        return title;
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CategoryFragment();
            case 1:
                return new DiscountFragment();
            case 2:
                return new TotalDiscountFragment();
            default:
                return new CategoryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
