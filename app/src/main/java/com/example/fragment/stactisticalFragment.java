package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.discountOnOrder;
import com.example.model.importOrder;
import com.example.model.product;
import com.example.model.size_stock;
import com.example.shop_app.AddProduct;
import com.example.shop_app.DiscountOnOrder;
import com.example.shop_app.OrderActivity;
import com.example.shop_app.ProductStatisticActivity;
import com.example.shop_app.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class stactisticalFragment extends Fragment {
    Chip scale, block;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stactistical, container, false);
        // Inflate the layout for this fragment
        setControl();
        //setData();
        setEvent();
        return view;
    }

    private void setEvent() {
        scale.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), ProductStatisticActivity.class);
            startActivity(intent);
        });
        block.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), OrderActivity.class);
            startActivity(intent);
        });
    }

    private void setControl() {
        scale = view.findViewById(R.id.chart_scale);
        block = view.findViewById(R.id.chart_block);
    }


}