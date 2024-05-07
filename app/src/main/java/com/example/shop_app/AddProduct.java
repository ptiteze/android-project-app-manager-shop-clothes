package com.example.shop_app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.*;

import com.bumptech.glide.Glide;
import com.example.API.ApiService;
import com.example.model.category;
import com.example.model.obj_imgur;
import com.example.model.product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends AppCompatActivity {
    public static final String CLIENT_ID = "ace5fa66141a632";
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    List<String> list_category = new ArrayList<>();
    List<String> list_namePR = new ArrayList<>();
    Chip chip_add, chip_cancel,chip_remove, chip_M, chip_L, chip_XL, chip_XXL;
    Drawable drawable, drawable_update;
    Spinner spinner_category;
    ImageButton btn_back;
    ImageView addProduct_img;
    EditText name, description, color, material, origin,
            price,
            improtPrice;
    TextView title;
    boolean updateState = false, update_img = false;
    private String product_nextID = "SP";
    Map<String, Integer> size = new HashMap<>();
    product pr;
    private Uri muri = null;
    ArrayAdapter<String> adapter_cate;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    Log.e(AddProduct.class.getName(), "onActivityResult");
                    if(o.getResultCode() == Activity.RESULT_OK){
                        Intent data = o.getData();
                        if(data == null){
                            return;
                        }
                        Uri uri = data.getData();
                        muri = uri;
                        try{
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            addProduct_img.setImageBitmap(bitmap);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        setControl();
        setData();
        setEvent();
    }

    private void setControl() {
        title = findViewById(R.id.addProduct_title);
        chip_add = findViewById(R.id.addproduct_complete);
        chip_cancel = findViewById(R.id.addproduct_cancel);
        chip_remove = findViewById(R.id.addproduct_remove);
        spinner_category = findViewById(R.id.add_product_category);
        btn_back = findViewById(R.id.addProduct_back);
        addProduct_img = findViewById(R.id.addproduct_img);
        name = findViewById(R.id.addProduct_name);
        description = findViewById(R.id.addProduct_description);
        color = findViewById(R.id.addProduct_color);
        material = findViewById(R.id.addProduct_material);
        origin = findViewById(R.id.addProduct_origin);
        price = findViewById(R.id.addProduct_price);
        improtPrice = findViewById(R.id.addProduct_importPrice);
        chip_M = findViewById(R.id.addProduct_M);
        chip_L = findViewById(R.id.addProduct_L);
        chip_XL = findViewById(R.id.addProduct_XL);
        chip_XXL = findViewById(R.id.addProduct_XXL);
        drawable = addProduct_img.getDrawable();
    }
    private void showData() {
        adapter_cate = new ArrayAdapter<>(AddProduct.this, android.R.layout.simple_spinner_item, list_category);
        adapter_cate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter_cate);
        if(updateState){
            int index = list_category.indexOf(pr.getCategory());
            spinner_category.setSelection(index);
        }
    }
    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            updateState = false;
        }else{
            pr =  (product) bundle.get("product");
            assert pr != null;
            updateState = true;
        }
        database.child("category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                category cate  = snapshot.getValue(category.class);
                if(cate!=null){
                    list_category.add(cate.getName());
                }
                showData();
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
                throw error.toException();
            }
        });
        Query query_product = database.child("product");
        query_product.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //list_namePR.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    product pr = dataSnapshot.getValue(product.class);
                    if(pr!=null)
                        list_namePR.add(pr.getName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
        if(updateState) changePrUpdate();
    }
    private void changePrUpdate() {
        name.setText(pr.getName());
        Glide.with(AddProduct.this).load(pr.getImage()).into(addProduct_img);
        description.setText(pr.getDescription());
        color.setText(pr.getColor());
        material.setText(pr.getMaterial());
        origin.setText(pr.getOrigin());
        price.setText(String.valueOf(pr.getPrice()));
        improtPrice.setText(String.valueOf(pr.getImportPrice()));
        if(pr.isState()){
            chip_remove.setText("Un lock");
        }
        database.child("productSize/"+pr.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    int value = childSnapshot.getValue(Integer.class);
                    size.put(key, value);
                }
                setSize();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        title.setText("UPDATE PRODUCT");
        chip_add.setText("Update Product");
        chip_remove.setEnabled(true);
        chip_remove.setVisibility(View.VISIBLE);
        drawable_update = addProduct_img.getDrawable();
    }

    private void setSize() {
        for (String s: size.keySet()) {
            switch (s){
                case "M":
                    chip_M.setChecked(true);
                    chip_M.setEnabled(false);
                    break;
                case "L":
                    chip_L.setChecked(true);
                    chip_L.setEnabled(false);
                    break;
                case "XL":
                    chip_XL.setChecked(true);
                    chip_XL.setEnabled(false);
                    break;
                case "XXL":
                    chip_XXL.setChecked(true);
                    chip_XXL.setEnabled(false);
                    break;
            }
        }
    }
    private void setEvent() {
    btn_back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    addProduct_img.setOnClickListener(v -> onClickRequestPermission());
    chip_add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (check()){
                if(updateState){
                    if(muri==null){
                        updateDataNotImg();
                    }else{
                        callApiRegister();
                    }
                }else{
                    Log.d("update image: ", "false false");
                    callApiRegister();
                    //Toast.makeText(AddProduct.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            }

        }
    });
    chip_remove.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(pr.isState()){
                pr.setState(false);
                chip_remove.setText("Un Lock");
                Toast.makeText(AddProduct.this, "Đã khóa sản phẩm", Toast.LENGTH_SHORT).show();
            }else{
                pr.setState(true);
                chip_remove.setText("Lock");
                Toast.makeText(AddProduct.this, "Đã mở sản phẩm", Toast.LENGTH_SHORT).show();
            }
        }
    });
    chip_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearInput();
            Toast.makeText(AddProduct.this, "Đã hủy và làm mới", Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void updateDataNotImg() {
        String pid = pr.getId();
        String pname = name.getText().toString().trim();
        String pcate = spinner_category.getSelectedItem().toString();
        String pmaterial = material.getText().toString().trim();
        String porigin = origin.getText().toString().trim();
        String pdes = description.getText().toString().trim();
        String pcolor = color.getText().toString().trim();
        int pprice = Integer.valueOf(price.getText().toString());
        int pImportPrice = Integer.valueOf(improtPrice.getText().toString());
        if (chip_M.isChecked()&&chip_M.isEnabled()) size.put("M",0);
        if (chip_L.isChecked()&&chip_L.isEnabled()) size.put("L",0);
        if (chip_XL.isChecked()&&chip_XL.isEnabled()) size.put("XL",0);
        if (chip_XXL.isChecked()&&chip_XXL.isEnabled()) size.put("XXL",0);
        database.child("productSize").child(pid).setValue(size);
        product product_update = new product(pid,pname,pcate,pmaterial,porigin,pdes,pcolor
                ,pprice,pImportPrice,0,pr.getStock(),pr.getImage(),pr.isState());
        database.child("product").child(pid).setValue(product_update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pr.setName(pname);
                    pr.setCategory(pcate);
                    pr.setMaterial(pmaterial);
                    pr.setOrigin(porigin);
                    pr.setDescription(pdes);
                    pr.setColor(pcolor);
                    pr.setPrice(pprice);
                    pr.setImportPrice(pImportPrice);
                    clearInput();
                    Toast.makeText(AddProduct.this, "Update sản phẩm: "+pname+" thành công!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddProduct.this, "Update sản phẩm: "+pname+" không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearInput() {
        if(updateState){
            changePrUpdate();
        }else {
            spinner_category.setSelection(0);
            addProduct_img.setImageDrawable(drawable);
            name.setText("");
            description.setText("");
            color.setText("");
            material.setText("");
            origin.setText("");
            price.setText("");
            improtPrice.setText("");
            chip_M.setChecked(false);
            chip_L.setChecked(false);
            chip_XL.setChecked(false);
            chip_XXL.setChecked(false);
        }
    }


    private boolean check() {
         //@SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getDrawable(R.drawable.img_add);
        if(addProduct_img.getDrawable()==drawable&&!updateState){
            Toast.makeText(AddProduct.this, "Chưa nhập ảnh", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!updateState && list_namePR.contains(name.getText().toString().trim())){
            name.setError("Tên sản phẩm bị trùng");
            name.requestFocus();
            return false;
        }
        if(updateState){
            List<String> listTemp =  new ArrayList<>(list_namePR);
            listTemp.remove(pr.getName());
            if( listTemp.contains(name.getText().toString().trim())){
                name.setError("Tên sản phẩm bị trùng");
                name.requestFocus();
                return false;
            }
            listTemp=null;
        }
        if(name.getText().toString().trim().isEmpty()){
            name.setError("Chưa nhâp tên sản phẩm");
            name.requestFocus();
            return false;
        }
        if(name.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            name.setError("Tên sản phâm Không được chứa kí tự đặc biệt");
            name.requestFocus();
            return false;
        }
        if(description.getText().toString().trim().isEmpty()){
            description.setError("Chưa nhâp thông tin sản phẩm");
            description.requestFocus();
            return false;
        }
        if(description.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            description.setError("Thông tin sản phẩm Không được chứa kí tự đặc biệt");
            description.requestFocus();
            return false;
        }
        if(color.getText().toString().trim().isEmpty()){
            color.setError("Chưa nhâp màu sắc");
            color.requestFocus();
            return false;
        }
        if(color.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            color.setError("Phần màu sắc Không được chứa kí tự đặc biệt");
            color.requestFocus();
            return false;
        }
        if(origin.getText().toString().trim().isEmpty()){
            origin.setError("Chưa nhâp nguồn gốc sản phẩm");
            origin.requestFocus();
            return false;
        }
        if(origin.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            origin.setError("Nguồn gốc sản phẩm Không được chứa kí tự đặc biệt");
            origin.requestFocus();
            return false;
        }
        if(material.getText().toString().trim().isEmpty()){
            material.setError("Chưa nhâp chất liệu sản phẩm");
            material.requestFocus();
            return false;
        }
        if(material.getText().toString().toLowerCase().trim().matches(".*[^a-zA-Z 0-9âăưêôơàằầèềìòồờùừỳáắấéếíóốớúứýãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵảẳẩẻểỉỏổởủửỷđ].*")){
            material.setError("CHất liệu sản phẩm Không được chứa kí tự đặc biệt");
            material.requestFocus();
            return false;
        }
        if(price.getText().toString().trim().equals("")){
            price.setError("Chưa nhập giá bán sản phẩm");
            price.requestFocus();
            return false;
        }
        if(improtPrice.getText().toString().trim().equals("")){
            improtPrice.setError("Chưa nhập giá bán sản phẩm");
            improtPrice.requestFocus();
            return false;
        }
        if((!(chip_M.isChecked()||chip_L.isChecked()||chip_XL.isChecked()||chip_XXL.isChecked()))&&!updateState){
            Toast.makeText(AddProduct.this, "Phải chọn tôi thiểu một loại kích cỡ cho sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,5);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
    private void callApiRegister() {
        //progressBar.setVisibility(View.VISIBLE);
        String strRealPath = RealPathUtil.getRealPath(AddProduct.this,muri);
        File imageFile = new File(strRealPath);
        Log.d("mUri",imageFile.toString());
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "file");
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), "anh upload to imgur");
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), "nhập từ app");

        ApiService.apiService.registerImg("Client-ID " + CLIENT_ID, imagePart, type, titleBody, descriptionBody).enqueue(new Callback<obj_imgur>() {
            @Override
            public void onResponse(Call<obj_imgur> call, Response<obj_imgur> response) {
                //progressBar.setVisibility(View.GONE);
                obj_imgur objImgur = response.body();
                if(objImgur != null){
                    Log.d("linkImage",objImgur.getLink());
                    if(updateState){
                        updateData(objImgur);
                    }else{
                        addData(objImgur);
                    }
                    //Toast.makeText(AddProduct.this, "call API xong ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddProduct.this, "call API xong, không lấy về đc ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<obj_imgur> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(AddProduct.this, "Call API ngu "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("APIngu",t.getMessage());
            }
        });
    }

    private void updateData(obj_imgur objImgur) {
        String pid = pr.getId();
        String pname = name.getText().toString().trim();
        String pcate = spinner_category.getSelectedItem().toString();
        String pmaterial = material.getText().toString().trim();
        String porigin = origin.getText().toString().trim();
        String pdes = description.getText().toString().trim();
        String pcolor = color.getText().toString().trim();
        int pprice = Integer.valueOf(price.getText().toString());
        int pImportPrice = Integer.valueOf(improtPrice.getText().toString());
        if (chip_M.isChecked()&&chip_M.isEnabled()) size.put("M",0);
        if (chip_L.isChecked()&&chip_L.isEnabled()) size.put("L",0);
        if (chip_XL.isChecked()&&chip_XL.isEnabled()) size.put("XL",0);
        if (chip_XXL.isChecked()&&chip_XXL.isEnabled()) size.put("XXL",0);
        database.child("productSize").child(pid).setValue(size);
        product product_update = new product(pid,pname,pcate,pmaterial,porigin,pdes,pcolor
                ,pprice,pImportPrice,0,pr.getStock(),objImgur.getLink(),pr.isState());
        database.child("product").child(pid).setValue(product_update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pr.setName(pname);
                    pr.setCategory(pcate);
                    pr.setMaterial(pmaterial);
                    pr.setOrigin(porigin);
                    pr.setDescription(pdes);
                    pr.setColor(pcolor);
                    pr.setPrice(pprice);
                    pr.setImage(objImgur.getLink());
                    clearInput();
                    Toast.makeText(AddProduct.this, "Update sản phẩm: "+pname+" thành công!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddProduct.this, "Update sản phẩm: "+pname+" không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addData(obj_imgur objImgur) {
        String pname = name.getText().toString().trim();
        String pcate = spinner_category.getSelectedItem().toString();
        String pmaterial = material.getText().toString().trim();
        String porigin = origin.getText().toString().trim();
        String pdes = description.getText().toString().trim();
        String pcolor = color.getText().toString().trim();
        int pprice = Integer.valueOf(price.getText().toString());
        int pImportPrice = Integer.valueOf(improtPrice.getText().toString());
        Map<String,Integer> size_stock = new TreeMap<>();
        if (chip_M.isChecked()) size_stock.put("M",0);
        if (chip_L.isChecked()) size_stock.put("L",0);
        if (chip_XL.isChecked()) size_stock.put("XL",0);
        if (chip_XXL.isChecked()) size_stock.put("XXL",0);
        product_nextID = database.child("product").push().getKey();
        database.child("productSize").child(product_nextID).setValue(size_stock);
        product product_add = new product(product_nextID,pname,pcate,pmaterial,porigin,pdes,pcolor
                ,pprice,pImportPrice,0,0,objImgur.getLink(),true);
        database.child("product").child(product_nextID).setValue(product_add).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    clearInput();
                    Toast.makeText(AddProduct.this, "Thêm sản phẩm: "+pname+" thành công!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddProduct.this, "Thêm sản phẩm: "+pname+" không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}