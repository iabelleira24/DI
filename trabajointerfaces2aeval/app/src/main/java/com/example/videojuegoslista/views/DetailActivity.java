package com.example.videojuegoslista.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.videojuegoslista.R;
import com.example.videojuegoslista.databinding.ActivityDetailBinding;
import com.example.videojuegoslista.repositories.FavoritesRepository;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private FavoritesRepository favoritesRepository;
    private String itemId;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        favoritesRepository = new FavoritesRepository();

        itemId = getIntent().getStringExtra("id");
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String url = getIntent().getStringExtra("url");

        setupUI(titulo, descripcion, url);
        setupFavoriteButton();
        checkFavoriteStatus();
    }

    private void setupUI(String titulo, String descripcion, String url) {
        binding.tvTituloDetalle.setText(titulo);
        binding.tvDescripcionDetalle.setText(descripcion);

        Glide.with(this)
                .load(url)
                .centerCrop()
                .into(binding.ivItemDetalle);
    }

    private void setupFavoriteButton() {
        binding.fabFavorite.setOnClickListener(v -> {
            if (itemId == null) {
                Toast.makeText(this, "Error: ID de item no disponible", Toast.LENGTH_SHORT).show();
                return;
            }

            favoritesRepository.toggleFavorite(itemId, task -> {
                if (task.isSuccessful()) {
                    isFavorite = !isFavorite;
                    updateFavoriteIcon();
                    Toast.makeText(this,
                            isFavorite ? "Añadido a favoritos" : "Eliminado de favoritos",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "Error al actualizar favoritos",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void checkFavoriteStatus() {
        if (itemId == null) return;

        favoritesRepository.checkFavoriteStatus(itemId, status -> {
            isFavorite = status;
            updateFavoriteIcon();
        });
    }

    private void updateFavoriteIcon() {
        binding.fabFavorite.setImageResource(
                isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border
        );
    }
}