package Adapters1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videojuegoslista.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import Models.Game;
import Views.DetailActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> games;
    private final Context context;
    private final OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    public GameAdapter(Context context, OnGameClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setGames(List<Game> games) {
        if (games != null) {
            this.games = games;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        if (games == null || games.isEmpty()) return;
        Game game = games.get(position);

        holder.title.setText(game.getTitle());
        holder.description.setText(game.getDescription());

        // Cargar imagen con Picasso
        if (game.getImage() != null && !game.getImage().isEmpty()) {
            Picasso.get().load(game.getImage()).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.placeholder_image);
        }

        holder.image.setContentDescription(game.getTitle() != null ? game.getTitle() : "Imagen del juego");

        // Configurar favoritos
        holder.favButton.setImageResource(game.isFavorite() ? R.drawable.corazon_lleno : R.drawable.corazon_vacio);
        holder.favButton.setOnClickListener(v -> {
            // Cambiar el estado localmente
            game.setFavorite(!game.isFavorite());

            // Obtener el userId del usuario actual
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Referencia al nodo de favoritos del usuario
            DatabaseReference userFavRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(userId)
                    .child("favoritos")
                    .child(game.getId());

            if (game.isFavorite()) {
                // Agregar a favoritos
                userFavRef.setValue(true);
            } else {
                // Eliminar de favoritos
                userFavRef.removeValue();
            }

            // Actualizar el icono del botón
            holder.favButton.setImageResource(game.isFavorite() ? R.drawable.corazon_lleno : R.drawable.corazon_vacio);
        });


        // Manejar el clic del juego
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onGameClick(game);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("GAME", game); // Pasar el objeto Game
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return games != null ? games.size() : 0;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView image;
        private final TextView description;
        private final ImageView favButton;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.gameTitle);
            image = itemView.findViewById(R.id.gameImage);
            description = itemView.findViewById(R.id.gameDescription);
            favButton = itemView.findViewById(R.id.favButton);
        }
    }
}
