package com.waka.techno;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waka.techno.adapter.CategorySlideAdapter;
import com.waka.techno.adapter.HomeCardAdapter;
import com.waka.techno.model.Product;
import com.waka.techno.model.Tag;

import java.util.ArrayList;
import java.util.Objects;

public class AllProductsFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    ArrayList<Tag> tagArrayList;
    String[] tagsName;
    ArrayList<Product> productArrayList;
    private HomeCardAdapter homeCardAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        firebaseDatabase=FirebaseDatabase.getInstance();
         productArrayList = new ArrayList<>();

        //back Button ------------------------------------------------------------------------------
        fragment.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
                requireActivity().findViewById(R.id.bottomNavHome).setSelected(true);
            }
            public void loadFragment(Fragment fragment) {
                FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }
        });

        //Products View ----------------------------------------------------------------------------
        // load products data ----------------------------------------------------------------------
        firebaseDatabase.getReference("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    productArrayList.add(product);
                }
                homeCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Db data load fail", Toast.LENGTH_LONG).show();
            }
        });

        RecyclerView cardRecycle = fragment.findViewById(R.id.allCardView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        cardRecycle.setLayoutManager(gridLayoutManager);
         homeCardAdapter = new HomeCardAdapter(productArrayList, getContext());
        cardRecycle.setAdapter(homeCardAdapter);
        cardRecycle.setHasFixedSize(true);


        //Search Bar Auto Complete Text View -------------------------------------------------------
        AutoCompleteTextView textInputSearch = (AutoCompleteTextView) fragment.findViewById(R.id.textInputSearch);
        ArrayList<String> productName =new ArrayList<>();
        String[] colors = new String[0];
        System.out.println(productArrayList);
        for (Product data :productArrayList){
//           productName.add(data.getName());
            colors = new String[]{data.getName()+","};
            System.out.println(data.getName());
        }

//        String[] colors = {"Red", "Green", "Black",
//                "Orange", "Blue", "Pink",
//                "Blush", "Brown", "Yellow"};
        ArrayAdapter<String> search = new ArrayAdapter<String>(requireContext(), R.layout.search_result_dialog, colors);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textInputSearch.setThreshold(1);
            textInputSearch.setAdapter(search);
        }

        //Filter btn -------------------------------------------------------------------------------
        fragment.findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.findViewById(R.id.filterSpinner).performClick();
            }
        });

        //Category Slider --------------------------------------------------------------------------
        categoryInitialized();
        RecyclerView categoryRecycle = fragment.findViewById(R.id.categorySliderView);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecycle.setLayoutManager(horizontalLayoutManager);
        categoryRecycle.setHasFixedSize(true);
        CategorySlideAdapter categorySlideAdapter = new CategorySlideAdapter(getContext(), tagArrayList);
        categoryRecycle.setAdapter(categorySlideAdapter);


    }

    // For Category Slider -------------------------------------------------------------------------
    private void categoryInitialized() {

        tagArrayList = new ArrayList<>();

        tagsName = new String[]{
                "All",
                "SmartPhones",
                "Laptops",
                "Computers",
                "Smart Watches",
        };

        for (String s : tagsName) {

            Tag tags = new Tag(s);
            tagArrayList.add(tags);
        }


    }
}