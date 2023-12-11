package com.waka.techno;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutFragment extends Fragment {

    private TextView contact;
    private VideoView vw;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        contact=fragment.findViewById(R.id.textViewn9);
        webView = fragment.findViewById(R.id.videoView);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.DIAL",Uri.parse("tel:0761318088"));
                startActivity(intent);
            }
        });

        // load google map
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                LatLng latLng = new LatLng(6.882664, 79.901819);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).title("Techno");
                googleMap.clear();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                googleMap.addMarker(markerOptions);

            }
        });

        // video

        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/xqyUdNxWazA?si=MQCHLwX4L6C8pyHP\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";

        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());


//        vw.setMediaController(new MediaController(getContext()));
//
//        // video name should be in lower case alphabet.
////        videolist.add(R.raw.middle);
////        videolist.add(R.raw.faded);
////        videolist.add(R.raw.aeroplane);
////        setVideo(videolist.get(0));
//
//        String uriPath= "https://www.youtube.com/watch?v=xqyUdNxWazA";
//        Uri uri = Uri.parse(uriPath);
//        vw.setVideoURI(uri);
//        vw.start();
    }

//    public void setVideo(int id)
//    {
//        String uriPath
//                = "android.resource://"
//                + getPackageName() + "/" + id;
//        Uri uri = Uri.parse(uriPath);
//        vw.setVideoURI(uri);
//        vw.start();
//    }

}
//}