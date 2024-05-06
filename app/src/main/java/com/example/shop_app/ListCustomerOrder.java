package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.model.order;
import com.example.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListCustomerOrder extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Spinner spin;
    TextView name;
    RecyclerView show;
    List<order> orderList = new ArrayList<>();
    List<order> orderListTemp = new ArrayList<>();
    private orderAdapter adapter;
    user us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_customer_order);
        setControl();
        setData();
        setEvent();
    }

    private void setEvent() {
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        orderListTemp.clear();
                        for (order o: orderList) {
                            if (o.getStatus() == 1) {
                                orderListTemp.add(o);
                            }
                        }
                        showOrder(orderListTemp);
                        break;
                    case 2:
                        orderListTemp.clear();
                        for (order o: orderList) {
                            if (o.getStatus() == 2) {
                                orderListTemp.add(o);
                            }
                        }
                        showOrder(orderListTemp);
                        break;
                    case 3:
                        orderListTemp.clear();
                        for (order o: orderList) {
                            if (o.getStatus() == 3) {
                                orderListTemp.add(o);
                            }
                        }
                        showOrder(orderListTemp);
                        break;
                    case 4:
                        orderListTemp.clear();
                        for (order o: orderList) {
                            if (o.getStatus() == 4) {
                                orderListTemp.add(o);
                            }
                        }
                        showOrder(orderListTemp);
                        break;
                    default:
                        showOrder(orderList);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;
        us = (user) bundle.get("user");
        name.setText(us.getName());
        database.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    order o = dataSnapshot.getValue(order.class);
                    if(o!=null&&o.getUser_id().equals(us.getUser_id())){
                        orderList.add(o);
                    }
                }
                showOrder(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException();
            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showOrder(List<order> orderShow) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new orderAdapter(orderShow,this);
        adapter.notifyDataSetChanged();
        show.setAdapter(adapter);
        show.setLayoutManager(linearLayoutManager);
    }
    private void setControl() {
        spin = findViewById(R.id.listOrderCustomer_spin);
        show = findViewById(R.id.listOrderCustomer_show);
        name = findViewById(R.id.listOrderCustomer_name);
    }
}