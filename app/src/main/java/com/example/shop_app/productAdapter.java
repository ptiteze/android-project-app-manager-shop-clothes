package com.example.shop_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        holder.name.setText(product_temp.getName());
        holder.cate.setText(product_temp.getCategory());
        holder.pri_stock.setText(product_temp.getPrice()+".VND - "+ product_temp.getStock());
        Glide.with(context).load(product_temp.getImage()).into(holder.product_view);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass(product_temp);
            }
        });
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_view = itemView.findViewById(R.id.item_product_img);
            name = itemView.findViewById(R.id.item_product_name);
            cate = itemView.findViewById(R.id.item_product_category);
            pri_stock = itemView.findViewById(R.id.item_product_price_stock);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }
}
