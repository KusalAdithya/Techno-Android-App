package com.waka.techno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {   //implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar materialToolbar;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new HomeFragment());

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.menuViewer);
        materialToolbar = findViewById(R.id.materialToolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        setSupportActionBar(materialToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.imageMenuButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (item.getItemId() == R.id.bottomNavHome || item.getItemId() == R.id.sideNavHome) {
            loadFragment(new HomeFragment());

            findViewById(R.id.bottomNavHome).setSelected(true);
            findViewById(R.id.sideNavHome).setSelected(true);

            findViewById(R.id.bottomNavProfile).setSelected(false);
//            findViewById(R.id.sideNavProfile).setSelected(false);

            item.setChecked(false);

        } else if (item.getItemId() == R.id.bottomNavProfile) { //||item.getItemId() == R.id.sideNavProfile

            if (firebaseUser != null) {
                loadFragment(new ProfileFragment());
            } else {
                Toast.makeText(MainActivity.this, "You want to log first!", Toast.LENGTH_SHORT).show();
                loadFragment(new LoginFragment());
            }

        } else if (item.getItemId() == R.id.bottomNavCart) {
            if (firebaseUser != null) {
                loadFragment(new CartFragment());
            } else {
                Toast.makeText(MainActivity.this, "You want to log first!", Toast.LENGTH_SHORT).show();
                loadFragment(new LoginFragment());
            }

        } else if (item.getItemId() == R.id.bottomNavWishlist) {
            if (firebaseUser != null) {
                loadFragment(new WishlistFragment());
            } else {
                Toast.makeText(MainActivity.this, "You want to log first!", Toast.LENGTH_SHORT).show();
                loadFragment(new LoginFragment());
            }

        } else if (item.getItemId() == R.id.bottomNavLogin) {

            if (firebaseUser != null) {
                Toast.makeText(MainActivity.this, "You are already logged!", Toast.LENGTH_SHORT).show();
                loadFragment(new HomeFragment());
            } else {
                loadFragment(new LoginFragment());
            }

        } else if (item.getItemId() == R.id.bottomNavProducts) {
            loadFragment(new AllProductsFragment());

        } else if (item.getItemId() == R.id.bottomNavNotifi) {
            if (firebaseUser != null) {
                loadFragment(new NotificationFragment());
            } else {
                Toast.makeText(MainActivity.this, "You want to log first!", Toast.LENGTH_SHORT).show();
                loadFragment(new LoginFragment());
            }

        } else if (item.getItemId() == R.id.bottomNavAbout) {
            loadFragment(new AboutFragment());

        } else if (item.getItemId() == R.id.bottomNavOrders) {
            if (firebaseUser != null) {
                loadFragment(new OrderFragment());
            } else {
                Toast.makeText(MainActivity.this, "You want to log first!", Toast.LENGTH_SHORT).show();
                loadFragment(new LoginFragment());
            }

        } else if (item.getItemId() == R.id.bottomNavLogout) {

            if (firebaseUser != null) {
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                loadFragment(new HomeFragment());
            } else {
                Toast.makeText(MainActivity.this, "You want to log first!", Toast.LENGTH_SHORT).show();
                loadFragment(new LoginFragment());
            }

        }

        drawerLayout.close();
        return true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}