package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.model.order;
import com.example.model.product;
import com.example.model.user;
import com.example.shop_app.Main_menu;
import com.example.shop_app.R;
import com.example.shop_app.ListOrder;
import com.example.shop_app.orderAdapter;
import com.example.shop_app.orderDetail;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private String u_id;
    TextView name;
    ImageView avata;
    SearchView search;
    ImageButton notifications;
    Spinner spin;
    RecyclerView show;
    View view;
    private Main_menu mainMenu;
    List<order> orderList = new ArrayList<>();
    List<order> orderListTemp = new ArrayList<>();
    private orderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home, container, false);
        mainMenu = (Main_menu) getActivity();
        setControl();
        setData();
        setEvent();
        return view;
    }

    private void setData() {


        database.child("user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                user new_user = snapshot.getValue(user.class);
                if (new_user!=null) {

                    if (new_user.getUser_id().equals(u_id)) {
                        setProfile(new_user);
                    }
                }
            }

            private void setProfile(user user_login) {
                name.setText(user_login.getName());
                Glide.with(HomeFragment.this).load(user_login.getAvata()).into(avata);
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
        database.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    order o = dataSnapshot.getValue(order.class);
                    if(o!=null){
                        if(o.getStatus()==1||o.getStatus()==2){
                            orderList.add(o);
                        }
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        adapter = new orderAdapter(orderShow,view.getContext());
        adapter.notifyDataSetChanged();
        show.setAdapter(adapter);
        show.setLayoutManager(linearLayoutManager);
    }

    private void setEvent() {
        notifications.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ListOrder.class);
            startActivity(intent);
        });
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
                    default:
                        showOrder(orderList);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                showOrder(orderList);
            }
        });
    }

    private static void toDetail(View view, order o) {
        Intent intent = new Intent(view.getContext(), orderDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", o);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }

    private void setControl() {
//        Intent intent = getActivity().getIntent();
//        if(intent != null && intent.hasExtra("user_id")) {
//            u_id = intent.getStringExtra("user_id");
//        }
        u_id = mainMenu.getU_id();
        name = view.findViewById(R.id.home_name);
        avata = view.findViewById(R.id.home_avata);
        search = view.findViewById(R.id.home_search);
        notifications = view.findViewById(R.id.home_notifications);
        spin = view.findViewById(R.id.home_spin);
        show = view.findViewById(R.id.home_show);
    }
}