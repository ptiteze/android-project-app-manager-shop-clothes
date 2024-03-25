package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {
    EditText search;
    ListView show;
    FloatingActionButton btn_add;
    ChildEventListener mChildEventListener;
    List<String> list_category = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        setControl();
        setData();
        setEvent();
    }
    private void setControl() {
        search = findViewById(R.id.category_search);
        show = findViewById(R.id.list_category);
        btn_add = findViewById(R.id.floatbtn_cate);
    }
    private void setData() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                category cate = new category();
                cate = snapshot.getValue(category.class);
                if(cate!=null){
                    list_category.add(cate.getName());
                    Toast.makeText(Category.this, cate.getName(), Toast.LENGTH_SHORT).show();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(Category.this, android.R.layout.simple_list_item_1,list_category);
                show.setAdapter(adapter);
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
        Query query = FirebaseDatabase.getInstance().getReference("category");
        query.addChildEventListener(mChildEventListener);
    }
    private void setEvent() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCategory = new Intent(Category.this, AddCategory.class);
                //add.putExtra("user_id", acc.getUser_id());
                startActivity(addCategory);
            }
        });
    }
}