package com.example.videojuegoslista.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardRepository {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference favoritesReference;

    public DashboardRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("games");


        if (firebaseAuth.getCurrentUser() != null) {
            favoritesReference = FirebaseDatabase.getInstance().getReference()
                    .child("usuarios")
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .child("favoritos");
        }
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void getItem(String itemId, ValueEventListener listener) {
        databaseReference.child(itemId).addListenerForSingleValueEvent(listener);
    }


    public void getItems(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }


    public void getFavorites(ValueEventListener listener) {
        if (firebaseAuth.getCurrentUser() != null) {
            favoritesReference.addListenerForSingleValueEvent(listener);
        }
    }
}
