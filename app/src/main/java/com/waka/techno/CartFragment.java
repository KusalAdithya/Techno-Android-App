package com.waka.techno;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.waka.techno.adapter.CardAdapter;
import com.waka.techno.adapter.ProductViewHolder;
import com.waka.techno.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.internal.cache.DiskLruCache;


public class CartFragment extends Fragment {

    //    private  ArrayList<Product> productArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private View emptyView, layout, layout2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        emptyView = fragment.findViewById(R.id.emptyCartView);
        layout = fragment.findViewById(R.id.constraintLayoutn3);
        layout2 = fragment.findViewById(R.id.constraintLayout6);

        //back Button
        fragment.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
//                fragment.findViewById(R.id.bottomNavHome).setSelected(true);

            }


        });

        // Products --------------------------------------------------------------------------------
        ArrayList<Product> productArrayList = new ArrayList<>();

        RecyclerView recyclerView = fragment.findViewById(R.id.cartCardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CardAdapter adapter = new CardAdapter(getContext(), productArrayList, CartFragment.this);
        List<String> list = new ArrayList<>();


        // load from db ------------------------------------------------------------------------
        firebaseDatabase.getReference("Cart/" + firebaseUser.getUid()).orderByValue()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                list.add(snapshot.getKey());
                            }
                            Collections.reverse(list);

                            for (String name : list) {
                                DatabaseReference reference = firebaseDatabase.getReference("Products");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            Product product = data.getValue(Product.class);

                                            if (product.getName().equals(name)) {
                                                productArrayList.add(product);
                                            }

                                        }
                                        adapter.notifyDataSetChanged();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        } else {
                            emptyView.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.INVISIBLE);
                            layout2.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//        if (!productArrayList.isEmpty()) {
        recyclerView.setAdapter(adapter);

//        } else {
//            emptyView.setVisibility(View.VISIBLE);
//            layout.setVisibility(View.INVISIBLE);
//            layout2.setVisibility(View.INVISIBLE);
//        }

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

}