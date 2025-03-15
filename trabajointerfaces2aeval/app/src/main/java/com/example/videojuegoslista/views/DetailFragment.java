package com.example.videojuegoslista.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.videojuegoslista.R;
import com.example.videojuegoslista.databinding.FragmentDetailBinding;
import com.example.videojuegoslista.repositories.FavoritesRepository;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    private FavoritesRepository favoritesRepository;
    private String itemId;
    private boolean isFavorite = false;


    public static DetailFragment newInstance(String id, String titulo, String descripcion, String url) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("title", titulo);
        args.putString("description", descripcion);
        args.putString("image", url);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        favoritesRepository = new FavoritesRepository();


        if (getArguments() != null) {
            itemId = getArguments().getString("id");
            String titulo = getArguments().getString("title");
            String descripcion = getArguments().getString("description");
            String url = getArguments().getString("image");

            setupUI(titulo, descripcion, url);
        } else {
            Toast.makeText(getContext(), "No se proporcionaron datos", Toast.LENGTH_SHORT).show();
        }

        setupFavoriteButton();
        checkFavoriteStatus();
    }

    private void setupUI(String titulo, String descripcion, String url) {
        binding.tvTituloDetalle.setText(titulo);
        binding.tvDescripcionDetalle.setText(descripcion);

        Glide.with(requireContext())
                .load(url)
                .centerCrop()
                .into(binding.ivItemDetalle);
    }

    private void setupFavoriteButton() {
        binding.fabFavorite.setOnClickListener(v -> {
            if (itemId == null) {
                Toast.makeText(getContext(), "Error: ID de item no disponible", Toast.LENGTH_SHORT).show();
                return;
            }

            favoritesRepository.toggleFavorite(itemId, task -> {
                if (task.isSuccessful()) {
                    isFavorite = !isFavorite;
                    updateFavoriteIcon();
                    Toast.makeText(getContext(),
                            isFavorite ? "Añadido a favoritos" : "Eliminado de favoritos",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
