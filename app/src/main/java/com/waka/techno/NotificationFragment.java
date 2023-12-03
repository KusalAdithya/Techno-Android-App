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

import com.waka.techno.adapter.NotificationAdapter;
import com.waka.techno.model.Notification;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
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
        ArrayList<Notification> notificationList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            notificationList.add(new Notification(
               "Title",
               "Description",
               "10:20 23-12-2023"
            ));
        }

        RecyclerView recyclerView = fragment.findViewById(R.id.notificationCardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter<NotificationAdapter.ViewHolder> adapter = new NotificationAdapter(notificationList);

        if (notificationList.isEmpty()){
            fragment.findViewById(R.id.emptyWishlistView).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.constraintLayoutn3).setVisibility(View.INVISIBLE);
        }else {
            recyclerView.setAdapter(adapter);
        }

    }
}