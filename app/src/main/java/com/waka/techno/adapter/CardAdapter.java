package com.waka.techno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.waka.techno.MainActivity;
import com.waka.techno.R;
import com.waka.techno.model.Product;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ProductViewHolder> {

    Context context;
    private ArrayList<Product> productList;
    Fragment fragment;
 View card;

    public CardAdapter(Context context, ArrayList<Product> productList,Fragment fragment) {
        this.context = context;
        this.productList = productList;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public CardAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        card = inflater.inflate(R.layout.cart_card,parent, false);

//        name = card.findViewById(R.id.productNameText);
        return new CardAdapter.ProductViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ProductViewHolder holder, int position) {

        Product item = productList.get(position);
        Glide.with(context).load(item.getProductImage().get(0)).into(holder.productImage);
        holder.name.setText(item.getName());
        holder.category.setText(item.getCategory());
        holder.price.setText(String.valueOf("LKR "+item.getPrice()+0));
//        TextView name = card.findViewById(R.id.productNameText);

//        View view=LayoutInflater.from(MainActivity).inflater.inflate(R.layout.cart_card,parent, false);

//        card.findViewById(R.id.imageButton7).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, String.valueOf(name), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    public void cart(){

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImage;
        public TextView name, category, price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productNameText);
            category = itemView.findViewById(R.id.categoryText);
            price = itemView.findViewById(R.id.priceText);

        }
    }
}


