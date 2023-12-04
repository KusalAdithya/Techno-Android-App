package com.waka.techno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.waka.techno.R;
import com.waka.techno.model.Product;
import com.waka.techno.model.ProductImages;

import java.util.ArrayList;

public class SingleProductImageAdapter extends RecyclerView.Adapter<SingleProductImageAdapter.ViewHolder> {

    ArrayList<String> productImagesList;
    Context context;

    public SingleProductImageAdapter(ArrayList<String> productImagesList, Context context) {
        this.productImagesList = productImagesList;
        this.context = context;
    }

    public SingleProductImageAdapter() {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewSingleProduct);
        }
    }

    @NonNull
    @Override
    public SingleProductImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleProductImageAdapter.ViewHolder holder, int position) {
        String productImages = productImagesList.get(position);
        Glide.with(context).load(productImages).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productImagesList.size();
    }
}
