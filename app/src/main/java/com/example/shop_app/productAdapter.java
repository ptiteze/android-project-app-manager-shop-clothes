package com.example.shop_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.product;

import java.util.ArrayList;
import java.util.List;

public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder> {
    List<product> list;
    Context context;
    public productAdapter(List<product> list, Context context) {
        this.list = list;
        this.context = context;
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
        product product_temp = list.get(position);
        if (product_temp==null) {
            return;
        }
        holder.name.setText(product_temp.getName());
        holder.cate.setText(product_temp.getCategory());
        holder.pri_stock.setText(product_temp.getPrice()+".VND - "+ product_temp.getStock());
        Glide.with(context).load(product_temp.getImage()).into(holder.product_view);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_view;
        TextView name, cate, pri_stock;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_view = itemView.findViewById(R.id.item_product_img);
            name = itemView.findViewById(R.id.item_product_name);
            cate = itemView.findViewById(R.id.item_product_category);
            pri_stock = itemView.findViewById(R.id.item_product_price_stock);
        }
    }
}
