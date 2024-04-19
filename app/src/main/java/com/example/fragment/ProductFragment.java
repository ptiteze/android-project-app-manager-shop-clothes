package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.model.category;
import com.example.model.product;
import com.example.shop_app.AddProduct;
import com.example.shop_app.Category;
import com.example.shop_app.Discount;
import com.example.shop_app.DiscountOnOrder;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ProductFragment extends Fragment {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    List<product> list_product = new ArrayList<>();
    List<product> list_product_cate = new ArrayList<>();
    Map<String, String> list_category = new HashMap<>(); // key, name
    ImageButton btn_back, btn_add;
    Chip btn_category,btn_discountOnOrder, btn_discountOnProduct;
    SearchView search;
    RecyclerView show;
    ChildEventListener mChildEventListener;
    Spinner spinner_cate;
    private productAdapter adapter;
    private Main_menu mainMenu;
    String u_id;
    View view;
    ArrayAdapter<String> adaptercate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product, container, false);
        setControl();
        setData();
        setEvent();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showData(List<product> listAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        adapter = new productAdapter(listAdapter,view.getContext());
        adapter.notifyDataSetChanged();
        show.setAdapter(adapter);
        show.setLayoutManager(linearLayoutManager);
        //Log.d("find", String.valueOf(list_product.size()));
    }

    private void setData() {
        database.child("category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                category cate = snapshot.getValue(category.class);
                String key = snapshot.getKey();
                if(cate!=null){
                    list_category.put(key,cate.getName());
                    showDataCate(list_category);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                category update_category = snapshot.getValue(category.class);
                String key = snapshot.getKey();
                if(update_category!=null){
                    for (String k: list_category.keySet()) {
                        if (k.equals(key)){
                            list_category.put(k,update_category.getName());
                            showDataCate(list_category);
                            break;
                        }
                    }
//                    for (int i = 0; i < list_category.size(); i++) {
//                        if(list_category.get(i).equals(update_category.getName())){
//                            list_category.set(i,update_category.getName());
//
//                        }
//                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String removedKey = snapshot.getKey();
                // Xóa phần tử tương ứng từ list_category
                list_category.remove(removedKey);
                showDataCate(list_category);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query_prodcut_all = FirebaseDatabase.getInstance().getReference("product");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                product new_product = new product();
                new_product = snapshot.getValue(product.class);
                if (new_product != null) {
                    list_product.add(new_product);
                    //Log.d("data  " + String.valueOf(list_product.size()), new_product.toString());
                    showData(list_product);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                product update_product = snapshot.getValue(product.class);
                if(update_product!=null){
                    for (int i = 0; i < list_product.size(); i++) {
                        if (list_product.get(i).getId().equals(update_product.getId())) {
                            list_product.set(i, update_product);
                            showData(list_product);
                            break;
                        }
                    }
                }
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

    private void showDataCate(Map<String, String> list_cate) {
        List<String> list = new ArrayList<>(list_cate.values());
        adaptercate = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,list);
        adaptercate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptercate.notifyDataSetChanged();
        spinner_cate.setAdapter(adaptercate);
    }

    private void setEvent() {
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
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                search.setQuery("",false);
                String cate = spinner_cate.getSelectedItem().toString();
                list_product_cate.clear();
                if(cate.equals("Tất cả")){
                    //Log.d("spinner 1", "onItemClick: "+ spinner_cate.getSelectedItem().toString());
                    showData(list_product);
                }else{
                    for (product pr: list_product) {
                        if(pr.getCategory().equals(cate)){
                            list_product_cate.add(pr);
                        }
                    }
                    showData(list_product_cate);
                    //Log.d("spinner 2", "onItemClick: "+ spinner_cate.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("spinner 1", "onItemClick: "+ spinner_cate.getSelectedItem().toString());
            }
        });
        btn_discountOnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent discountOnProduct = new Intent(view.getContext(), Discount.class);
                //discountOnProduct.putExtra("user_id", u_id);
                startActivity(discountOnProduct);
            }
        });
        btn_discountOnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent discountOnOrder = new Intent(view.getContext(), DiscountOnOrder.class);
                //discountOnProduct.putExtra("user_id", u_id);
                startActivity(discountOnOrder);
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setControl() {
        list_category.put("1","Tất cả");
        mainMenu = (Main_menu) getActivity();
        if(mainMenu!=null){
            u_id = mainMenu.getU_id();
        }
        search = view.findViewById(R.id.product_search);
        show = view.findViewById(R.id.product_show);
        btn_add = view.findViewById(R.id.product_add);
        btn_category = view.findViewById(R.id.product_cate);
        spinner_cate = view.findViewById(R.id.product_spin);
        btn_discountOnProduct = view.findViewById(R.id.product_chip_discount);
        btn_discountOnOrder = view.findViewById(R.id.product_discountOnOrder);

    }
}