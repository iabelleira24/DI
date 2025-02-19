package com.example.videojuegoslista.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.videojuegoslista.R;
import com.example.videojuegoslista.adapters.ItemAdapter;
import com.example.videojuegoslista.databinding.FragmentFavoritesBinding;
import com.example.videojuegoslista.models.Item;
import com.example.videojuegoslista.repositories.FavoritesRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private ItemAdapter adapter;
    private FavoritesRepository favoritesRepository;

    public FavoritesFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout sin la Toolbar
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuramos la Toolbar de la misma forma que en DashboardFragment
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(binding.toolbar);

        if (activity.getSupportActionBar() != null) {
            // Eliminamos la flecha de retroceso
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // No muestra el ícono de retroceso
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

            // Establecemos el título de la Toolbar
            activity.getSupportActionBar().setTitle("Mis Favoritos");
        }

        // Elimina la acción del botón de retroceso
        binding.toolbar.setNavigationOnClickListener(null); // No hace nada cuando se hace clic en el ícono de retroceso

        // Configura el RecyclerView
        setupRecyclerView();
        loadFavorites();
    }

    private void setupRecyclerView() {
        adapter = new ItemAdapter(new ArrayList<>(), item -> {
            // Realiza una transacción para mostrar el DetailFragment sin Toolbar
            DetailFragment detailFragment = DetailFragment.newInstance(
                    item.getId(),
                    item.getTitulo(),
                    item.getDescripcion(),
                    item.getUrl()
            );
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null) // Permite volver al listado al pulsar "Atrás"
                    .commit();
        });

        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(adapter);
    }

    private void loadFavorites() {
        favoritesRepository = new FavoritesRepository();
        favoritesRepository.getFavorites(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();

                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (DataSnapshot favSnapshot : snapshot.getChildren()) {
                        String itemId = favSnapshot.getKey();
                        if (itemId != null) {
                            FirebaseDatabase.getInstance().getReference("games")
                                    .child(itemId)
                                    .get()
                                    .addOnSuccessListener(dataSnapshot -> {
                                        if (dataSnapshot.exists()) {
                                            Item item = new Item();
                                            item.setId(itemId);
                                            item.setTitulo(dataSnapshot.child("title").getValue(String.class));
                                            item.setDescripcion(dataSnapshot.child("description").getValue(String.class));
                                            item.setUrl(dataSnapshot.child("image").getValue(String.class));
                                            items.add(item);
                                            adapter.setItems(new ArrayList<>(items));
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(requireContext(),
                                                "Error al cargar juego: " + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                } else {
                    adapter.setItems(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(),
                        "Error al cargar favoritos: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
