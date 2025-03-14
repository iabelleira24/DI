package com.example.videojuegoslista.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.videojuegoslista.R;
import com.example.videojuegoslista.adapters.ItemAdapter;
import com.example.videojuegoslista.databinding.FragmentDashboardBinding;
import com.example.videojuegoslista.models.Item;
import com.example.videojuegoslista.viewmodels.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements ItemAdapter.OnItemClickListener {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private ItemAdapter itemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySavedTheme();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        setupRecyclerView();
        observeViewModel();
        setupListeners();
        dashboardViewModel.cargarItems();
    }

    private void setupRecyclerView() {
        itemAdapter = new ItemAdapter(new ArrayList<>(), this);
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewItems.setAdapter(itemAdapter);
    }

    private void observeViewModel() {
        dashboardViewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items != null) {
                    itemAdapter.setItems(items);
                }
            }
        });

        dashboardViewModel.getNavigateToLogin().observe(getViewLifecycleOwner(), shouldNavigate -> {
            if (shouldNavigate) {
                startActivity(new Intent(requireContext(), LoginActivity.class));
                requireActivity().finish();
            }
        });

        dashboardViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnCerrarSesion.setOnClickListener(v -> {
            dashboardViewModel.cerrarSesion();
            Toast.makeText(getContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        });

        binding.btnTheme.setOnClickListener(v -> toggleTheme());
    }

    private void toggleTheme() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPref.getBoolean("darkMode", false);
        boolean newMode = !isDarkMode;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("darkMode", newMode);
        editor.apply();

        AppCompatDelegate.setDefaultNightMode(newMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        requireActivity().recreate();
    }

    private void applySavedTheme() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean darkMode = sharedPref.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(darkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onItemClick(Item item) {
        DetailFragment detailFragment = DetailFragment.newInstance(
                item.getId(),
                item.getTitulo(),
                item.getDescripcion(),
                item.getUrl()
        );

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
