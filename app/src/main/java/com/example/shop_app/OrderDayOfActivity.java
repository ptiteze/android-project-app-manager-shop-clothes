package com.example.shop_app;

import android.Manifest;
import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.model.order;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDayOfActivity extends AppCompatActivity {
    final static int REQUEST_CODE = 1232;
    private ChildEventListener mChildEventListener;
    private List<order> order_month = new ArrayList<>();
    private BarChart chart_day;
    private ImageView start, end;
    private String start_date, end_date;
    private Button btn_look,btn_export;
    int pageWidth = 1200;
    int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_day_of);

        askPermission();

        setControl();
        setEvent();
        setData();
        setEventButton();
    }

    private void setEventButton()
    {
        btn_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(check())
                        handleOrder();
                    else
                        Toast.makeText(OrderDayOfActivity.this,"Please enter data",Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()) {
                    try {
                        createPDF();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                    Toast.makeText(OrderDayOfActivity.this,"Please enter data",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean check()
    {
        if(start_date == null|| end_date == null)
        {
            return false;
        }
        return true;
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

    }
    private void setControl()
    {
        chart_day = findViewById(R.id.chart_day);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        btn_look = findViewById(R.id.btn_look);
        btn_export = findViewById(R.id.btn_export);
    }
    private void setEvent()
    {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderDayOfActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        start_date = formatDate(dayOfMonth,month,year);

                    }
                }, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                datePickerDialog.show();

            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderDayOfActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        end_date = formatDate(dayOfMonth,month,year);
                        Toast.makeText(OrderDayOfActivity.this,end_date,Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                datePickerDialog.show();

            }
        });
    }
    private String formatDate(int day, int month, int year)
    {
        return day + "/" + month + "/"+ year;
    }
    private void setData() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                order dh = new order();
                dh = snapshot.getValue(order.class);
                if(dh!=null){
                    order_month.add(dh);
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

    private void handleOrder() throws ParseException {
        if (order_month.isEmpty())
        {
            Toast.makeText(OrderDayOfActivity.this,"List empty",Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<Integer, Integer> statistic = new HashMap<>();
        ArrayList<BarEntry> month = new ArrayList<>();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date start = df.parse(start_date);
        Date end = df.parse(end_date);
        int count = 0;
        for(order tk : order_month)
        {
            Date date_firebase = df.parse(tk.getUpdateAt());
            if(date_firebase.compareTo(start) >= 0 && date_firebase.compareTo(end) <=0)
            {
                statistic.put(count,tk.getTotalPrice());
            }
            count++;
        }
        for (Map.Entry<Integer, Integer> entry : statistic.entrySet())
        {
            month.add(new BarEntry(Float.parseFloat(String.valueOf(entry.getKey())), entry.getValue()));
        }

        BarDataSet dbSet = new BarDataSet(month,"Thống kê");
        dbSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dbSet.setValueTextColor(Color.BLACK);

        BarData db = new BarData(dbSet);

        chart_day.setFitBars(true);
        chart_day.setData(db);
        chart_day.getDescription().setText("Thống kê doanh thu theo mốc thời gian");
        chart_day.animateY(2000);
        chart_day.invalidate();

    }
    private void createPDF() throws ParseException {
        PdfDocument myPdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080,1920,1).create();
        PdfDocument.Page page = myPdf.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.rgb(0,113,188));
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String title = "THỐNG KÊ DANH SÁCH ĐƠN HÀNG";
        String timeRange = "Từ ngày " + start_date +" đến ngày "+ end_date;

        canvas.drawText(title, pageWidth/2 , 100, paint);
        canvas.drawText(timeRange, pageWidth/2-40, 200, paint);

        paint.setStyle(Paint.Style.STROKE);

        paint.setTextSize(30);
        paint.setStrokeWidth(2);
        canvas.drawRect(20,380, pageWidth - 150,460,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("STT", 40,420,paint);
        canvas.drawText("MÃ KH",130,420,paint);
        canvas.drawText("TỔNG GIÁ",280,420,paint);
        canvas.drawText("THỜI GIAN", 450, 420,paint);
        canvas.drawText("THANH TOÁN", 700, 420,paint);

        canvas.drawLine(120,390,120,440,paint);
        canvas.drawLine(260,390,260,440,paint);
        canvas.drawLine(430,390,430,440,paint);
        canvas.drawLine(640,390,640,440,paint);
        int stt = 1;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date start = df.parse(start_date);
        Date end = df.parse(end_date);
        y = 0;
        float total = 0;
        for(order tk : order_month)
        {
            Date date_firebase = df.parse(tk.getUpdateAt());
            if(date_firebase.compareTo(start) >= 0 && date_firebase.compareTo(end) <=0)
            {
                canvas.drawText(String.valueOf(stt), 40,520 + y,paint);
                //canvas.drawText(tk.getUser_id(),130 ,520 + y,paint);
                canvas.drawText(String.valueOf(tk.getTotalPrice()),270 ,520 + y,paint);
                canvas.drawText(tk.getUpdateAt(), 450, 520 + y,paint);
                canvas.drawText(tk.getPayment(), 650, 520 + y,paint);
                y+= 70;
                stt++;
                total += tk.getTotalPrice();
            }
        }
        canvas.drawLine(20,520 + y,pageWidth - 150,520 + y,paint);
        canvas.drawText("Total: " , 700, 520 + y + 50,paint);
        canvas.drawText(String.valueOf(total), 800, 520 + y + 50,paint);

        myPdf.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //Date date = new Date();
        String fileName = "export.pdf"+ "";
        File file = new File(downloadsDir, fileName);

        try{
            FileOutputStream fos = new FileOutputStream(file);
            myPdf.writeTo(fos);
            myPdf.close();
            fos.close();
            Toast.makeText(this, "Written Successfully!!!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.d("mylog", "Error while writing " + e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetValue()
    {
        start_date = "";
        end_date = "";
    }
}