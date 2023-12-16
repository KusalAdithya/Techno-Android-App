package com.waka.techno;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RemoteViews;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.waka.techno.adapter.SingleProductImageAdapter;
import com.waka.techno.model.Product;
import com.waka.techno.model.ProductImages;
import com.waka.techno.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SingleProductFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private TextView nameText, category, model, brand, description, price, qty, qtyView;
    private ArrayList<String> productImageList;
    private RecyclerView recyclerView;
    private SingleProductImageAdapter singleProductImageAdapter;
    private Product product;
    private NotificationManager notificationManager;
    private String channelId = "info";
    private ImageButton miniBtn, plusBtn;

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
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        nameText = fragment.findViewById(R.id.textView58);
        category = fragment.findViewById(R.id.textViewn64);
        model = fragment.findViewById(R.id.textView64);
        brand = fragment.findViewById(R.id.textView66);
        description = fragment.findViewById(R.id.textView60);
        price = fragment.findViewById(R.id.textView62);
        qty = fragment.findViewById(R.id.textQty);

        miniBtn = fragment.findViewById(R.id.miniButton);
        plusBtn = fragment.findViewById(R.id.plusButton);
        qtyView = fragment.findViewById(R.id.textView24);


        //load product data ------------------------------------------------------------------------
        nameText.setText(product.getName());
        category.setText(product.getCategory());
        model.setText(product.getModel());
        brand.setText(product.getBrand());
        description.setText(product.getDescription());
        qty.setText(String.valueOf(product.getQty()) + " Items");
        price.setText("LKR " + product.getPrice() + "0");

        //Single Product Images --------------------------------------------------------------------
        productImageList = new ArrayList<>();

        for (String imgList : product.getProductImage()) {
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

                if (firebaseUser != null) {

                    long currentTimeMillis = System.currentTimeMillis();

                    // add to cart db
                    FirebaseDatabase.getInstance().getReference("Cart").child(firebaseUser.getUid())
                            .child(product.getId()).setValue(currentTimeMillis)
                            .addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Product added to cart successfully.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                            ).addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Add to cart Fail. Try Again " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );

                } else {
                    Toast.makeText(getContext(), "Please login first!", Toast.LENGTH_LONG).show();
                }


            }
        });

        // add to wishlist btn ---------------------------------------------------------------------
        fragment.findViewById(R.id.addWishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (firebaseUser != null) {

                    long currentTimeMillis = System.currentTimeMillis();

                    // add to wishlist db
                    FirebaseDatabase.getInstance().getReference("Wishlist").child(firebaseUser.getUid())
                            .child(product.getId()).setValue(currentTimeMillis)
                            .addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Product added to wishlist successfully.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                            ).addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Add to wishlist Fail. Try Again " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                } else {
                    Toast.makeText(getContext(), "Please login first!", Toast.LENGTH_LONG).show();
                }


            }
        });


        // qty -------------------------------------------------------------------------------------
        int currentQty = product.getQty();
        miniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyText = Integer.parseInt((String) qtyView.getText());

                if (qtyText > 1) {
                    qtyView.setText(String.valueOf(qtyText - 1));
                } else {
                    Toast.makeText(getContext(), "You can't go below 1", Toast.LENGTH_LONG).show();
                }
            }
        });
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyText = Integer.parseInt((String) qtyView.getText());

                if (qtyText < currentQty) {
                    qtyView.setText(String.valueOf(qtyText + 1));
                } else {
                    Toast.makeText(getContext(), "Maximum quantity deserve", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Notification ----------------------------------------------------------------------------
        requestPermissions(new String[]{
                Manifest.permission.POST_NOTIFICATIONS
        }, 1000);

        notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "INFO", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            channel.setDescription("This is Information Channel");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setVibrationPattern(new long[]{0, 500, 0, 0});
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        // Buy now btn------------------------------------------------------------------------------
        fragment.findViewById(R.id.buyBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (firebaseUser != null) {


                            // SMS -----------------------------------------------------------------
                            // ---- search user ----------------------------------------------------
                            final String[] mobile = new String[1];
                            db.collection("users").document(firebaseUser.getUid()).get().addOnSuccessListener(
                                    new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String mobile = null;
                                            if (documentSnapshot.exists()) {
                                                User user = documentSnapshot.toObject(User.class);
                                                System.out.println(user.getMobile());
                                                mobile = user.getMobile();

                                                // Notification --------------------------------------------------------
                                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                                intent.putExtra("name", "ABCD");

                                                PendingIntent pendingIntent = PendingIntent.getActivity(
                                                        getActivity(),
                                                        0,
                                                       intent,
                                                        PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
                                                );
                                                String qty = qtyView.getText().toString();
                                                String message = "Hi " + user.getFirstName() + ",\n\n" +
                                                        "You have purchased " + qty + " of " + product.getName() + ", " + "Product from us.\n\n" +
                                                        "Thank you for choosing Techno! and we're thrilled to be a part of your shopping experience.\n\n" +
                                                        "Techno";

                                                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                                                Notification notification = new NotificationCompat.Builder(getActivity().getApplicationContext(), channelId)
                                                        .setAutoCancel(true)
                                                        .setSmallIcon(R.drawable.ic_stat_name)
                                                        .setContentTitle("Techno")
                                                        .setContentText(message)
                                                        .setContentIntent(pendingIntent)
                                                        .setStyle(
                                                                new NotificationCompat.InboxStyle()
                                                                        .addLine("Hi " + user.getFirstName() + ",")
                                                                        .addLine("You have purchased " + qty + " of" + product.getName() + " from Techno")
                                                                        .addLine("We're thrilled to be a part of your shopping experience")
                                                                        .addLine("Thank you for choosing us!")
                                                        )
                                                        .build();
                                                notificationManager.notify(1, notification);


                                                // SMS notification --------------------------------------------------------------------------------------------------
//                                                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//                                                    SmsManager smsManager = SmsManager.getDefault();
//                                                    smsManager.sendTextMessage(mobile, null, "Hi " + firebaseUser.getEmail()
//                                                            + ",\n" +"You have purchased " + product.getName() + " from us.\n\n"
//                                                            + "Thank you for choosing Techno!", null, null);
//                                                    Toast.makeText(getContext(), "Order Purchased Successfully", Toast.LENGTH_LONG).show();
//                                                } else {
//                                                    ActivityCompat.requestPermissions(getActivity(), new String[]{
//                                                            Manifest.permission.SEND_SMS
//                                                    }, 100);
//                                                }

                                                // add to SharedPreferences -------------------------------------------------------------------------------------------------------------------------
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
                                                String formattedTime = sdf.format(new Date());

                                                com.waka.techno.model.Notification notificationModel
                                                        = new com.waka.techno.model.Notification(
                                                        "Order Purchased Successfully",
                                                        message,
                                                        formattedTime
                                                );

                                                ArrayList<com.waka.techno.model.Notification> notificationItemsList = new ArrayList<>();
                                                notificationItemsList.add(notificationModel);

                                                Gson gson = new Gson();
                                                String jsonNotificationList = gson.toJson(notificationItemsList);

                                                // Shared Preferences -----------------------------------------------------------------------------------------
                                                SharedPreferences preferences = getContext().getSharedPreferences("notification", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString(String.valueOf(System.currentTimeMillis()), jsonNotificationList);
                                                editor.apply();

                                                // add order db ---------------------------------------------------------------------------------------------

                                                Toast.makeText(getContext(), "Order Purchased Successfully", Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(getContext(), "Please update your profile.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }
                            ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(getContext(), "Please login first!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}