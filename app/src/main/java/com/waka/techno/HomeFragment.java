package com.waka.techno;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.waka.techno.adapter.CardAdapter;
import com.waka.techno.adapter.HomeCardAdapter;
import com.waka.techno.adapter.ImageAdapter;
import com.waka.techno.model.Product;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Search Bar Auto Complete Text View ------------------------------------------------------
//        AutoCompleteTextView textInputSearch = (AutoCompleteTextView) fragment.findViewById(R.id.textInputSearch);
//        String[] colors = {"Red", "Green", "Black",
//                "Orange", "Blue", "Pink",
//                "Blush", "Brown", "Yellow"};
//        ArrayAdapter<String> search = new ArrayAdapter<String>(requireContext(), R.layout.search_result_dialog, colors);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            textInputSearch.setThreshold(1);
//            textInputSearch.setAdapter(search);
//        }


        // Image Carousel --------------------------------------------------------------------------
        RecyclerView recyclerView = fragment.findViewById(R.id.image_recycler);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://pillayrgroup.com/wp-content/uploads/2020/10/Electronics-Banner-F.jpg");
        arrayList.add("https://pillayrgroup.com/wp-content/uploads/2020/10/IT-Banner-F.jpg");
        arrayList.add("https://assets.dragonmart.ae//pictures/0103296_DragonMart_categorylisting_computer&electronics_1of3.jpeg");
        arrayList.add("https://www.shopickr.com/wp-content/uploads/2015/10/amazon-india-electronics-sale-2015-banner1.jpg");

        ImageAdapter adapter = new ImageAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onClick(ImageView imageView, String path) {

            }
        });

        //Products View ----------------------------------------------------------------------------
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


        RecyclerView cardRecycle = fragment.findViewById(R.id.homeCardView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        cardRecycle.setLayoutManager(gridLayoutManager);
        HomeCardAdapter homeCardAdapter = new HomeCardAdapter(productArrayList, getContext());
        cardRecycle.setAdapter(homeCardAdapter);
        cardRecycle.setHasFixedSize(true);

    }
}