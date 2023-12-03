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

import com.waka.techno.adapter.ProductViewHolder;
import com.waka.techno.adapter.WishlistCardAdapter;
import com.waka.techno.model.Product;

import java.util.ArrayList;

public class WishlistFragment extends Fragment {

    public WishlistFragment() {
        // Required empty public constructor
    }

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

        //back Button
        fragment.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
            }

            public void loadFragment(Fragment fragment) {
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }
        });

        //Products
        ArrayList<Product> productArrayList = new ArrayList<>();
        productArrayList.add(new Product(
                R.drawable.logitech_mouse,
                "Logitech - G305",
                "Gaming Mouse",
                100.00
        ));

        productArrayList.add(new Product(
                R.drawable.logitech_mouse,
                "Logitech - G305",
                "Gaming Mouse",
                100.00
        ));

        productArrayList.add(new Product(
                R.drawable.logitech_mouse,
                "Logitech - G305",
                "Gaming Mouse",
                100.00
        ));
        productArrayList.add(new Product(
                R.drawable.logitech_mouse,
                "Logitech - G305",
                "Gaming Mouse",
                100.00
        ));
        productArrayList.add(new Product(
                R.drawable.logitech_mouse,
                "Logitech - G305",
                "Gaming Mouse",
                100.00
        ));
        productArrayList.add(new Product(
                R.drawable.logitech_mouse,
                "Logitech - G305",
                "Gaming Mouse",
                100.00
        ));

        RecyclerView recyclerView=fragment.findViewById(R.id.wishlistCardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter<ProductViewHolder> adapter = new WishlistCardAdapter(productArrayList);

        if (productArrayList.isEmpty()){
            fragment.findViewById(R.id.emptyWishlistView).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.constraintLayoutn3).setVisibility(View.INVISIBLE);
        }else {
            recyclerView.setAdapter(adapter);
        }

    }
}