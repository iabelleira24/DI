package com.example.videojuegoslista.repositories;

import com.example.videojuegoslista.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemRepository {
    private DatabaseReference databaseReference;
    private DatabaseReference favoritosReference;
    private FirebaseAuth firebaseAuth;


    public ItemRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("games");
    }

    public void getItem(String itemId, ValueEventListener listener) {
        databaseReference.child(itemId).addListenerForSingleValueEvent(listener);
    }

    public void getAllItems(ValueEventListener listener) {
        databaseReference.addListenerForSingleValueEvent(listener);
    }

    // Método para obtener actualizaciones en tiempo real
    public void observeItems(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }

    public void getFavoritos(ValueEventListener listener) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        favoritosReference.child(userId).addValueEventListener(listener);
    }

    public void toggleFavorito(String peliculaId, OnCompleteListener<Void> listener) {
        if (peliculaId == null || peliculaId.isEmpty()) {
            return;
        }
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userFavRef = favoritosReference.child(userId).child(peliculaId);

        userFavRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                // Si existe, lo removemos
                userFavRef.removeValue().addOnCompleteListener(listener);
            } else {
                // Si no existe, lo agregamos
                userFavRef.setValue(true).addOnCompleteListener(listener);
            }
        });
    }

    public void isFavorite(String peliculaId, OnSuccessListener<Boolean> listener) {
        if (peliculaId == null || peliculaId.isEmpty()) {
            listener.onSuccess(false);
            return;
        }

        String userId = firebaseAuth.getCurrentUser().getUid();
        favoritosReference.child(userId).child(peliculaId)
                .get()
                .addOnSuccessListener(snapshot -> listener.onSuccess(snapshot.exists()));
    }

    public void addItem(Item item, OnCompleteListener<Void> listener) {
        if (item == null || item.getId() == null) {
            return;
        }
        databaseReference.child(item.getId()).setValue(item)
                .addOnCompleteListener(listener);
    }

    public void removeItem(String itemId, OnCompleteListener<Void> listener) {
        if (itemId == null || itemId.isEmpty()) {
            return;
        }
        databaseReference.child(itemId).removeValue()
                .addOnCompleteListener(listener);
    }
}