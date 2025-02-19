package com.example.videojuegoslista.views;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.videojuegoslista.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializar DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        // Configurar ActionBarDrawerToggle para mostrar el icono de hamburguesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Cargar fragmento inicial (solo si no se ha guardado el estado previamente)
        if (savedInstanceState == null) {
            // Cargar DashboardFragment y configurar el título
            switchFragment(new DashboardFragment(), getString(R.string.title_dashboard));
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }

        // Manejar clics en el menú de navegación
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                switchFragment(new DashboardFragment(), getString(R.string.title_dashboard));
            } else if (itemId == R.id.nav_favourites) {
                switchFragment(new FavoritesFragment(), getString(R.string.title_favorites));
            } else if (itemId == R.id.nav_profile) {
                switchFragment(new ProfileFragment(), getString(R.string.title_profile));
            } else if (itemId == R.id.nav_logout) {
                finish();
                return true;
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    // Método para cambiar de fragmento y actualizar el título
    private void switchFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        // Actualiza el título de la Toolbar (header) al nombre del fragmento
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title); // Actualiza el título con el valor adecuado
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
