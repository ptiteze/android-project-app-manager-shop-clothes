package com.example.shop_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.order;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private ChildEventListener mChildEventListener;
    private List<order> listThongKe = new ArrayList<>();
    public BarChart chart_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        setControl();

        //settingBarChart();
        setData();
        //add();
    }
    private void settingBarChart()
    {
        //chart1.getDescription().setEnabled(false);
        chart_order.setDrawValueAboveBar(false);
        XAxis xAxis = chart_order.getXAxis();
        xAxis.setAxisMaximum(12);
        xAxis.setAxisMinimum(1);
        String [] months = new String[]{"Tháng 1","Tháng 2", "Tháng 3","Tháng 4","Tháng 5"
                ,"Tháng 6","Tháng 7","Tháng 8","Tháng 9","Tháng 10","Tháng 11","Tháng 12"};

        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        YAxis yAxisRight = chart_order.getAxisRight();
        yAxisRight.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        YAxis yAxisLeft = chart_order.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
    }
    private void setControl()
    {
        chart_order = findViewById(R.id.chart_order);
    }

    private void setData() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                order dh = new order();
                dh = snapshot.getValue(order.class);
                if(dh!=null){
                    listThongKe.add(dh);
                }
                try {
                    setHandler();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
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
        Query query = FirebaseDatabase.getInstance().getReference("order");
        query.addChildEventListener(mChildEventListener);
    }
    private void add()
    {
        for(order dh : listThongKe)
        {

        }
    }
    private void setHandler() throws ParseException {

        HashMap<String, Integer> statistic = new HashMap<>();
        statistic.put("1", 0);
        statistic.put("2", 0);
        statistic.put("3", 0);
        statistic.put("4", 0);
        statistic.put("5", 0);
        statistic.put("6", 0);
        statistic.put("7", 0);
        statistic.put("8", 0);
        statistic.put("9", 0);
        statistic.put("10", 0);
        statistic.put("11", 0);
        statistic.put("12", 0);
        ArrayList<BarEntry> month = new ArrayList<>();
        for(order tk : listThongKe)
        {
//            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            Date date_firebase = df.parse(tk.getUpdateAt());
            String monthTK = tk.getUpdateAt().substring(4,5);
            if(statistic.containsKey(monthTK))
            {
                //int res = Integer.parseInt(monthTK) - 1;
                statistic.put(String.valueOf(monthTK), statistic.get(monthTK) + tk.getTotalPrice());
                int res =  statistic.get(monthTK) + tk.getTotalPrice();
            }
        }
        for (Map.Entry<String, Integer> entry : statistic.entrySet())
        {
            month.add(new BarEntry(Float.parseFloat(entry.getKey()), entry.getValue()));
        }

        BarDataSet dbSet = new BarDataSet(month,"Thống kê doanh thu theo tháng");
        dbSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dbSet.setValueTextColor(Color.BLACK);

        BarData db = new BarData(dbSet);

        chart_order.setFitBars(true);
        chart_order.setData(db);
        chart_order.getDescription().setText("Thống kê doanh thu theo tháng");
        chart_order.animateY(2000);
        chart_order.invalidate();
    }

}