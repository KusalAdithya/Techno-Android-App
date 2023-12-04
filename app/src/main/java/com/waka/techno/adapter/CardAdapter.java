package com.waka.techno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.waka.techno.R;
import com.waka.techno.model.Product;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    Context context;
    private ArrayList<Product> productList;

    public CardAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View card = inflater.inflate(R.layout.cart_card,parent, false);
        return new ProductViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product item = productList.get(position);
        Glide.with(context).load(item.getProductImage().get(0)).into(holder.getProductImage());
        holder.getName().setText(item.getName());
        holder.getCategory().setText(item.getCategory());
        holder.getPrice().setText(String.valueOf("LKR "+item.getPrice()+0));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}


