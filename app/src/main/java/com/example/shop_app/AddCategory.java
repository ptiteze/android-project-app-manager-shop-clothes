package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.category;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCategory extends AppCompatActivity {
    String thisCategory;
    boolean hasThisCategory = false, checkCate = false;
    ImageButton btn_back;
    TextView title;
    Chip btn_add, btn_cancel, btn_remove;
    TextView name, des;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);
        setControl();
        setData();
        setEvent();
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("thisCategory")) {
            thisCategory = intent.getStringExtra("thisCategory");
            hasThisCategory = true;
        }
        if(hasThisCategory){
           title.setText("MODIFY CATEGORY");
            btn_add.setText("Complete");
            btn_remove.setEnabled(true);
            btn_remove.setVisibility(View.VISIBLE);
            database.child("category").orderByChild("name").equalTo(thisCategory);
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        category cate = new category();
                        cate = dataSnapshot.getValue(category.class);
                        if (cate != null) {
                            name.setText(cate.getName());
                            des.setText(cate.getDescrip());
                        } else {
                            Toast.makeText(AddCategory.this, "không tìm thấy loại sản phẩm này", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddCategory.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setEvent() {
        btn_back.setOnClickListener(view -> finish());
        btn_cancel.setOnClickListener(view -> {
            if(!hasThisCategory){
                clearData();
            }else{
                database.child("category").orderByChild("name").equalTo(thisCategory);
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            category cate = new category();
                            cate = dataSnapshot.getValue(category.class);
                            if (cate != null) {
                                name.setText(cate.getName());
                                des.setText(cate.getDescrip());
                            } else {
                                Toast.makeText(AddCategory.this, "không tìm thấy loại sản phẩm này", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddCategory.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_add.setOnClickListener(view -> {
            if(hasThisCategory){
                Toast.makeText(AddCategory.this,"Sửa thành công",Toast.LENGTH_SHORT).show();
            }else {
                if(check()){
                    category cate = new category(name.getText().toString(), des.getText().toString());
                    if(!checkCate){
                        database.child("category").push().setValue(cate);
                        Toast.makeText(AddCategory.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                        clearData();
                    }else{
                        Toast.makeText(AddCategory.this,"Trùng tên loại sản phẩm",Toast.LENGTH_SHORT).show();
                        name.requestFocus();
                    }
                }
            }

        });
    }

    private void clearData() {
        name.setText("");
        des.setText("");
    }

    private boolean check() {
        if(name.getText().toString().trim().isEmpty()){
            name.setError("Chưa nhâp tên loại sản phẩm");
            name.requestFocus();
            return false;
        }
        if(name.getText().toString().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵđ].*")){
            name.setError("Tên sản loại sản phâm Không được chứa kí tự đặc biệt");
            name.requestFocus();
            return false;
        }
        if(des.getText().toString().trim().isEmpty()){
            des.setError("Chưa nhâp thông tin loại sản phẩm");
            des.requestFocus();
            return false;
        }
        if(des.getText().toString().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵđ].*")){
            des.setError("Thông tin loại sản phẩm Không được chứa kí tự đặc biệt");
            des.requestFocus();
            return false;
        }
        return true;
    }
    private void checkCate(){
        database.child("category");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    category cate = new category();
                    cate = dataSnapshot.getValue(category.class);
                    if(cate!=null){
                        if (cate.getName().equals(name.getText().toString().trim())){
                            checkCate = true;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddCategory.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setControl() {
        title = findViewById(R.id.addcategory_title);
        btn_add = findViewById(R.id.addcategory_complete);
        btn_cancel = findViewById(R.id.addcategory_cancel);
        btn_remove = findViewById(R.id.addcategory_remove);
        btn_back = findViewById(R.id.addCategory_back);
        name = findViewById(R.id.addCategory_name);
        des = findViewById(R.id.addCategory_description);
    }

}