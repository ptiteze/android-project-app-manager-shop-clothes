package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.model.product;
import com.example.shop_app.AddProduct;
import com.example.shop_app.CategoryDiscount;
import com.example.shop_app.Main_menu;
import com.example.shop_app.R;
import com.example.shop_app.productAdapter;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    List<product> list_product = new ArrayList<>();
    ImageButton btn_back, btn_cate_discount, btn_add;
    EditText search;
    RecyclerView show;
    ChildEventListener mChildEventListener;
    Chip chip_product, chip_discount, chip_category;
    private productAdapter adapter;
    private Main_menu mainMenu;
    String u_id;
    View view;
    static long product_count = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product, container, false);
        setControl();
        setData();
        //Log.d("main check", list.size()+" ");
        setEvent();
        Log.d("product_count in main  " + String.valueOf(list_product.size()), String.valueOf(product_count));
        return view;
    }

    private void showData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        adapter = new productAdapter(list_product,view.getContext());
        show.setAdapter(adapter);
        show.setLayoutManager(linearLayoutManager);
        //Log.d("find", String.valueOf(list_product.size()));
    }

    private void setData() {
        Query query_prodcut_all = FirebaseDatabase.getInstance().getReference("product");
//        database.child("product").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                product_count = snapshot.getChildrenCount();
//                Log.d("product_count  ", String.valueOf(product_count));
//                product new_product = new product();
//                new_product = snapshot.getValue(product.class);
//                if (new_product != null) {
//                    list_product.add(new_product);
//                    Log.d("data  " + String.valueOf(list_product.size()), new_product.toString());
//                }
//                showData();
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                product_count = snapshot.getChildrenCount();
                Log.d("product_count  ", String.valueOf(product_count));
                product new_product = new product();
                new_product = snapshot.getValue(product.class);
                if (new_product != null) {
                    list_product.add(new_product);
                    Log.d("data  " + String.valueOf(list_product.size()), new_product.toString());
                }
                showData();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query_prodcut_all.addChildEventListener(mChildEventListener);
    }

    private void setEvent() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProduct = new Intent(view.getContext(), AddProduct.class);
                startActivity(addProduct);
            }
        });
        btn_cate_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryDiscount = new Intent(view.getContext(), CategoryDiscount.class);
                categoryDiscount.putExtra("user_id", u_id);
                startActivity(categoryDiscount);
            }
        });
    }

    private void setControl() {
        mainMenu = (Main_menu) getActivity();
        if(mainMenu!=null){
            u_id = mainMenu.getU_id();
        }
        search = view.findViewById(R.id.product_search);
        show = view.findViewById(R.id.product_show);
        btn_add = view.findViewById(R.id.product_add);
        btn_cate_discount = view.findViewById(R.id.product_cate_discount);
//        chip_product = view.findViewById(R.id.product_chip_sp);
//        chip_discount = view.findViewById(R.id.product_chip_discount);
//        chip_category = view.findViewById(R.id.product_chip_cate);
    }
}