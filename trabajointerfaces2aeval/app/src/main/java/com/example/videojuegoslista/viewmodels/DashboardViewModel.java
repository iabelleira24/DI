package com.example.videojuegoslista.viewmodels;

import androidx.annotation.NonNull;
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

public class DashboardViewModel extends ViewModel {
    private ItemRepository itemRepository;
    private MutableLiveData<Boolean> navigateToLogin;
    private MutableLiveData<List<Item>> items;
    private MutableLiveData<String> error;

    public DashboardViewModel() {
        itemRepository = new ItemRepository();
        navigateToLogin = new MutableLiveData<>();
        items = new MutableLiveData<>(new ArrayList<>());
        error = new MutableLiveData<>();
    }

    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    public LiveData<List<Item>> getItems() {
        return items;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void cerrarSesion() {
        // Usar el UserRepository para cerrar sesión
        navigateToLogin.setValue(true);
    }

    public void cargarItems() {
        itemRepository.getAllItems(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> listaItems = new ArrayList<>();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String id = itemSnapshot.getKey();
                    String titulo = itemSnapshot.child("title").getValue(String.class);
                    String descripcion = itemSnapshot.child("description").getValue(String.class);
                    String url = itemSnapshot.child("image").getValue(String.class);

                    Item item = new Item();
                    item.setId(id);
                    item.setTitulo(titulo);
                    item.setDescripcion(descripcion);
                    item.setUrl(url);

                    listaItems.add(item);
                }

                items.setValue(listaItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                error.setValue("Error al cargar los items: " + databaseError.getMessage());
            }
        });
    }
}