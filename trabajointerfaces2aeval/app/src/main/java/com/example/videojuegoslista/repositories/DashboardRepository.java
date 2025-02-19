package com.example.videojuegoslista.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardRepository {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public DashboardRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("games");
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
}