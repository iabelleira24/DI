package com.example.videojuegoslista.repositories;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoritesRepository {
    private final DatabaseReference favoritesRef;
    private final DatabaseReference itemsRef;
    private final FirebaseAuth auth;

    public FavoritesRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.favoritesRef = database.getReference("usuarios");
        this.itemsRef = database.getReference("games");
    }

    public void toggleFavorite(String itemId, OnCompleteListener<Void> listener) {
        if (auth.getCurrentUser() == null || itemId == null) {
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference userFavorites = favoritesRef
                .child(userId)
                .child("favoritos");

        userFavorites.child(itemId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    userFavorites.child(itemId).removeValue()
                            .addOnCompleteListener(listener);
                } else {
                    userFavorites.child(itemId).setValue(true)
                            .addOnCompleteListener(listener);
                }
            }
        });
    }

    public void getFavorites(ValueEventListener listener) {
        if (auth.getCurrentUser() == null) return;

        String userId = auth.getCurrentUser().getUid();
        favoritesRef.child(userId)
                .child("favoritos")
                .addValueEventListener(listener);
    }

    public void checkFavoriteStatus(String itemId, OnSuccessListener<Boolean> listener) {
        if (auth.getCurrentUser() == null || itemId == null) {
            if (listener != null) {
                listener.onSuccess(false);
            }
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        favoritesRef.child(userId)
                .child("favoritos")
                .child(itemId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (listener != null) {
                        listener.onSuccess(snapshot.exists());
                    }
                });
    }


    public void clearFavorites(OnCompleteListener<Void> listener) {
        if (auth.getCurrentUser() == null) {
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        favoritesRef.child(userId)
                .child("favoritos")
                .removeValue()
                .addOnCompleteListener(listener);
    }
}
