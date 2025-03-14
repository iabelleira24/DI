package com.example.videojuegoslista.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.videojuegoslista.R;
import com.example.videojuegoslista.repositories.FavoritesRepository;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            switchFragment(new DashboardFragment(), getString(R.string.title_dashboard));
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }


        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                switchFragment(new DashboardFragment(), getString(R.string.title_dashboard));
            } else if (itemId == R.id.nav_favourites) {
                switchFragment(new FavoritesFragment(), getString(R.string.title_favorites));
            } else if (itemId == R.id.nav_profile) {
                switchFragment(new ProfileFragment(), getString(R.string.title_profile));
            } else if (itemId == R.id.nav_clear_favourites) {

                new FavoritesRepository().clearFavorites(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Favoritos eliminados", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al eliminar favoritos", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (itemId == R.id.nav_logout) {
                finish();
                return true;
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void switchFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}