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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.model.category;
import com.example.model.product;
import com.example.shop_app.AddProduct;
import com.example.shop_app.Category;
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
    List<String> list_category = new ArrayList<>();
    ImageButton btn_back, btn_add;
    Chip btn_category;
    EditText search;
    RecyclerView show;
    ChildEventListener mChildEventListener;
    Spinner spinner_cate;
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
        database.child("category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                category cate = new category();
                cate = snapshot.getValue(category.class);
                if(cate!=null){
                    list_category.add(cate.getName());
                }
                showDataCate();
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
        });
//        Query query_prodcut_all = FirebaseDatabase.getInstance().getReference("product");
////        Query query_prodcut_cate = FirebaseDatabase.getInstance().getReference("product")
////                .orderByChild("category")
////                .equalTo(spinner_cate.getSelectedItem().toString());
//        mChildEventListener = new ChildEventListener() {
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
//
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
//        };
//        query_prodcut_all.addChildEventListener(mChildEventListener);
    }

    private void showDataCate() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, list_category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cate.setAdapter(adapter);
    }

    private void setEvent() {
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
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProduct = new Intent(view.getContext(), AddProduct.class);
                startActivity(addProduct);
            }
        });
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent category = new Intent(view.getContext(), Category.class);
                category.putExtra("user_id", u_id);
                startActivity(category);
            }
        });
        spinner_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!list_product.isEmpty()){
                    list_product.clear();
                }
                if(spinner_cate.getSelectedItem().toString().equals("Tất cả")){
                    Query query_prodcut_all = FirebaseDatabase.getInstance().getReference("product");
                    query_prodcut_all.addChildEventListener(mChildEventListener);
                }else{
                    Query query_prodcut_cate = FirebaseDatabase.getInstance().getReference("product")
                        .orderByChild("category")
                        .equalTo(spinner_cate.getSelectedItem().toString());
                    query_prodcut_cate.addChildEventListener(mChildEventListener);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH
//                    || actionId == EditorInfo.IME_ACTION_DONE
//                    || event.getAction() == KeyEvent.ACTION_DOWN
//                    || event.getAction() == KeyEvent.KEYCODE_ENTER ){
//                    String search_text = search.getText().toString();
//                    Query query_search = FirebaseDatabase.getInstance().getReference("products")
//                            .orderByChild("name")
//                            .startAt(search_text)
//                            .endAt(search_text+"\uf8ff");
//                    query_search.addChildEventListener(mChildEventListener);
//                }
//                return false;
//            }
//        });
    }

    private void setControl() {
        list_category.add("Tất cả");
        mainMenu = (Main_menu) getActivity();
        if(mainMenu!=null){
            u_id = mainMenu.getU_id();
        }
        search = view.findViewById(R.id.product_search);
        show = view.findViewById(R.id.product_show);
        btn_add = view.findViewById(R.id.product_add);
        btn_category = view.findViewById(R.id.product_cate);
        spinner_cate = view.findViewById(R.id.product_spin);
    }
}