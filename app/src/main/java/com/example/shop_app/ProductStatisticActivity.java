package com.example.shop_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.model.category;
import com.example.model.product;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStatisticActivity extends AppCompatActivity {

    private List<product> listThongKe = new ArrayList<>();
    private List<category> listCategory = new ArrayList<>();
    private ChildEventListener mChildEventListener;
    private PieChart chart_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_statistic);
        setControl();

        setCategory();
        setDataProduct();
    }

    private void setControl()
    {
        chart_product = findViewById(R.id.chart_product);
    }
    private void setCategory()
    {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                category cate = snapshot.getValue(category.class);
                if(cate!=null){
                    listCategory.add(cate);

                }
                Toast.makeText(ProductStatisticActivity.this, cate.getName(), Toast.LENGTH_SHORT).show();
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
        Query query = FirebaseDatabase.getInstance().getReference("category");
        query.addChildEventListener(mChildEventListener);
    }
    private void setDataProduct() {

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                product pr = snapshot.getValue(product.class);
                if(pr!=null){
                    listThongKe.add(pr);

                }
                setHanler(listThongKe);
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
        Query query = FirebaseDatabase.getInstance().getReference("product");
        query.addChildEventListener(mChildEventListener);
    }
    private void setHanler(List<product> listThongKe)
    {
        HashMap<String, Integer> statistic = new HashMap<>();
        for(category cate : listCategory)
        {
            statistic.put(cate.getName(),0);
        }
        ArrayList<PieEntry> entries = new ArrayList<>();
        for(product pr : listThongKe)
        {
            if(statistic.containsKey(pr.getCategory()))
            {
                statistic.put(pr.getCategory(), statistic.get(pr.getCategory()) + pr.getStock());
            }
        }
        for (Map.Entry<String, Integer> entry : statistic.entrySet())
        {
            entries.add(new PieEntry( entry.getValue(), entry.getKey()));
        }
        PieDataSet dataSetImport = new PieDataSet(entries,"Biểu đồ các sản phẩm tỉ lệ");
        dataSetImport.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData dataImport = new PieData(dataSetImport);
        chart_product.setData(dataImport);
        chart_product.animateY(2000);
        chart_product.invalidate();

    }
}