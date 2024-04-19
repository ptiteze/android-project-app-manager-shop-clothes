package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.model.product;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportProduct extends AppCompatActivity {
    Chip confirm;
    ImageButton back;
    TextView name, des;
    ImageView img;
    EditText txtM, txtL, txtXL, txtXXL;
    product pr;
    Map<String, Integer> size_stock = new HashMap<>();
    List<String> list_size = new ArrayList<>();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_product);
        setControl();
        setData();
        setEvent();
    }

    private void setControl() {
        back = findViewById(R.id.importPr_back);
        img  = findViewById(R.id.importPr_picture);
        confirm = findViewById(R.id.importPr_confirm);
        name = findViewById(R.id.importPr_name);
        des = findViewById(R.id.importPr_des);
        txtM = findViewById(R.id.importPr_M);
        txtL = findViewById(R.id.importPr_L);
        txtXL = findViewById(R.id.importPr_XL);
        txtXXL = findViewById(R.id.importPr_XXL);
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;
        pr =  (product) bundle.get("product");
        assert pr != null;
        name.setText(pr.getName());
        Glide.with(ImportProduct.this).load(pr.getImage()).into(img);
        des.setText(pr.getDescription());
        database.child("productSize").child(pr.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    list_size.add(key);
                }
                enebleSize();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImportProduct.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();

            }
        });
        database.child("importProduct").child(pr.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        int value = dataSnapshot.getValue(Integer.class);
                        size_stock.put(key,value);
                    }
                    setBeforeValue();
                }else{
                    //Toast.makeText(ImportProduct.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImportProduct.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBeforeValue() {
        for (String s: size_stock.keySet()) {
            switch (s){
                case "M":
                    txtM.setEnabled(true);
                    txtM.setText(String.valueOf(size_stock.get("M")));
                    break;
                case "L":
                    txtL.setEnabled(true);
                    txtL.setText(String.valueOf(size_stock.get("L")));
                    break;
                case "XL":
                    txtXL.setEnabled(true);
                    txtXL.setText(String.valueOf(size_stock.get("XL")));
                    break;
                case "XXL":
                    txtXXL.setEnabled(true);
                    txtXXL.setText(String.valueOf(size_stock.get("XXL")));
                    break;
            }
        }

    }

    private void enebleSize() {
        for (String s: list_size) {
            switch (s){
                case "M":
                    txtM.setEnabled(true);
                    txtM.setText("0");
                    break;
                case "L":
                    txtL.setEnabled(true);
                    txtL.setText("0");
                    break;
                case "XL":
                    txtXL.setEnabled(true);
                    txtXL.setText("0");
                    break;
                case "XXL":
                    txtXXL.setEnabled(true);
                    txtXXL.setText("0");
                    break;
            }
        }
    }

    private void setEvent() {
    back.setOnClickListener(view -> finish());
    confirm.setOnClickListener(view -> {
      if(check())  {
          for (String k: list_size) {
              switch (k){
                  case "M":
                      size_stock.put("M", Integer.parseInt(txtM.getText().toString()));
                      break;
                  case "L":
                      size_stock.put("L", Integer.parseInt(txtL.getText().toString()));
                      break;
                  case "XL":
                      size_stock.put("XL", Integer.parseInt(txtXL.getText().toString()));
                      break;
                  case "XXL":
                      size_stock.put("XXL", Integer.parseInt(txtXXL.getText().toString()));
                      break;
              }
          }
          database.child("importProduct").child(pr.getId()).setValue(size_stock);
          Toast.makeText(this,"Thêm sản phẩm: "+ pr.getName()+ " vào đơn nhập thành công", Toast.LENGTH_SHORT).show();
          back.requestFocus();
      }
    });
    }

    private boolean check() {
        String M = txtM.getText().toString();
        String L = txtL.getText().toString();
        String XL = txtXL.getText().toString();
        String XXL = txtXXL.getText().toString();
        if(txtM.isEnabled()&&(M.isEmpty()||M.contains(".")||M.contains(","))){
            txtM.setError("Nhập liệu không hợp lệ");
            txtM.requestFocus();
            return false;
        }
        if(txtL.isEnabled()&&(L.isEmpty()||L.contains(".")||L.contains(","))){
            txtL.setError("Nhập liệu không hợp lệ");
            txtL.requestFocus();
            return false;
        }
        if(txtXL.isEnabled()&&(XL.isEmpty()||XL.contains(".")||XL.contains(","))){
            txtXL.setError("Nhập liệu không hợp lệ");
            txtXL.requestFocus();
            return false;
        }
        if(txtXXL.isEnabled()&&(XXL.isEmpty()||XXL.contains(".")||XXL.contains(","))){
            txtXXL.setError("Nhập liệu không hợp lệ");
            txtXXL.requestFocus();
            return false;
        }
        if(Sum(M,L,XL,XXL)==0){
            Toast.makeText(this,"Tổng lượng nhập không thể bằng 0", Toast.LENGTH_SHORT).show();
            confirm.requestFocus();
            return false;
        }
        return true;
    }

    private int Sum(String m, String l, String xl, String xxl) {
        int sm = 0, sl=0, sxl=0, sxxl=0;
        if(txtM.isEnabled()) sm = Integer.parseInt(m);
        if(txtL.isEnabled()) sl = Integer.parseInt(l);
        if(txtXL.isEnabled()) sxl = Integer.parseInt(xl);
        if(txtXXL.isEnabled()) sxxl = Integer.parseInt(xxl);
        return (sm+sl+sxl+sxxl);
    }
}