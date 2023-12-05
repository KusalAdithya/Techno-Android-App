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

public class CardAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    Context context;
    private ArrayList<Product> productList;
 View card;

    public CardAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        card = inflater.inflate(R.layout.cart_card,parent, false);

//        name = card.findViewById(R.id.productNameText);
        return new ProductViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product item = productList.get(position);
        Glide.with(context).load(item.getProductImage().get(0)).into(holder.getProductImage());
        holder.getName().setText(item.getName());
        holder.getCategory().setText(item.getCategory());
        holder.getPrice().setText(String.valueOf("LKR "+item.getPrice()+0));
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
}


