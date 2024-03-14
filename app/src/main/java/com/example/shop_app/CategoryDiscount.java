package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.fragment.viewPagerAdapterCateDis;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CategoryDiscount extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private viewPagerAdapterCateDis mviewPagerAdapterCateDis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_discount);
        setControl();
        mviewPagerAdapterCateDis = new viewPagerAdapterCateDis(CategoryDiscount.this);
        viewPager.setAdapter(mviewPagerAdapterCateDis);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
        case 0:
            tab.setText("Category");
            break;
        case 1:
            tab.setText("Discount");
            break;
        case 2:
            tab.setText("Order Discount");
            break;
    }
        }).attach();
        setData();
        SetEvent();
    }

    private void setControl() {
        viewPager = findViewById(R.id.view_pager_category_discount);
        tabLayout = findViewById(R.id.tab_layout_category_discount);
    }

    private void setData() {
    }

    private void SetEvent() {
        
    }
}