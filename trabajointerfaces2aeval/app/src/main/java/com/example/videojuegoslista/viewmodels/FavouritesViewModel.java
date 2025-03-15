package com.example.videojuegoslista.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.videojuegoslista.models.Item;
import com.example.videojuegoslista.repositories.ItemRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouritesViewModel extends ViewModel {
    private ItemRepository itemRepository;
    private MutableLiveData<List<Item>> favoritos;
    private MutableLiveData<String> error;

    public FavouritesViewModel() {
        itemRepository = new ItemRepository();
        favoritos = new MutableLiveData<>(new ArrayList<>());
        error = new MutableLiveData<>();
    }

    public void cargarFavoritos() {
        itemRepository.getFavoritos(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> itemsFavoritos = new ArrayList<>();
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {
                    String itemId = favSnapshot.getKey();
                    itemRepository.getItem(itemId, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot itemSnapshot) {
                            Item item = itemSnapshot.getValue(Item.class);
                            if (item != null) {
                                item.setId(itemId);
                                item.setFavorite(true);
                                itemsFavoritos.add(item);
                                favoritos.setValue(itemsFavoritos);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            error.setValue("Error al cargar item: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                error.setValue("Error al cargar favoritos: " + databaseError.getMessage());
            }
        });
    }

    public void toggleFavorito(Item item) {
        itemRepository.toggleFavorito(item.getId(), task -> {
            if (task.isSuccessful()) {
                cargarFavoritos(); // Recargar la lista de favoritos
            } else {
                error.setValue("Error al actualizar favorito");
            }
        });
    }


    public void checkFavorito(String itemId, OnFavoritoCheckListener listener) {
        itemRepository.isFavorite(itemId, isFavorite -> {
            if (listener != null) {
                listener.onResult(isFavorite);
            }
        });
    }


    public interface OnFavoritoCheckListener {
        void onResult(boolean isFavorite);
    }

    public LiveData<List<Item>> getFavoritos() {
        return favoritos;
    }

    public LiveData<String> getError() {
        return error;
    }
}