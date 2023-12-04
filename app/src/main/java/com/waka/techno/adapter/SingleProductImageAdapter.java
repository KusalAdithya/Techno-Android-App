package com.waka.techno.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waka.techno.R;
import com.waka.techno.model.ProductImages;

import java.util.ArrayList;

public class SingleProductImageAdapter extends RecyclerView.Adapter<SingleProductImageAdapter.ViewHolder> {

    ArrayList<ProductImages> productImagesList;

    public SingleProductImageAdapter(ArrayList<ProductImages> productImagesList) {
        this.productImagesList = productImagesList;
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
        ProductImages productImages = productImagesList.get(position);
        holder.imageView.setImageResource(productImages.getImg());
    }

    @Override
    public int getItemCount() {
        return productImagesList.size();
    }
}
