package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.os.health.TimerStat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.category;
import com.example.model.discountOnProduct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

public class Discount extends AppCompatActivity {
    SearchView search;
    ListView show;
    FloatingActionButton btn_add;
    ChildEventListener mChildEventListener;
    List<String> list_discount = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount);
        setControl();
        setData();
        setEvent();
    }
    private void setControl() {
        search = findViewById(R.id.discount_search);
        show = findViewById(R.id.list_discount);
        btn_add = findViewById(R.id.floatbtn_discount);
    }

    private void setData() {

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                discountOnProduct discount = new discountOnProduct();
                discount = snapshot.getValue(discountOnProduct.class);
                if(discount!=null){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        format.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Calendar currentTime = Calendar.getInstance();
                        Calendar dateE = Calendar.getInstance();
                        try {
                            dateE.setTime(format.parse(discount.getTimeEnd()));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if(dateE.after(currentTime)){
                         String data="";
                         data = "Khuyến mãi: "+ discount.getName() + "\n Thời gian"
                                 + discount.getTimeStart() + " - " + discount.getTimeEnd()
                                 + "\n Khuyến mãi: " + String.valueOf(discount.getPercent()) + " %.";
                         list_discount.add(data);
                     }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(Discount.this, android.R.layout.simple_list_item_1,list_discount);
                show.setAdapter(adapter);
                }
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
        Query query = FirebaseDatabase.getInstance().getReference("discount");
        query.addChildEventListener(mChildEventListener);
    }

    private void setEvent() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDiscount = new Intent(Discount.this, AddDiscountOnProduct.class);
                //add.putExtra("user_id", acc.getUser_id());
                startActivity(addDiscount);
            }
        });
    }
}