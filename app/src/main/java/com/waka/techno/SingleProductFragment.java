package com.waka.techno;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.waka.techno.adapter.SingleProductImageAdapter;
import com.waka.techno.model.Product;
import com.waka.techno.model.ProductImages;

import java.util.ArrayList;
import java.util.List;

public class SingleProductFragment extends Fragment {

    private String productName;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private TextView nameText, category, model, brand, description, price,qty;
    private ArrayList<String> productImageList;
    private RecyclerView recyclerView;
    private SingleProductImageAdapter singleProductImageAdapter;
    private Product product;
    private String msg;

    public SingleProductFragment(Product product) {
        this.product = product;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();

        nameText = fragment.findViewById(R.id.textView58);
        category = fragment.findViewById(R.id.textViewn64);
        model = fragment.findViewById(R.id.textView64);
        brand = fragment.findViewById(R.id.textView66);
        description = fragment.findViewById(R.id.textView60);
        price = fragment.findViewById(R.id.textView62);
        qty = fragment.findViewById(R.id.textQty);

        //load product data ------------------------------------------------------------------------
        nameText.setText(product.getName());
        category.setText(product.getCategory());
        model.setText(product.getModel());
        brand.setText(product.getBrand());
        description.setText(product.getDescription());
        qty.setText(String.valueOf(product.getQty())+" Items");
        price.setText("LKR " + product.getPrice() + "0");

        //Single Product Images --------------------------------------------------------------------
        productImageList = new ArrayList<>();

        for ( String imgList: product.getProductImage()) {
            productImageList.add(imgList);
        }

        recyclerView = fragment.findViewById(R.id.singleProductImgView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        singleProductImageAdapter = new SingleProductImageAdapter(productImageList, getContext());
        singleProductImageAdapter.getItemCount();

        TextView countView = fragment.findViewById(R.id.textViewItemCount);
        countView.setText("*Slide to View " + singleProductImageAdapter.getItemCount() + " Images");
        recyclerView.setAdapter(singleProductImageAdapter);

        // add to cart btn -------------------------------------------------------------------------
        fragment.findViewById(R.id.addCartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();

                // add to cart db
                FirebaseDatabase.getInstance().getReference("Cart").child(firebaseUser.getUid())
                        .child(product.getId()).setValue(currentTimeMillis)
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getContext(), "Product added to cart successfully.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Add to cart Fail. Try Again "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
            }
        });

        // add to wishlist btn ---------------------------------------------------------------------
        fragment.findViewById(R.id.addWishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add to cart db
                FirebaseDatabase.getInstance().getReference("Wishlist").child(firebaseUser.getUid())
                        .child(product.getName()).setValue(product.getName())
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getContext(), "Product added to wishlist successfully.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Add to wishlist Fail. Try Again "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
            }
        });

        // search user -----------------------------------------------------------------------------
        db.collection("users/"+firebaseUser.getUid()).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();
                        if (result.isEmpty()) {
                            Toast.makeText(getContext(), "Update your profile first!", Toast.LENGTH_LONG).show();

                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Buy now btn------------------------------------------------------------------------------
        fragment.findViewById(R.id.buyBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        msg = "Dear "+firebaseUser.getDisplayName()+"\n" +
                                "\n" +
                                "Thank you for choosing Techno! Your order has been successfully placed, and we're thrilled to be a part of your shopping experience.";


//                        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
//                            SmsManager smsManager = SmsManager.getDefault();
//                            smsManager.sendTextMessage(mobileNumber,null,message,null,null);
//                            Toast.makeText(getContext(),"SMS Sent Successfully.",Toast.LENGTH_LONG).show();
//                        }else {
//                            ActivityCompat.requestPermissions(getActivity(),new String[]{
//                                    Manifest.permission.SEND_SMS
//                            },100);
//                        }
                    }
                }
        );

    }
}