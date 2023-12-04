package com.waka.techno;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waka.techno.adapter.OrderAdapter;
import com.waka.techno.model.Order;
import com.waka.techno.model.Product;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        //Products
        ArrayList<Product> productArrayList = new ArrayList<>();
//        productArrayList.add(new Product(
//                R.drawable.logitech_mouse,
//                "Logitech - G305",
//                "Gaming Mouse",
//                100.00
//        ));

//        Product product = new Product(
//                R.drawable.logitech_mouse,
//                "Logitech - G305",
//                "Gaming Mouse",
//                100.00
//        );
//        String date = "22:00 23-12-2023";
//        int qty= 3;

        ArrayList<Order> orderArrayList = new ArrayList<>();
        ArrayList<String> productImageList = new ArrayList<>();
        productImageList.add("https://i.ebayimg.com/images/g/6l4AAOSwiadlBoUS/s-l1600.jpg");
        for (int i = 0; i < 5; i++) {
            orderArrayList.add(new Order(
                    new Product(
                            "Logitech - G305",
                            "Gaming Mouse",
                            100.00,
                            productImageList
                    ),
                    "22:00 23-12-2023",
                    2
            ));
        }

        RecyclerView recyclerView = fragment.findViewById(R.id.orderCardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter<OrderAdapter.ViewHolder> adapter = new OrderAdapter(getContext(),orderArrayList);

        if (orderArrayList.isEmpty()) {
            fragment.findViewById(R.id.emptyOderView).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.constraintLayoutn3).setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setAdapter(adapter);
        }
    }
}