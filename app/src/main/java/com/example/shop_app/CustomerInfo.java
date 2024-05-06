package com.example.shop_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.model.user;
import com.google.android.material.chip.Chip;

public class CustomerInfo extends AppCompatActivity {
    user us;
    ImageButton back;
    ImageView img;
    Chip showList;
    TextView name, birth, address, email, phone, sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_info);
        setControl();
        setData();
        setEvent();
    }

    private void setEvent() {
        back.setOnClickListener(view -> finish());
        showList.setOnClickListener(view -> {
            Intent intent = new Intent(this,ListCustomerOrder.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",us);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setControl() {
        back = findViewById(R.id.customerInfo_back);
        img = findViewById(R.id.customerInfo_picture);
        showList = findViewById(R.id.customerInfo_chip);
        name = findViewById(R.id.customerInfo_name);
        birth = findViewById(R.id.customerInfo_birth);
        address = findViewById(R.id.customerInfo_address);
        email = findViewById(R.id.customerInfo_Email);
        phone = findViewById(R.id.customerInfo_phone);
        sex = findViewById(R.id.customerInfo_sex);
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;
        us = (user) bundle.get("user");
        name.setText(us.getName());
        birth.setText(us.getBirth());
        Glide.with(this).load(us.getAvata()).into(img);
        address.setText(us.getAddress());
        email.setText(us.getEmail());
        phone.setText(us.getPhone());
        sex.setText(us.getSex());
    }
}