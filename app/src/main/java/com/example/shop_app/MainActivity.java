package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.account;
import com.example.model.product;
import com.example.model.size_stock;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    EditText username, password;
    Button btn_login;
    List<account> list_acc = new ArrayList<>();
    private boolean checkLogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setControl();
        setEvent();
//        product new_product = new product("SP0004","Áo thun nam xanh dương", "Áo thun nam", "vải cotton",
//                                    "Việt Nam", "Áo thun phong cách cho Nam","Xanh", 250000, 10,
//                                    "https://aothunnhatban.vn/wp-content/uploads/2017/09/cotton-nam-co-tron-mau-xanh-duong.jpg", true);
//        database.child("product").push().setValue(new_product);
//        Map<String, Integer> size = new TreeMap<>();
//        size.put("L",10);
//        size.put("M",5);
//        size.put("XL",5);
//        size_stock ss = new size_stock("SP0001",size);
//        database.child("productSize").child(ss.getProduct_id()).setValue(ss.getSize_stock());
    }

    private void setEvent() {
        database.child("account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                account acc = new account();
                acc = snapshot.getValue(account.class);
                list_acc.add(acc);
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
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    String usname = username.getText().toString().trim(), pass = password.getText().toString().trim();
                    if(!list_acc.isEmpty()){
                        for (account acc: list_acc) {
                            if(acc.getAcc().equals(usname) && acc.getPass().equals(pass)){
                                if(acc.isState() && (acc.getRule().equals("ADMIN")||acc.getRule().equals("NHANVIEN"))) {
                                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    Intent home = new Intent(MainActivity.this, Main_menu.class);
                                    home.putExtra("user_id", acc.getUser_id());
                                    checkLogin = true;
                                    startActivity(home);
                                    finish();
                                }
                            }
                        }
                    }
                    if(!checkLogin)
                        Toast.makeText(MainActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();}
                }

        });
    }

    private boolean check() {
            if(username.getText().toString().trim().isEmpty()){
                username.setError("Chưa nhâp UserName");
                username.requestFocus();
                return false;
            }
            if(password.getText().toString().trim().isEmpty()){
                password.setError("Chưa nhâp PassWord");
                password.requestFocus();
                return false;
            }
            if(username.getText().toString().trim().matches(".*[\\s\\W].*")){
                username.setError("Username Không được chứa kí tự đặc biệt");
                username.requestFocus();
                return false;
            }
            if(password.getText().toString().trim().matches(".*[\\s\\W].*")){
                password.setError("PassWord Không được chứa kí tự đặc biệt");
                password.requestFocus();
                return false;
            }
        return  true;
    }

    private void setControl() {
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_Password);
        btn_login = findViewById(R.id.btn_login);
    }

}