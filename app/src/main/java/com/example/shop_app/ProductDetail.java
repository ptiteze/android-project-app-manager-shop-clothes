package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.model.product;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {
    ImageButton btn_back;
    ImageView img;
    TextView name, des, cate, color, material, origin, price, state
            , stockM, stockL, stockXL, stockXXL;
    Chip chip_fix, chipM, chipL, chipXL, chipXXL;
    product pr;
    Map<String, Integer> size = new HashMap<>();
    //ValueEventListener mvalueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        setControl();
        setData();
        setEvent();
    }

    private void setControl() {
        btn_back = findViewById(R.id.productDetail_back);
        img = findViewById(R.id.productDetail_picture);
        name = findViewById(R.id.productDetail_name);
        des = findViewById(R.id.productDetail_des);
        cate = findViewById(R.id.productDetail_category);
        color = findViewById(R.id.productDetail_color);
        material = findViewById(R.id.productDetail_material);
        origin = findViewById(R.id.productDetail_origin);
        price = findViewById(R.id.productDetail_price);
        chip_fix = findViewById(R.id.productDetail_complete);
        chipM = findViewById(R.id.productDetail_M);
        chipL = findViewById(R.id.productDetail_L);
        chipXL = findViewById(R.id.productDetail_XL);
        chipXXL = findViewById(R.id.productDetail_XXL);
        state = findViewById(R.id.productDetail_state);
        stockM = findViewById(R.id.prDetail_stockM);
        stockL = findViewById(R.id.prDetail_stockL);
        stockXL = findViewById(R.id.prDetail_stockXL);
        stockXXL = findViewById(R.id.prDetail_stockXXL);
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
//        Intent intent = getIntent();
//        if(intent != null && intent.hasExtra("product")) {
//            pr_id = intent.getStringExtra("product");
//            Query query =  FirebaseDatabase.getInstance().getReference("product/"+pr_id);
//            query.addValueEventListener(mvalueEventListener);
//        }

        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;
        pr =  (product) bundle.get("product");
        assert pr != null;
        name.setText(pr.getName());
        Glide.with(ProductDetail.this).load(pr.getImage()).into(img);
        des.setText(pr.getDescription());
        cate.setText(pr.getCategory());
        color.setText(pr.getColor());
        material.setText(pr.getMaterial());
        origin.setText(pr.getOrigin());
        price.setText(String.valueOf(pr.getPrice()));
        if(pr.isState()){
            state.setText("      State:  Đang bán");
        }else {
            state.setText("      State:  Đang khóa");
        }
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("productSize/"+pr.getId());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    int value = childSnapshot.getValue(Integer.class);
                    size.put(key,value);
                }
                setSize();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSize() {
        for (String s: size.keySet()) {
            switch (s){
                case "M":
                    chipM.setChecked(true);
                    stockM.setText(String.valueOf(size.get("M")));
                    break;
                case "L":
                    chipL.setChecked(true);
                    stockL.setText(String.valueOf(size.get("L")));
                    break;
                case "XL":
                    chipXL.setChecked(true);
                    stockXL.setText(String.valueOf(size.get("XL")));
                    break;
                case "XXL":
                    chipXXL.setChecked(true);
                    stockXXL.setText(String.valueOf(size.get("XXL")));
                    break;
            }
        }
    }

    private void setEvent() {
        btn_back.setOnClickListener(view -> finish());
        chip_fix.setOnClickListener(view -> {
            Intent detail = new Intent(ProductDetail.this,AddProduct.class);
            Bundle bundle = new Bundle();
            //Log.d("product", productTemp.toString());
            bundle.putSerializable("product", pr);
            detail.putExtras(bundle);
            startActivity(detail);
        });
    }
}