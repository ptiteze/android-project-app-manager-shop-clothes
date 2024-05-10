package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.os.health.TimerStat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.discountOnProduct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    SearchView search;
    ListView show;
    FloatingActionButton btn_add;
    ChildEventListener mChildEventListener;
    List<discountOnProduct> listDiscountP = new ArrayList<>();
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
                discountOnProduct discount = snapshot.getValue(discountOnProduct.class);
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
                        if(dateE.after(currentTime)&&discount.isStatus()){
                         String data="";
                         data = "Khuyến mãi: "+ discount.getName() + "\n Thời gian"
                                 + discount.getTimeStart() + " - " + discount.getTimeEnd()
                                 + "\n Khuyến mãi: " + discount.getPercent() + " %.";
                         listDiscountP.add(discount);
                         list_discount.add(data);
                     } else if (discount.isStatus()) {
                            changeStatusDiscount(discount);
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

    private void changeStatusDiscount(discountOnProduct discount) {
        DatabaseReference databases = FirebaseDatabase.getInstance().getReference();
        database.child("discount").child(discount.getId()).child("status").setValue(false);
        database.child("discountList").child(discount.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    databases.child("product").child(dataSnapshot.getKey())
                            .child("discountP").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Discount.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
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
        show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                discountOnProduct discount = listDiscountP.get(i);
                Intent detail = new Intent(Discount.this,AddDiscountOnProduct.class);
                Bundle bundle = new Bundle();
                //Log.d("product", productTemp.toString());
                bundle.putSerializable("discount", discount);
                detail.putExtras(bundle);
                startActivity(detail);
            }
        });
    }
}