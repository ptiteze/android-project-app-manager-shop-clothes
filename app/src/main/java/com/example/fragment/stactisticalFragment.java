package com.example.fragment;

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
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Chip confirm;
    int sumP;
    ListView show;
    Spinner showVocher;
    TextView totalPrice, finalPrice, vocher;
    ImageButton detail;
    View view;
    List<product> listProduct = new ArrayList<>();
    List<String> listShow = new ArrayList<>();
    List<importOrder> listOrder = new ArrayList<>();
    List<size_stock> listss = new ArrayList<>();
    List<String> list_discount = new ArrayList<>();
    ArrayAdapter<String> adapter;
    List<discountOnOrder> discountOnOrderList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stactistical, container, false);
        // Inflate the layout for this fragment
        setControl();
        setData();
        setEvent();
        return view;
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
        database.child("demo").addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(view.getContext(),"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
        database.child("discountOnOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    discountOnOrder discount = new discountOnOrder();
                    discount = dataSnapshot.getValue(discountOnOrder.class);
                    if(discount!=null) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            format.setTimeZone(TimeZone.getTimeZone("GMT"));
                            Calendar currentTime = Calendar.getInstance();
                            Calendar dateS = Calendar.getInstance();
                            Calendar dateE = Calendar.getInstance();
                            try {
                                dateS.setTime(format.parse(discount.getTimeStart()));
                                dateE.setTime(format.parse(discount.getTimeEnd()));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            if (dateE.after(currentTime)&&currentTime.after(dateS)) {
                                discountOnOrderList.add(discount);
                                String data = "";
                                data = "Khuyến mãi: " + discount.getName() + "\n Thời gian"
                                        + discount.getTimeStart() + " - " + discount.getTimeEnd()
                                        + "\n Khuyến mãi: " + discount.getPercent() + " %."
                                        + " Cho đơn từ: "+discount.getCondition();
                                list_discount.add(data);
                            }
                        }
                    }
                }
                ArrayAdapter<String> adapterVC = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,list_discount);
                showVocher.setAdapter(adapterVC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Không thể kết nối", Toast.LENGTH_SHORT).show();
            }
        });
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
        adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,listShow);
        show.setAdapter(adapter);
    }
    private void setEvent() {
        showVocher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    finalPrice.setText(String.valueOf(sumP));
                }
                else{
                    discountOnOrder discount = discountOnOrderList.get(i-1);
                    if(sumP<discount.getCondition()){
                        Toast.makeText(view.getContext(), "Không đủ điều kiện dùng Vocher", Toast.LENGTH_SHORT).show();
                        showVocher.setSelection(0);
                    }else{
                        Toast.makeText(view.getContext(), "Đang dùng Vocher", Toast.LENGTH_SHORT).show();
                        int fp = sumP-(sumP / 100 * discount.getPercent());
                        finalPrice.setText(String.valueOf(fp));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(view.getContext(), "Không thể kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setControl() {
        confirm = view.findViewById(R.id.demoPr_confirm);
        show  = view.findViewById(R.id.demoorder_show);
        totalPrice = view.findViewById(R.id.demoorder_price);
        detail = view.findViewById(R.id.orderPr_detail);
        finalPrice = view.findViewById(R.id.demoorder_finalPrice);
        vocher = view.findViewById(R.id.demoordervc);
        showVocher = view.findViewById(R.id.demoListVocher);
        list_discount.add(" ");
    }
}