package com.example.shop_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.discountOnOrder;
import com.example.model.discountOnProduct;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddDiscountOnOrder extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    TextView title, tStart, tEnd;
    Chip btn_add, btn_cancel, btn_remove;
    ImageButton btn_back, btn_tStart, btn_tEnd;
    EditText name, des, percent, condition;
    discountOnOrder disc;
    boolean updateState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discount_on_order);
        setControl();
        setData();
        setEvent();
    }

    private void setEvent() {
        btn_cancel.setOnClickListener(view -> clearInput());
        btn_back.setOnClickListener(view -> finish());
        btn_tStart.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiscountOnOrder.this, (datePicker, year12, month12, dayOfMonth) -> {
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
                @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiscountOnOrder.this, (datePicker, year1, month1, dayOfMonth) -> {
                    tEnd.setText(String.format("%d/%d/%d", dayOfMonth, (month1 +1), year1));
                    //Log.d("Time End", "onDateSet: "+String.format("%d/%d/%d", dayOfMonth, (month1 +1), year1));
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        btn_add.setOnClickListener(view -> {
            if(!checkfalse()){
                discountOnOrder discount = new discountOnOrder();
                discount.setName(name.getText().toString().trim());
                discount.setDes(des.getText().toString().trim());
                discount.setTimeStart(tStart.getText().toString());
                discount.setTimeEnd(tEnd.getText().toString());
                discount.setPercent(Integer.parseInt(percent.getText().toString()));
                discount.setCondition(Integer.parseInt(condition.getText().toString()));
                saveDiscount(discount);
            }
        });
        btn_remove.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH)-1;
            String date = day+"/"+month+"/"+year;
            disc.setTimeEnd(date);
            database.child("discountOnOrder").child(disc.getId()).setValue(disc);
            Toast.makeText(AddDiscountOnOrder.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
            btn_remove.setEnabled(false);
        });
    }

    private void saveDiscount(discountOnOrder discount) {
        String discount_id = database.child("discountOnOder").push().getKey();
        discount.setId(discount_id);
        assert discount_id != null;
        database.child("discountOnOrder").child(discount_id != null ? discount_id : null).setValue(discount);
        clearInput();
        Toast.makeText(AddDiscountOnOrder.this, "Lưu đợt giảm giá hoàn tất", Toast.LENGTH_SHORT).show();
    }

    private boolean checkfalse() {
        int p = Integer.valueOf(percent.getText().toString());
        if(name.getText().toString().trim().isEmpty()){
            name.setError("Chưa nhâp tên đợt giảm giá");
            name.requestFocus();
            return true;
        }
        if(name.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9-/,%âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            name.setError("Tên sản phâm Không được chứa kí tự đặc biệt");
            name.requestFocus();
            return true;
        }
        if(des.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9-/,%âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            des.setError("Thông tin sản phẩm Không được chứa kí tự đặc biệt");
            des.requestFocus();
            return true;
        }
        if(tStart.getText().toString().trim().isEmpty()){
            tStart.setError("Chưa nhâp tên đợt giảm giá");
            tStart.requestFocus();
            return true;
        }
        if(tEnd.getText().toString().trim().isEmpty()){
            tEnd.setError("Chưa nhâp tên đợt giảm giá");
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
                dateE.setTime(format.parse(tS));
//                Date dateS = format.parse(tS);
//                Date dateE = format.parse(tS);
//                if(currentTime.after(dateS)){
//                    Toast.makeText(AddDiscountOnOrder.this, "thời gian bắt đầu giảm giá phải sau thời gian hiện tại", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
                if(dateE.after(dateS)||tStart.getText().toString().equals(tEnd.getText().toString())){
                    Toast.makeText(AddDiscountOnOrder.this, "thời gian hết giảm giá phải sau thời gian bắt đầu giảm giá", Toast.LENGTH_SHORT).show();
                    return true;
                }
                // Bây giờ bạn có thể sử dụng đối tượng Date hoặc chuyển đổi nó thành định dạng khác nếu cần.
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            LocalDate dateS = LocalDate.parse(tS,formatter);
//            LocalDate dateE = LocalDate.parse(tE, formatter);
        }
        if(percent.getText().toString().equals("")){
            percent.setError("Chưa nhập tỉ lệ giảm");
            percent.requestFocus();
            return true;
        }
        if(p<=0||p>100){
            percent.setError("Tỉ lệ giảm giá không phù hợp");
            percent.requestFocus();
            return true;
        }
        if(condition.getText().toString().equals("")){
            condition.setError("Chưa nhập tỉ lệ giảm");
            condition.requestFocus();
            return true;
        }
        return false;
    }

    private void clearInput() {
        name.setText("");
        des.setText("");
        percent.setText("");
        condition.setText("");
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        tStart.setText(String.format("%d/%d/%d", day, (month +1), year));
//        tEnd.setText(String.format("%d/%d/%d", day, (month +1), year));
    }
    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            updateState = false;
        }else{
            disc =  (discountOnOrder) bundle.get("discount");
            assert disc != null;
            updateState = true;
            setEneble(disc);
        }
    }

    private void setEneble(discountOnOrder disc) {
        title.setText("VIEW DISCOUNT");
        name.setText(disc.getName());
        des.setText(disc.getDes());
        percent.setText(disc.getPercent()+"");
        tStart.setText(disc.getTimeStart());
        tEnd.setText(disc.getTimeEnd());
        condition.setText(disc.getCondition()+"");
        condition.setEnabled(false);
        btn_add.setEnabled(false);
        btn_cancel.setEnabled(false);
        btn_tEnd.setEnabled(false);
        name.setEnabled(false);
        des.setEnabled(false);
        percent.setEnabled(false);
        btn_remove.setVisibility(View.VISIBLE);
        btn_remove.setEnabled(true);
    }

    private void setControl() {
        name = findViewById(R.id.addDiscountOO_name);
        des = findViewById(R.id.addDiscountOO_description);
        title = findViewById(R.id.addDiscountOO_title);
        btn_add = findViewById(R.id.addDiscountOO_complete);
        btn_cancel = findViewById(R.id.addDiscountOO_cancel);
        btn_remove = findViewById(R.id.addDiscountOO_remove);
        percent = findViewById(R.id.addDiscountOO_percent);
        condition = findViewById(R.id.addDiscountOO_condition);
        tStart = findViewById(R.id.addDiscountOO_timeStart);
        tEnd = findViewById(R.id.addDiscountOO_timeEnd);
        btn_tStart = findViewById(R.id.timeStart_btnOO);
        btn_tEnd = findViewById(R.id.timeEnd_btnOO);
        btn_back = findViewById(R.id.addDiscountOO_back);
    }
}