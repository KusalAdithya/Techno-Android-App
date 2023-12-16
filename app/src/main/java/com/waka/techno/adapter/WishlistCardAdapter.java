package com.waka.techno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waka.techno.R;
import com.waka.techno.WishlistFragment;
import com.waka.techno.model.Product;

import java.util.ArrayList;

public class WishlistCardAdapter extends RecyclerView.Adapter<WishlistCardAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;
    Fragment fragment;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    public WishlistCardAdapter(Context context, ArrayList<Product> productArrayList, Fragment fragment) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public WishlistCardAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View card = inflater.inflate(R.layout.wishlist_card, parent, false);
        return new WishlistCardAdapter.ProductViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistCardAdapter.ProductViewHolder holder, int position) {
        Product item = productArrayList.get(position);
        Glide.with(context).load(item.getProductImage().get(0)).into(holder.productImage);
        holder.name.setText(item.getName());
        holder.category.setText(item.getCategory());
        holder.price.setText(String.valueOf("LKR " + item.getPrice() + 0));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Delete btn ------------------------------------------------------------------------------
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pId = item.getId();
                String uid = firebaseUser.getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference("Wishlist").child(uid);

                databaseReference.child(pId).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                        Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

        // Add to cart btn -------------------------------------------------------------------------
        long currentTimeMillis = System.currentTimeMillis();
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add to cart db
                FirebaseDatabase.getInstance().getReference("Cart").child(firebaseUser.getUid())
                        .child(item.getId()).setValue(currentTimeMillis)
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(fragment.getContext(), "Product added to cart successfully.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(fragment.getContext(), "Add to cart Fail. Try Again " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
            }
        });


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImage;
        public TextView name, category, price;
        public ImageButton deleteBtn;
        public Button addToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productNameText);
            category = itemView.findViewById(R.id.categoryText);
            price = itemView.findViewById(R.id.priceText);
            deleteBtn = itemView.findViewById(R.id.imageButton7);
            addToCart = itemView.findViewById(R.id.imageButton6);
        }
    }

    public void loadFragment(Fragment newFragment) {
        FragmentManager supportFragmentManager = fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.commit();
    }
}
