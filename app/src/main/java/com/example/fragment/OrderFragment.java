package com.example.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.category;
import com.example.model.importOrder;
import com.example.model.product;
import com.example.model.saveImport;
import com.example.model.size_stock;
import com.example.shop_app.AddCategory;
import com.example.shop_app.Category;
import com.example.shop_app.ImportProduct;
import com.example.shop_app.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Chip confirm;
    int sumP;
    ListView show;
    TextView totalPrice;
    ImageButton detail;
    View view;
    List<product> listProduct = new ArrayList<>();
    List<String> listShow = new ArrayList<>();
    List<importOrder> listOrder = new ArrayList<>();
    List<size_stock> listss = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        // Inflate the layout for this fragment
        setControl();
        setData();
        setEvent();
        return view;
    }

    private void setEvent() {
        DatabaseReference databaseI = FirebaseDatabase.getInstance().getReference();
        confirm.setOnClickListener(view -> {
            listss.clear();
            List<product> listPrTemp = new ArrayList<>();
            if(listShow.isEmpty()){
                Toast.makeText(view.getContext(),"không có sản phẩm để nhập",Toast.LENGTH_SHORT).show();
            }else{
                for(importOrder io: listOrder) {
                    Map<String,Integer> MapIO = new HashMap<>(io.getSize_stock());
                    for (product pr: listProduct){
                        if(io.getId().equals(pr.getId())){
                            product prTemp = pr;
                            prTemp.setStock(prTemp.getStock() + io.getStock());
                            listPrTemp.add(prTemp);
                            break;
                        }
                    }
                    size_stock ss = new size_stock();
                    ss.setProduct_id(io.getId());
                    database.child("productSize").child(io.getId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, Integer> mapTemp = new HashMap<>();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                String key = dataSnapshot.getKey();
                                int value = dataSnapshot.getValue(Integer.class);
                                mapTemp.put(key,MapIO.get(key) + value);
                            }
                            ss.setSize_stock(mapTemp);
                            listss.add(ss);
                            if(listss.size()==listPrTemp.size()){

                                for (size_stock si: listss) {
                                   databaseI.child("productSize").child(si.getProduct_id())
                                           .setValue(si.getSize_stock());
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(view.getContext(),"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Log.d("product size", listPrTemp.size()+"");
                for (product pi: listPrTemp) {
                    databaseI.child("product").child(pi.getId()).child("stock")
                            .setValue(pi.getStock());
                }
                saveImport si = new saveImport();
                Date date  = new Date();
                String id = database.child("orderImport").push().getKey();
                si.setId(id);
                si.setTotalPrice(sumP);
                si.setCreateAt(String.valueOf(date.getDate()));
                database.child("orderImport").child(si.getId()).setValue(si);
                DatabaseReference iRef = FirebaseDatabase.getInstance().getReference("importProduct");
                DatabaseReference importRef = FirebaseDatabase.getInstance().getReference("importDetail/"+si.getId());
                iRef.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        Object data = currentData.getValue();
                        importRef.setValue(data);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        if (error != null) {
                            // Xử lý lỗi
                            error.toException();
                        }
                    }
                });
                database.child("importProduct").setValue(null);
                Toast.makeText(view.getContext(),"Nhập sản phẩm hoàn tất",Toast.LENGTH_SHORT).show();
                listShow.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setData() {
        database.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    product pr = dataSnapshot.getValue(product.class);
                    if (pr != null) {
                        listProduct.add(pr);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(),"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
        database.child("importProduct").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOrder.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    importOrder io = new importOrder();
                    io.setId(dataSnapshot.getKey());
                    Map<String, Object> sizeMapData = (Map<String, Object>) dataSnapshot.getValue();
                    Map<String, Integer> sizeMap = new HashMap<>();
                    for (Map.Entry<String, Object> entry : sizeMapData.entrySet()) {
                        sizeMap.put(entry.getKey(), ((Long) entry.getValue()).intValue());
                    }
                    for (product pr: listProduct) {
                        if (pr.getId().equals(io.getId())){
                            io.setName(pr.getName());
                            io.setPrice(pr.getImportPrice());
                            break;
                        }
                    }
                    int sum = 0;
                    for (int value: sizeMap.values()){
                        sum+=value;
                    }
                    io.setStock(sum);
                    int total = sum*io.getPrice();
                    io.setTotalPrice(total);
                    io.setSize_stock(sizeMap);
                    listOrder.add(io);
                }
                showData(listOrder);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(),"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showData(List<importOrder> listOrder) {
        listShow.clear();
        sumP =0;
        for (importOrder io: listOrder) {
            sumP += io.getTotalPrice();
            String data = "Sản phẩm: "+io.getName()+" - Đơn giá nhập: "+ String.valueOf(io.getPrice())
                            +" VND\n Số lượng: ";
            for (String s: io.getSize_stock().keySet()){
                data += (s+": "+String.valueOf(io.getSize_stock().get(s))+"  ");
            }
            data += ("\n Tổng giá: "+ String.valueOf(io.getTotalPrice()));
            listShow.add(data);
        }
        totalPrice.setText(String.valueOf(sumP));
        adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,listShow);
        show.setAdapter(adapter);
    }

    private void setControl() {
        confirm = view.findViewById(R.id.orderPr_confirm);
        show  = view.findViewById(R.id.orderPr_show);
        totalPrice = view.findViewById(R.id.orderPr_price);
        detail = view.findViewById(R.id.orderPr_detail);
    }
}