package com.example.shop_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.product;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder> implements Filterable {
    List<product> list;
    List<product> list_temp;
    Context context;


    public productAdapter(List<product> list, Context context) {
        this.list = list;
        this.context = context;
        this.list_temp = list;
    }

    @NonNull
    @Override
    public productAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productAdapter.ViewHolder holder, int position) {
        final product product_temp = list.get(position);
        if (product_temp==null) {
            return;
        }
        String nameState = "";
        if(product_temp.isState()){
            nameState = product_temp.getName() +"\n Đang bán";
        }else{
            nameState = product_temp.getName() +"\n Đã khóa";
        }
        holder.name.setText(nameState);
        holder.cate.setText(product_temp.getCategory()+" - "+ product_temp.getStock());
        if(product_temp.getDiscountP()==0){
            holder.pri_stock.setText(product_temp.getPrice()+".VND");
        }else{
            SpannableString spannableString1 = new SpannableString(product_temp.getPrice()+".VND");
            spannableString1.setSpan(new StrikethroughSpan(), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            int finalprice = product_temp.getPrice() - (product_temp.getPrice()*product_temp.getDiscountP()/100);
            SpannableString spannableString2 = new SpannableString(finalprice+".VND");
            spannableString2.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            CharSequence combinedText = TextUtils.concat(spannableString1, " -> ", spannableString2);
            holder.pri_stock.setText(combinedText);
        }
        Glide.with(context).load(product_temp.getImage()).into(holder.product_view);
        holder.name.setOnClickListener(view -> pass(product_temp));
        holder.btn_import.setOnClickListener(view -> addToImportCart(product_temp));
        //holder.btn_demo.setOnClickListener(view -> addtoDemo(product_temp));
    }

    private void addtoDemo(product productTemp) {
        Intent addToDemo = new Intent(context, DemoVocher.class);
        Bundle bundle = new Bundle();
        //Log.d("product", productTemp.toString());
        bundle.putSerializable("product", productTemp);
        addToDemo.putExtras(bundle);
        context.startActivity(addToDemo);
    }

    private void addToImportCart(product productTemp) {
        Intent addToCart = new Intent(context, ImportProduct.class);
        Bundle bundle = new Bundle();
        //Log.d("product", productTemp.toString());
        bundle.putSerializable("product", productTemp);
        addToCart.putExtras(bundle);
        context.startActivity(addToCart);
    }

    private void pass(product productTemp) {
        Intent detail = new Intent(context, ProductDetail.class);
        Bundle bundle = new Bundle();
        //Log.d("product", productTemp.toString());
        bundle.putSerializable("product", productTemp);
        detail.putExtras(bundle);
        context.startActivity(detail);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if(search.trim().isEmpty()){
                    list = list_temp;
                }else {
                    List<product> list_search = new ArrayList<>();
                    for (product pr: list_temp){
                        if(pr.getName().toLowerCase().contains(search.toLowerCase())){
                            list_search.add(pr);
                        }
                    }
                    list = list_search;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_view;
        TextView name, cate, pri_stock;
        LinearLayout layoutItem;
        Chip btn_import, btn_demo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_view = itemView.findViewById(R.id.item_product_img);
            name = itemView.findViewById(R.id.item_product_name);
            cate = itemView.findViewById(R.id.item_product_category);
            pri_stock = itemView.findViewById(R.id.item_product_price_stock);
            layoutItem = itemView.findViewById(R.id.layout_item);
            btn_import = itemView.findViewById(R.id.item_import);
            btn_demo = itemView.findViewById(R.id.item_demo);
        }
    }
}
