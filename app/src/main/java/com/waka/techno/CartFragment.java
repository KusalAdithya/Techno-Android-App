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
import android.widget.TextView;
import android.widget.Toast;

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

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private View emptyView, layout, layout2;
    private TextView items, subTotal, delivery, total;

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

        items = fragment.findViewById(R.id.textView8);
        subTotal = fragment.findViewById(R.id.textView13);
        delivery = fragment.findViewById(R.id.textView15);
        total = fragment.findViewById(R.id.textView17);

        Product product;

        //back Button ------------------------------------------------------------------------------
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

        int itemSize;

        // load from db ----------------------------------------------------------------------------
        firebaseDatabase.getReference("Cart/" + firebaseUser.getUid()).orderByValue()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getChildrenCount() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.INVISIBLE);
                            layout2.setVisibility(View.INVISIBLE);

                        } else {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                list.add(snapshot.getKey());
                            }
                            Collections.reverse(list);

                            final int[] sub = new int[1];
                            for (String pId : list) {
                                DatabaseReference reference = firebaseDatabase.getReference("Products");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        double sub1 = 0;
                                        for (DataSnapshot data : snapshot.getChildren()) {

                                            Product product = data.getValue(Product.class);
//                                            String price="";
                                            if (product.getId().equals(pId)) {
                                                productArrayList.add(product);
//                                               price = String.valueOf(product.getPrice());
//sub[0] = (int) product.getPrice();
                                                sub1 = sub1 + product.getPrice();
                                            }

                                            subTotal.setText("LKR " + (sub1) + "0");
                                        }

                                        // Load items count ----------------------------------------
                                        items.setText(productArrayList.size() + " Items");

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

                        // Delivery total ----------------------------------------------------------
                        int resultDelivery = 250 * Integer.valueOf((int) dataSnapshot.getChildrenCount());
                        delivery.setText("LKR " + resultDelivery + ".00");

                        // total --------------------------------------------------------------------------
                        total.setText("LKR " + (500 + resultDelivery) + ".00");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Connection Failed. Try Again Later", Toast.LENGTH_LONG).show();
                    }
                });

        // Ckeckout btn ----------------------------------------------------------------------------
        fragment.findViewById(R.id.button2);

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

}