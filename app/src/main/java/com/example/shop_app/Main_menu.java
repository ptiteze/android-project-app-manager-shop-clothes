package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fragment.viewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayoutMediator;

public class Main_menu extends AppCompatActivity {
    private ViewPager2 viewPager;
    private String u_id = "";

    public String getU_id() {
        return u_id;
    }

    //    boolean isSwipingEnabled = false;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        setControl();
        viewPagerAdapter adapter= new viewPagerAdapter(Main_menu.this);
        viewPager.setAdapter(adapter);
        setEvent();

    }

    private void setEvent() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                switch (position){
//                    case 0:
//                        bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
//                        break;
//                    case 1:
//                        bottomNavigationView.getMenu().findItem(R.id.bottom_product).setChecked(true);
//                        break;
//                    case 2:
//                        bottomNavigationView.getMenu().findItem(R.id.bottom_order).setChecked(true);
//                        break;
//                    case 3:
//                        bottomNavigationView.getMenu().findItem(R.id.bottom_statistical).setChecked(true);
//                        break;
//                    case 4:
//                        bottomNavigationView.getMenu().findItem(R.id.bottom_profile).setChecked(true);
//                        break;
//                }
//            }
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.bottom_home){
                    viewPager.setCurrentItem(0);
                }
                if(item.getItemId() == R.id.bottom_product){
                    viewPager.setCurrentItem(1);
                }
                if(item.getItemId() == R.id.bottom_order){
                    viewPager.setCurrentItem(2);
                }
                if(item.getItemId() == R.id.bottom_statistical){
                    viewPager.setCurrentItem(3);
                }
                if(item.getItemId() == R.id.bottom_profile){
                    viewPager.setCurrentItem(4);
                }
                return true;
            }
        });


    }

    private void setControl() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("user_id")) {
            u_id = intent.getStringExtra("user_id");
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);

    }
//    bottomNavigationView.setOnTouchListener((v, event) -> true);
//
//        bottomNavigationView.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            isSwipingEnabled = !isSwipingEnabled;
//            viewPager2.setUserInputEnabled(isSwipingEnabled);
//        }
//    });

}