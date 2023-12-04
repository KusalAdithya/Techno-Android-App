package com.waka.techno;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.waka.techno.adapter.SingleProductImageAdapter;
import com.waka.techno.model.ProductImages;

import java.util.ArrayList;

public class SingleProductFragment extends Fragment {

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

        //Single Product Images --------------------------------------------------------------------
        ArrayList<ProductImages> imagesArrayList = new ArrayList<>();
//        for (ProductImages img : imagesArrayList){
//            imagesArrayList.add(new ProductImages(
//                    R.drawable.logitech_mouse
//            ));
//        }

        for (int i = 0; i < 5; i++) {
            imagesArrayList.add(new ProductImages(
                    R.drawable.logitech_mouse
            ));
        }

        RecyclerView recyclerView = fragment.findViewById(R.id.singleProductImgView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
        RecyclerView.Adapter<SingleProductImageAdapter.ViewHolder> adapter = new SingleProductImageAdapter(imagesArrayList);
        adapter.getItemCount();

//        if (imagesArrayList.isEmpty()) {
//            fragment.findViewById(R.id.singleProductImgView).setVisibility(View.INVISIBLE);
//        } else {
        TextView countView = fragment.findViewById(R.id.textViewItemCount);
        countView.setText("*Slide to View "+adapter.getItemCount()+" Images");
        recyclerView.setAdapter(adapter);
//        }

    }
}