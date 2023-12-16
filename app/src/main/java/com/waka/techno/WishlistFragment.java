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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waka.techno.adapter.ProductViewHolder;
import com.waka.techno.adapter.WishlistCardAdapter;
import com.waka.techno.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WishlistFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private View emptyView, layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        emptyView = fragment.findViewById(R.id.emptyWishlistView);
        layout = fragment.findViewById(R.id.constraintLayoutn3);

        //back Button
        fragment.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
            }


        });

        //Products ---------------------------------------------------------------------------------
        ArrayList<Product> productArrayList = new ArrayList<>();

        RecyclerView recyclerView=fragment.findViewById(R.id.wishlistCardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        WishlistCardAdapter adapter = new WishlistCardAdapter(getContext(),productArrayList,WishlistFragment.this);
        List<String> list = new ArrayList<>();

//        if (productArrayList.isEmpty()){
//            fragment.findViewById(R.id.emptyWishlistView).setVisibility(View.VISIBLE);
//            fragment.findViewById(R.id.constraintLayoutn3).setVisibility(View.INVISIBLE);
//        }else {
//            recyclerView.setAdapter(adapter);
//        }

        // load from db ----------------------------------------------------------------------------
        firebaseDatabase.getReference("Wishlist/" + firebaseUser.getUid()).orderByValue()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getChildrenCount() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.INVISIBLE);

                        } else {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                list.add(snapshot.getKey());
                            }
                            Collections.reverse(list);

                            for (String pId : list) {
                                DatabaseReference reference = firebaseDatabase.getReference("Products");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot data : snapshot.getChildren()) {

                                            Product product = data.getValue(Product.class);
//                                            String price="";
                                            if (product.getId().equals(pId)) {
                                                productArrayList.add(product);
//                                               price = String.valueOf(product.getPrice());

                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getContext(), "Connection Failed. Try Again Later", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            list.clear();
                            productArrayList.clear();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Connection Failed. Try Again Later", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}