package com.waka.techno.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waka.techno.R;
import com.waka.techno.model.Product;

import java.util.ArrayList;

public class WishlistCardAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    ArrayList<Product> productArrayList = new ArrayList<>();

    public WishlistCardAdapter(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View card = inflater.inflate(R.layout.wishlist_card, parent, false);
        return new ProductViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product item = productArrayList.get(position);
        holder.getProductImage().setImageResource(item.getProductImage());
        holder.getName().setText(item.getName());
        holder.getCategory().setText(item.getCategory());
        holder.getPrice().setText(String.valueOf("LKR "+item.getPrice()+0));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
}
