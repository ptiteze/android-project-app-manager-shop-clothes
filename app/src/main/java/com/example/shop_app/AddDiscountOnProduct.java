package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.product;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDiscountOnProduct extends AppCompatActivity {
    TextView title, tStart, tEnd;
    Chip btn_add, btn_cancel, btn_remove;
    ImageButton btn_back, btn_tStart, btn_tEnd;
    EditText name, des, percent;
    ListView listProduct, listProductDiscont;
    ChildEventListener mChildEventListener;
    Map<String, String> list_product = new HashMap<>();// id, name
    Map<String, String> list_product_discount = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discount_on_product);
        setControl();
        setData();
        setEvent();
    }

    private void setData() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                product pr = new product();
                pr = snapshot.getValue(product.class);
                if(pr!=null){
                    list_product.put(pr.getId(),pr.getName());
                }
                showDataProduct();
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
        Query query = FirebaseDatabase.getInstance().getReference("product");
        query.addChildEventListener(mChildEventListener);
    }

    private void showDataProduct() {
        List<String> list_pr = new ArrayList<>(list_product.values());
        ArrayAdapter<String> adapter = new ArrayAdapter(AddDiscountOnProduct.this, android.R.layout.simple_list_item_1,list_pr);
        listProduct.setAdapter(adapter);
    }

    private void setControl() {
        name = findViewById(R.id.addDiscount_name);
        des = findViewById(R.id.addDiscount_description);
        title = findViewById(R.id.addDiscount_title);
        btn_add = findViewById(R.id.addDiscount_complete);
        btn_cancel = findViewById(R.id.addDiscount_cancel);
        btn_remove = findViewById(R.id.addDiscount_remove);
        percent = findViewById(R.id.addDiscount_percent);
        tStart = findViewById(R.id.addDiscount_timeStart);
        tEnd = findViewById(R.id.addDiscount_timeEnd);
        btn_tStart = findViewById(R.id.timeStart_btn);
        btn_tEnd = findViewById(R.id.timeEnd_btn);
        btn_back = findViewById(R.id.addDiscount_back);
        listProduct = findViewById(R.id.addDiscount_listProduct);
        listProductDiscont = findViewById(R.id.addDiscount_productDiscount);
    }

    private void setEvent() {
        btn_back.setOnClickListener(view -> finish());
        btn_tStart.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiscountOnProduct.this, (datePicker, year12, month12, dayOfMonth) -> {
                tStart.setText(String.format("%d/%d/%d", dayOfMonth, (month12 +1), year12));
                //Log.d("Time Start", "onDateSet: "+String.format("%d/%d/%d", dayOfMonth, (month12 +1), year12));
            }, year, month, day);
            datePickerDialog.show();
        });
        btn_tEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiscountOnProduct.this, (datePicker, year1, month1, dayOfMonth) -> {
                    tEnd.setText(String.format("%d/%d/%d", dayOfMonth, (month1 +1), year1));
                    //Log.d("Time End", "onDateSet: "+String.format("%d/%d/%d", dayOfMonth, (month1 +1), year1));
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
}