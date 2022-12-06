package com.example.groceree.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceree.R;
import com.example.groceree.model.Product;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    List<Product> productList;
    public ProductListAdapter(List<Product> blogList){
        this.productList = blogList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(productList.get(position).getName());
        holder.price.setText(productList.get(position).getPrice());
        Glide.with(holder.itemView.getContext()).load(productList.get(position).getImageUrl()).into(holder.productImage);

    }
    @Override
    public int getItemCount() {
        if(productList!=null){
            return productList.size();
        }else
            return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView name;
        TextView price;
        ImageView productImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.tv_product_name);
            price = view.findViewById(R.id.tv_product_price);
            productImage = view.findViewById(R.id.iv_product_image);
        }
    }
}
