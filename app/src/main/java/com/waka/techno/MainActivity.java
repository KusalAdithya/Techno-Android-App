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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {   //implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar materialToolbar;
    private BottomNavigationView bottomNavigationView;

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

        if (item.getItemId() == R.id.bottomNavHome || item.getItemId() == R.id.sideNavHome) {
            loadFragment(new SingleProductFragment());

            findViewById(R.id.bottomNavHome).setSelected(true);
            findViewById(R.id.sideNavHome).setSelected(true);

            findViewById(R.id.bottomNavProfile).setSelected(false);
//            findViewById(R.id.sideNavProfile).setSelected(false);

            item.setChecked(false);

        } else if (item.getItemId() == R.id.bottomNavProfile) { //||item.getItemId() == R.id.sideNavProfile
            loadFragment(new ProfileFragment());

            findViewById(R.id.bottomNavProfile).setSelected(true);
//            findViewById(R.id.sideNavProfile).setSelected(true);

            findViewById(R.id.bottomNavHome).setSelected(false);
            findViewById(R.id.sideNavHome).setSelected(false);

            item.setChecked(false);

        } else if (item.getItemId() == R.id.bottomNavCart) {
            loadFragment(new CartFragment());

        } else if (item.getItemId() == R.id.bottomNavWishlist) {
            loadFragment(new WishlistFragment());

        } else if (item.getItemId() == R.id.bottomNavLogin) {
            loadFragment(new LoginFragment());

        } else if (item.getItemId() == R.id.bottomNavProducts) {
            loadFragment(new AllProductsFragment());

        } else if (item.getItemId() == R.id.bottomNavNotifi) {
            loadFragment(new NotificationFragment());
//            findViewById(R.id.bottomNavNotifi).setSelected(true);

        } else if (item.getItemId() == R.id.bottomNavAbout) {
            loadFragment(new AboutFragment());

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