package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.discountOnProduct;
import com.example.model.product;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AddDiscountOnProduct extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    TextView title, tStart, tEnd;
    Chip btn_add, btn_cancel, btn_remove;
    ImageButton btn_back, btn_tStart, btn_tEnd;
    EditText name, des, percent;
    ListView listProduct, listProductDiscont;
    ChildEventListener mChildEventListener;
    List<String> listPD = new ArrayList<>();
    List<String> listPDTrue = new ArrayList<>();
    // id, name
    List<String> list_pr_key = new ArrayList<>(); // pr Id
    List<String> list_pr_val= new ArrayList<>();    //pr name
    ArrayAdapter<String> adapterD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discount_on_product);
        setControl();
        setData();
        setEvent();
    }

    private void setData() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                product pr = snapshot.getValue(product.class);
                if(pr!=null){
                    list_pr_key.add(pr.getId());
                    list_pr_val.add(pr.getName());
                }
                showDataProduct(listProduct);
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
        DatabaseReference databases = FirebaseDatabase.getInstance().getReference();
        database.child("discount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> listDTrue = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    discountOnProduct dp = dataSnapshot.getValue(discountOnProduct.class);
                    if(dp!=null&&dp.isStatus()){
                        //Log.d("id discount", dp.getId());
                        listDTrue.add(dp.getId());
                    }
                }
                listPDTrue.clear();
                for (String key : listDTrue) {
                    databases.child("discountList").child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                //Log.d("id pr", dataSnapshot.getKey());
                                listPDTrue.add(dataSnapshot.getKey());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AddDiscountOnProduct.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddDiscountOnProduct.this,"không thể kết nối tới dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDataProduct(ListView listView) {
        ArrayAdapter<String> adapter = new ArrayAdapter(AddDiscountOnProduct.this, android.R.layout.simple_list_item_1,list_pr_val);
        listView.setAdapter(adapter);
    }

    private void setControl() {
        name = findViewById(R.id.addDiscount_name);
        des = findViewById(R.id.addDiscount_description);
        title = findViewById(R.id.addDiscount_title);
        btn_add = findViewById(R.id.addDiscount_complete);
        btn_cancel = findViewById(R.id.addDiscount_cancel);
        btn_remove = findViewById(R.id.addDiscount_remove);
        percent = findViewById(R.id.addDiscount_percent);
        tStart = findViewById(R.id.addDiscount_timeStart);
        tEnd = findViewById(R.id.addDiscount_timeEnd);
        btn_tStart = findViewById(R.id.timeStart_btn);
        btn_tStart.setEnabled(false);
        btn_tEnd = findViewById(R.id.timeEnd_btn);
        btn_back = findViewById(R.id.addDiscount_back);
        listProduct = findViewById(R.id.addDiscount_listProduct);
        listProductDiscont = findViewById(R.id.addDiscount_productDiscount);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        tStart.setText(day+"/"+month+"/"+year);
    }
    private void clearInput() {
        name.setText("");
        des.setText("");
        percent.setText("");
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        tStart.setText(String.format("%d/%d/%d", day, (month +1), year));
//        tEnd.setText(String.format("%d/%d/%d", day, (month +1), year));
    }

    private void setEvent() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInput();
            }

        });
        listProductDiscont.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPD.remove(i);
                adapterD.notifyDataSetChanged();
            }
        });
        listProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listPD.isEmpty()||(!listPD.contains(list_pr_val.get(i)))) {
                    listPD.add(list_pr_val.get(i));
                    ShowListProductDiscount();
                }
            }
        });
        btn_back.setOnClickListener(view -> finish());
        btn_tStart.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiscountOnProduct.this, (datePicker, year12, month12, dayOfMonth) -> {
                tStart.setText(String.format("%d/%d/%d", dayOfMonth, (month12 +1), year12));
                //Log.d("Time Start", "onDateSet: "+String.format("%d/%d/%d", dayOfMonth, (month12 +1), year12));
            }, year, month, day);
            datePickerDialog.show();
        });
        btn_tEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiscountOnProduct.this, (datePicker, year1, month1, dayOfMonth) -> {
                    tEnd.setText(String.format("%d/%d/%d", dayOfMonth, (month1 +1), year1));
                    //Log.d("Time End", "onDateSet: "+String.format("%d/%d/%d", dayOfMonth, (month1 +1), year1));
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkfalse()){
                    discountOnProduct discount = new discountOnProduct();
                    discount.setName(name.getText().toString().trim());
                    discount.setDes(des.getText().toString().trim());
                    discount.setTimeStart(tStart.getText().toString());
                    discount.setTimeEnd(tEnd.getText().toString());
                    discount.setPercent(Integer.valueOf(percent.getText().toString()));
                    discount.setStatus(true);
                    saveDiscount(discount);
                }
                //Log.d("list pr true:", " "+ listPDTrue.size());
            }
        });
    }

    private void saveDiscount(discountOnProduct discount) {
        List<String> listKey = new ArrayList<>();
        String discount_id = database.child("discount").push().getKey();
        discount.setId(discount_id);
        int percent = discount.getPercent();
        assert discount_id != null;
        database.child("discount").child(discount_id != null ? discount_id : null).setValue(discount);
        for (String pr: listPD) {
            listKey.add(list_pr_key.get(list_pr_val.indexOf(pr)));
        }
        for (String name: listKey) {
            database.child("discountList").child(discount_id).child(name).setValue(1);
            database.child("product").child(name).child("discountP").setValue(percent);
        }
        clearInput();
        Toast.makeText(AddDiscountOnProduct.this, "Các sản phẩm đã được giảm giá", Toast.LENGTH_SHORT).show();
    }


    private boolean checkfalse() {
        int p = Integer.valueOf(percent.getText().toString());
        if(name.getText().toString().trim().isEmpty()){
            name.setError("Chưa nhâp tên đợt giảm giá");
            name.requestFocus();
            return true;
        }
        if(name.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9-/âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            name.setError("Tên sản phâm Không được chứa kí tự đặc biệt");
            name.requestFocus();
            return true;
        }
        if(des.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9-/âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            des.setError("Thông tin sản phẩm Không được chứa kí tự đặc biệt");
            des.requestFocus();
            return true;
        }
//        if(tStart.getText().toString().trim().isEmpty()){
//            tStart.setError("Chưa nhâp tên đợt giảm giá");
//            tStart.requestFocus();
//            return true;
//        }
        if(p<=0||p>100){
            percent.setError("Tỉ lệ giảm giá không phù hợp");
            percent.requestFocus();
            return true;
        }
        if(tEnd.getText().toString().trim().isEmpty()){
            tEnd.setError("Chưa nhâp thời gian kết thúc đợt giảm giá");
            tEnd.requestFocus();
            return true;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String tS = tStart.getText().toString().trim();
            String tE = tEnd.getText().toString().trim();
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                Calendar currentTime = Calendar.getInstance();
                Calendar dateS = Calendar.getInstance();
                dateS.setTime(format.parse(tS));
                Calendar dateE = Calendar.getInstance();
                dateE.setTime(format.parse(tE));
//                Date dateS = format.parse(tS);
//                Date dateE = format.parse(tS);
//                if(currentTime.after(dateS)){
//                    Toast.makeText(AddDiscountOnProduct.this, "thời gian bắt đầu giảm giá phải sau thời gian hiện tại", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
                if(!dateE.after(dateS)||tStart.getText().toString().equals(tEnd.getText().toString())){
                    Toast.makeText(AddDiscountOnProduct.this, "thời gian hết giảm giá phải sau thời gian bắt đầu giảm giá", Toast.LENGTH_SHORT).show();
                    return true;
                }
                // Bây giờ bạn có thể sử dụng đối tượng Date hoặc chuyển đổi nó thành định dạng khác nếu cần.
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            LocalDate dateS = LocalDate.parse(tS,formatter);
//            LocalDate dateE = LocalDate.parse(tE, formatter);
        }
        if(listPD.isEmpty()){
            Toast.makeText(AddDiscountOnProduct.this, "Chưa chọn sản phẩm giảm giá", Toast.LENGTH_SHORT).show();
            return true;
        }
        for (String value : listPD) {
            if (listPDTrue.contains(list_pr_key.get(list_pr_val.indexOf(value)))){
                Toast.makeText(AddDiscountOnProduct.this, "sản phẩm: "+
                        value+" đang được áp dụng ưu đãi khác", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
    private void ShowListProductDiscount() {
        adapterD = new ArrayAdapter(AddDiscountOnProduct.this, android.R.layout.simple_list_item_1,listPD);
        listProductDiscont.setAdapter(adapterD);
    }
}