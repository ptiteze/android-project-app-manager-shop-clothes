package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.model.discountOnOrder;
import com.example.model.discountOnProduct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class DiscountOnOrder extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    SearchView search;
    ListView show;
    FloatingActionButton btn_add;
    List<String> list_discount = new ArrayList<>();
    List<discountOnOrder> listDiscountO = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_on_order);
        setControl();
        setData();
        setEvent();
    }

    private void setData() {
        database.child("discountOnOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_discount.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    discountOnOrder discount = new discountOnOrder();
                    discount = dataSnapshot.getValue(discountOnOrder.class);
                    if(discount!=null) {
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
                            if (dateE.after(currentTime)) {
                                String data = "";
                                data = "Khuyến mãi: " + discount.getName() + "\n Thời gian"
                                        + discount.getTimeStart() + " - " + discount.getTimeEnd()
                                        + "\n Khuyến mãi: " + discount.getPercent() + " %."
                                        + " Cho đơn từ: "+discount.getCondition();
                                list_discount.add(data);
                                listDiscountO.add(discount);
                            }
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(DiscountOnOrder.this, android.R.layout.simple_list_item_1,list_discount);
                show.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        search = findViewById(R.id.discountOO_search);
        show = findViewById(R.id.list_discountOO);
        btn_add = findViewById(R.id.floatbtn_discountOO);
    }
    private void setEvent() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDiscount = new Intent(DiscountOnOrder.this, AddDiscountOnOrder.class);
                //add.putExtra("user_id", acc.getUser_id());
                startActivity(addDiscount);
            }
        });
        show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                discountOnOrder discount = listDiscountO.get(i);
                Intent detail = new Intent(DiscountOnOrder.this,AddDiscountOnOrder.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("discount", discount);
                detail.putExtras(bundle);
                startActivity(detail);
            }
        });
    }
}