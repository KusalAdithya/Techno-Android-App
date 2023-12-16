package com.waka.techno;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.waka.techno.adapter.NotificationAdapter;
import com.waka.techno.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);


        // Notification Card -----------------------------------------------------------------------
        loadNotification(fragment);

        fragment.findViewById(R.id.clearNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                loadNotification(fragment);
            }
        });
    }

    private void  loadNotification(View fragment){
        ArrayList<Notification> notificationList = new ArrayList<>();

        Gson gson = new Gson();

        sharedPreferences = getContext().getSharedPreferences("notification", Context.MODE_PRIVATE);

        if (sharedPreferences.getAll().isEmpty()) {
            fragment.findViewById(R.id.emptyWishlistView).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.constraintLayoutn3).setVisibility(View.GONE);
        } else {
            fragment.findViewById(R.id.emptyWishlistView).setVisibility(View.GONE);
            fragment.findViewById(R.id.constraintLayoutn3).setVisibility(View.VISIBLE);

            Map<String, ?> allEntries = sharedPreferences.getAll();

            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                String key = entry.getKey();
                String jsonList = entry.getValue().toString();

                List<Notification> notificationItems = gson.fromJson(jsonList, new TypeToken<List<Notification>>() {
                }.getType());
                for (Notification item : notificationItems) {

                    notificationList.add(new Notification(
                            item.getTitle(),
                            item.getDescription(),
                            item.getDateTime(),
                            R.drawable.notification_icon_2));
                }
            }

            RecyclerView recyclerView = fragment.findViewById(R.id.notificationCardView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            NotificationAdapter adapter = new NotificationAdapter(notificationList);
            recyclerView.setAdapter(adapter);

        }
    }

}