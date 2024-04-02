package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.model.user;
import com.example.shop_app.Main_menu;
import com.example.shop_app.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    }

    private void setEvent() {

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