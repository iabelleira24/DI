package com.example.videojuegoslista.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.videojuegoslista.R;
import com.example.videojuegoslista.adapters.ItemAdapter;
import com.example.videojuegoslista.databinding.ActivityDashboardBinding;
import com.example.videojuegoslista.models.Item;
import com.example.videojuegoslista.viewmodels.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {
    private ActivityDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar el tema según la preferencia guardada
        applySavedTheme();

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.setLifecycleOwner(this);

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        setupRecyclerView();
        observeViewModel();
        setupListeners();
        dashboardViewModel.cargarItems();
    }

    private void setupRecyclerView() {
        itemAdapter = new ItemAdapter(new ArrayList<>(), this);
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewItems.setAdapter(itemAdapter);
    }

    private void observeViewModel() {
        dashboardViewModel.getItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items != null) {
                    itemAdapter.setItems(items);
                }
            }
        });

        dashboardViewModel.getNavigateToLogin().observe(this, shouldNavigate -> {
            if (shouldNavigate) {
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                finish();
            }
        });

        dashboardViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(DashboardActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnCerrarSesion.setOnClickListener(v -> {
            dashboardViewModel.cerrarSesion();
            Toast.makeText(this, "Cerraste Sesión Exitosamente", Toast.LENGTH_SHORT).show();
        });

        binding.btnFavoritos.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, FavoritesActivity.class))
        );

        binding.btnTheme.setOnClickListener(v -> toggleTheme());
    }

    private void toggleTheme() {
        SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPref.getBoolean("darkMode", false);

        // Alternar tema
        boolean newMode = !isDarkMode;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("darkMode", newMode);
        editor.apply();

        // Aplicar tema
        AppCompatDelegate.setDefaultNightMode(newMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Recrear actividad para aplicar cambios
        recreate();
    }

    private void applySavedTheme() {
        SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean darkMode = sharedPref.getBoolean("darkMode", false);

        AppCompatDelegate.setDefaultNightMode(darkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("titulo", item.getTitulo());
        intent.putExtra("descripcion", item.getDescripcion());
        intent.putExtra("url", item.getUrl());
        startActivity(intent);
    }
}
