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

import com.waka.techno.adapter.CardAdapter;
import com.waka.techno.adapter.ProductViewHolder;
import com.waka.techno.model.Product;

import java.util.ArrayList;


public class CartFragment extends Fragment {

//    private  ArrayList<Product> productArrayList;

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

        //back Button
        fragment.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
//                fragment.findViewById(R.id.bottomNavHome).setSelected(true);
                
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
        ArrayList<String> productImageList = new ArrayList<>();
        productImageList.add("https://i.ebayimg.com/images/g/6l4AAOSwiadlBoUS/s-l1600.jpg");
        for (int i = 0; i < 5; i++) {
            productArrayList.add(new Product(
                    "Logitech - G305",
                    "Gaming Mouse",
                    100.00,
                    productImageList
            ));
        }

            RecyclerView recyclerView = fragment.findViewById(R.id.cartCardView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            RecyclerView.Adapter<ProductViewHolder> adapter = new CardAdapter(getContext(), productArrayList);


            if (productArrayList.isEmpty()){
                fragment.findViewById(R.id.emptyCartView).setVisibility(View.VISIBLE);
                fragment.findViewById(R.id.constraintLayoutn3).setVisibility(View.INVISIBLE);
                fragment.findViewById(R.id.constraintLayout6).setVisibility(View.INVISIBLE);
            }else {
                recyclerView.setAdapter(adapter);
            }

    }

}