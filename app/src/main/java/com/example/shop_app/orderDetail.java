package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.importOrder;
import com.example.model.order;
import com.example.model.product;
import com.example.model.size_stock;
import com.example.model.user;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderDetail extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ImageButton back;
    Chip cancel, compelete, info;
    TextView id, name, phone, address, createAt, updateAt, payment,
                totalPrice, status;
    ListView showProduct;
    order or = new order();
    user us = new user();
    List<importOrder> listOrder = new ArrayList<>();
    List<String> listShow = new ArrayList<>();
    List<size_stock> listss = new ArrayList<>();
    List<product> listProduct = new ArrayList<>();
    ArrayAdapter<String> adapter;
    boolean overStock = false;
    int sumP;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        setCotrol();
        setData();
        setEvent();
    }
    private void setCotrol() {
        back = findViewById(R.id.orderDetail_back);
        compelete = findViewById(R.id.orderDetail_complete);
        cancel = findViewById(R.id.orderDetail_cancel);
        id = findViewById(R.id.order_detail_id);
        name = findViewById(R.id.order_detail_name);
        phone = findViewById(R.id.order_detail_phone);
        address = findViewById(R.id.order_detail_address);
        createAt = findViewById(R.id.order_detail_createAt);
        updateAt = findViewById(R.id.order_detail_updateAt);
        payment = findViewById(R.id.order_detail_payment);
        totalPrice = findViewById(R.id.order_detail_totalPrice);
        status = findViewById(R.id.order_detail_status);
        showProduct = findViewById(R.id.order_detail_show);
        info = findViewById(R.id.orderDetail_info);
    }
    private void setEvent() {
        back.setOnClickListener(view -> finish());
        compelete.setOnClickListener(view -> {
            if(or!=null){
                int status = or.getStatus();
                switch (status){
                    case 1:
                        or.setStatus(2);
                        //updateOrder(or);
                        updateProduct(2);
                        break;
                    case 2:
                        Toast.makeText(orderDetail.this,"Đơn hàng đang được giao ", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(orderDetail.this,"Khách hàng đã nhận được đơn hàng thành công ", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(orderDetail.this,"Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        cancel.setOnClickListener(view -> {
            if(or!=null){
                switch (or.getStatus()){
                    case 1:
                        or.setStatus(4);
                        updateOrder(or);
                        Toast.makeText(orderDetail.this,"Đã thay đổi trạng thái đơn hàng: " +
                                "Không Xác nhận đơn hàng ", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        or.setStatus(4);
                        updateOrder(or);
                        updateProduct(4);
                        Toast.makeText(orderDetail.this,"Đã thay đổi trạng thái đơn hàng: " +
                        "Hủy đơn hàng ", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(orderDetail.this,"Khách hàng đã nhận được đơn hàng thành công, không thể hủy đơn ", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(orderDetail.this,"Đơn hàng đã được hủy từ trước", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        info.setOnClickListener(view -> {
            Intent intent = new Intent(this,CustomerInfo.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",us);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void updateProduct(int s) {
        overStock = false;
        DatabaseReference databaseI = FirebaseDatabase.getInstance().getReference();
        listss.clear();
        List<product> listPrTemp = new ArrayList<>();
        for(importOrder io : listOrder) {

            Map<String,Integer> MapIO = new HashMap<>(io.getSize_stock());
            for (product pr: listProduct){
                if(io.getId().equals(pr.getId())){
                    product prTemp = pr;
                    if(s==2){
                        prTemp.setStock(prTemp.getStock() - io.getStock());
                    }
                    if(s==4)
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
                        if(key!=null){
                            if(s==2){
                                if(MapIO.getOrDefault(key, 0)>value){
                                    Toast.makeText(orderDetail.this,"Số lượng tồn trong kho không đủ đáp " +
                                            "ứng đơn hàng",Toast.LENGTH_SHORT).show();
                                    overStock = true;
                                    return;
                                }else mapTemp.put(key,value-MapIO.getOrDefault(key, 0));
                            }

                            if(s==4)
                                mapTemp.put(key,MapIO.getOrDefault(key, 0) + value);
                        }
                    }
                    ss.setSize_stock(mapTemp);
                    listss.add(ss);
                    if(!overStock&&listPrTemp.size()==listOrder.size()){
                        Log.d("size list pr", listPrTemp.size()+"  "+overStock);
                        for (product pi: listPrTemp) {
                            databaseI.child("product").child(pi.getId()).child("stock")
                                    .setValue(pi.getStock());
                        }
                        Toast.makeText(orderDetail.this,"Đã thay đổi trạng thái đơn hàng: " +
                                " Xác nhận đơn hàng ", Toast.LENGTH_SHORT).show();
                        updateOrder(or);
                        if(listss.size()==listPrTemp.size()){
                            Log.d("size list ss", listss.size()+"  "+overStock);
                            //i==listOrder.size()&&!overStock
                            for (size_stock si: listss) {
                                databaseI.child("productSize").child(si.getProduct_id())
                                        .setValue(si.getSize_stock());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(orderDetail.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
                }
            });
        }
        //Log.d("product size", listPrTemp.size()+"");

//        database.child("importProduct").setValue(null);
        //Toast.makeText(orderDetail.this,"Nhập sản phẩm hoàn tất",Toast.LENGTH_SHORT).show();
    }



    private void updateOrder(order or) {
        database.child("order").child(or.getId()).setValue(or);
        setStatus(or);
    }

    private void setData() {
        //user , allProduct,
        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;
        or =  (order) bundle.get("order");
        assert or != null;
        id.setText(or.getId());
        createAt.setText(or.getCreateAt());
        updateAt.setText(or.getUpdateAt());
        payment.setText(or.getPayment());
        totalPrice.setText(or.getTotalPrice()+" VNĐ");
        setStatus(or);
        String user_id = or.getUser_id();
        database.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    user u = dataSnapshot.getValue(user.class);
                    if(u.getUser_id().equals(user_id)){
                        us = u;
                        break;
                    }
                }
                name.setText(us.getName());
                phone.setText(us.getPhone());
                address.setText(us.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException();
            }
        });
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
                Toast.makeText(orderDetail.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
        database.child("orderDetail").child(or.getId()).addValueEventListener(new ValueEventListener() {
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
                            io.setPrice(pr.getPrice());
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
                Toast.makeText(orderDetail.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStatus(order ord) {
        String statuss = "";
        switch (ord.getStatus()){
            case 1:
                statuss = "Đang xử lý";
                break;
            case 2:
                statuss = "Đang giao";
                break;
            case 3:
                statuss = "Đã nhận";
                break;
            case 4:
                statuss = "Hủy";
                break;
        }
        status.setText(statuss);
    }

    private void showData(List<importOrder> listOrder) {
        listShow.clear();
        sumP =0;
        for (importOrder io: listOrder) {
            sumP += io.getTotalPrice();
            String data = "Sản phẩm: "+io.getName()+" - Đơn giá mua: "+ io.getPrice()
                    +" VND\n Số lượng: ";
            for (String s: io.getSize_stock().keySet()){
                data += (s+": "+String.valueOf(io.getSize_stock().get(s))+"  ");
            }
            data += ("\n Tổng giá: "+ String.valueOf(io.getTotalPrice()));
            listShow.add(data);
        }
        totalPrice.setText(String.valueOf(sumP));
        adapter = new ArrayAdapter(orderDetail.this, android.R.layout.simple_list_item_1,listShow);
        showProduct.setAdapter(adapter);
    }

}