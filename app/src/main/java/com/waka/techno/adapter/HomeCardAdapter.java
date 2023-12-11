package com.waka.techno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.waka.techno.HomeFragment;
import com.waka.techno.R;
import com.waka.techno.SignupFragment;
import com.waka.techno.SingleProductFragment;
import com.waka.techno.model.Product;

import java.util.ArrayList;
import java.util.Objects;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.ProductViewHolder> {

    private ArrayList<Product> productList;
    Context context;
    private Fragment fragment;

    public HomeCardAdapter(ArrayList<Product> productList, Context context, Fragment fragment) {
        this.productList = productList;
        this.context = context;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public HomeCardAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View card = inflater.inflate(R.layout.home_card,parent, false);
        return new ProductViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCardAdapter.ProductViewHolder holder, int position) {
        Product item = productList.get(position);
        Glide.with(context).load(item.getProductImage().get(0)).into(holder.productImage);
        holder.name.setText(item.getName());
        holder.category.setText(item.getCategory());
        holder.price.setText(String.valueOf("LKR "+item.getPrice()+0));

     // product image click ------------------------------------------------------------------------
        holder.productImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadFragment(new SingleProductFragment(item));
                    }
                }
        );
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

    public void loadFragment(Fragment newFragment) {
        FragmentManager supportFragmentManager = fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.commit();
    }
}
