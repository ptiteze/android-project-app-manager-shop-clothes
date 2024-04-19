package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.category;
import com.example.model.product;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddCategory extends AppCompatActivity {
    String thisCategory, keyThisCategory;
    boolean hasThisCategory = false, checkCate = false;
    ImageButton btn_back;
    TextView title;
    Chip btn_add, btn_cancel, btn_remove;
    TextView name, des;
    category catex;
    List<String> listTemp =  new ArrayList<>();
    List<String> list_name = new ArrayList<>();
    List<String> list_temp = new ArrayList<>();
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
            database.child("category").orderByChild("name").equalTo(thisCategory).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        keyThisCategory = dataSnapshot.getKey();
                        category cate = new category();
                        cate = dataSnapshot.getValue(category.class);
                        if (cate != null) {
                            catex = cate;
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
            database.child("category").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list_name.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        category cate = new category();
                        cate = dataSnapshot.getValue(category.class);
                        if (cate != null) {
                            list_name.add(cate.getName());
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
                name.setText(catex.getName());
                des.setText(catex.getDescrip());
//                database.child("category").orderByChild("name").equalTo(thisCategory);
//                database.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            category cate = new category();
//                            cate = dataSnapshot.getValue(category.class);
//                            if (cate != null) {
//                                name.setText(cate.getName());
//                                des.setText(cate.getDescrip());
//                            } else {
//                                Toast.makeText(AddCategory.this, "không tìm thấy loại sản phẩm này", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(AddCategory.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
        btn_add.setOnClickListener(view -> {
            if(hasThisCategory){
                if(check()){
                    catex.setName(name.getText().toString().trim());
                    catex.setDescrip(des.getText().toString().trim());
                    database.child("category").child(keyThisCategory).setValue(catex);
                    Query query = database.child("product").orderByChild("category").equalTo(thisCategory);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list_temp.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                product pr = dataSnapshot.getValue(product.class);
                                //if (pr.getCategory().equals(thisCategory))
                                    list_temp.add(pr.getId());
                            }
                            for (String id: list_temp) {
                                database.child("product/"+id+"/category").setValue(catex.getName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            error.toException();
                        }
                    });
                    Toast.makeText(AddCategory.this,"Sửa thành công",Toast.LENGTH_SHORT).show();
                }
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
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("product").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Set<String> setCateProduct = new HashSet<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            product pr = dataSnapshot.getValue(product.class);
                            if (pr != null) {
                                setCateProduct.add(pr.getCategory());
                            }
                        }
                        if(setCateProduct.contains(catex.getName())){
                            Toast.makeText(AddCategory.this,"Loại sản phẩm này đã được sử dụng, không thể xóa",Toast.LENGTH_SHORT).show();
                        }else{
                            database.child("category").child(keyThisCategory).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if(error==null){
                                        hasThisCategory=false;
                                        title.setText("ADD CATEGORY");
                                        btn_add.setText("Add Category");
                                        name.setText("");
                                        des.setText("");
                                        btn_remove.setEnabled(false);
                                        btn_remove.setVisibility(View.GONE);
                                        Toast.makeText(AddCategory.this,"Loại sản phẩm:"+catex.getName() + " đã được xóa",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(AddCategory.this,"Loại sản phẩm: "+catex.getName() + " chưa được xóa thành công",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddCategory.this,"Loại sản phẩm: "+catex.getName() + " chưa được xóa thành công",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void clearData() {
        if(hasThisCategory){
            name.setText(catex.getName());
            des.setText(catex.getDescrip());
        }else{
            name.setText("");
            des.setText("");
        }
    }

    private boolean check() {
        if(list_name.contains(name.getText().toString().trim())&&!hasThisCategory){
            name.setError("Tên loại sản phẩm bị trùng");
            name.requestFocus();
            return false;
        }
        if(hasThisCategory){
            listTemp =  new ArrayList<>(list_name);
            listTemp.remove(catex.getName());
            if(listTemp.contains(name.getText().toString().trim())){
                name.setError("Tên loại sản phẩm bị trùng");
                name.requestFocus();
                return false;
            }
            listTemp=null;
        }

        if(name.getText().toString().trim().isEmpty()){
            name.setError("Chưa nhâp tên loại sản phẩm");
            name.requestFocus();
            return false;
        }
        if(name.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            name.setError("Tên sản loại sản phâm Không được chứa kí tự đặc biệt");
            name.requestFocus();
            return false;
        }
        if(des.getText().toString().trim().isEmpty()){
            des.setError("Chưa nhâp thông tin loại sản phẩm");
            des.requestFocus();
            return false;
        }
        if(des.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
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