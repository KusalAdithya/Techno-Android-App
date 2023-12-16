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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waka.techno.MainActivity;
import com.waka.techno.R;
import com.waka.techno.model.Product;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ProductViewHolder> {

    Context context;
    private ArrayList<Product> productList;
    Fragment fragment;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    public CardAdapter(Context context, ArrayList<Product> productList,Fragment fragment) {
        this.context = context;
        this.productList = productList;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public CardAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View card = inflater.inflate(R.layout.cart_card,parent, false);

        return new CardAdapter.ProductViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ProductViewHolder holder, int position) {

        Product item = productList.get(position);
        Glide.with(context).load(item.getProductImage().get(0)).into(holder.productImage);
        holder.name.setText(item.getName());
        holder.category.setText(item.getCategory());
        holder.price.setText(String.valueOf("LKR "+item.getPrice()+0));

        // qty btn ---------------------------------------------------------------------------------
        int currentQty = item.getQty();
        holder.miniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyText = Integer.parseInt((String)  holder.qtyView.getText());

                if (qtyText > 1) {
                    holder.qtyView.setText(String.valueOf(qtyText - 1));
                } else {
                    Toast.makeText(v.getContext(), "You can't go below 1", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyText = Integer.parseInt((String)  holder.qtyView.getText());

                if (qtyText < currentQty) {
                    holder.qtyView.setText(String.valueOf(qtyText + 1));
                } else {
                    Toast.makeText(v.getContext(), "Maximum quantity deserve", Toast.LENGTH_LONG).show();
                }
            }
        });


        // delete btn ------------------------------------------------------------------------------
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                String pId = item.getId();
                String uid = firebaseUser.getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(uid);

                databaseReference.child(pId).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                        Toast.makeText(v.getContext(), "Deleted..", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImage;
        public TextView name, category, price,qtyView;
        public ImageButton deleteBtn,miniBtn, plusBtn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productNameText);
            category = itemView.findViewById(R.id.categoryText);
            price = itemView.findViewById(R.id.priceText);
            deleteBtn=itemView.findViewById(R.id.imageButton7);
            qtyView=itemView.findViewById(R.id.textView24);
            miniBtn=itemView.findViewById(R.id.imageButton6);
            plusBtn=itemView.findViewById(R.id.imageButton5);

        }
    }
}


