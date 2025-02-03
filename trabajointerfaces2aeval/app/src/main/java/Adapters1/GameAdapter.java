package Adapters1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videojuegoslista.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Models.Game;
import android.content.Intent;
import Views.DetailActivity;  // Asegúrate de importar la clase DetailActivity

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> games;  // Lista de objetos Game
    private final OnGameClickListener listener;

    // Interfaz para la acción de click sobre un juego
    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    // Constructor del adaptador
    public GameAdapter(List<Game> games, OnGameClickListener listener) {
        this.games = games;
        this.listener = listener;
    }

    // Establecer los juegos (para actualizar la lista)
    public void setGames(List<Game> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);

        // Establecer el título del juego
        holder.title.setText(game.getTitle());

        // Usar Picasso para cargar la imagen del juego
        Picasso.get()
                .load(game.getImage())  // URL de la imagen del juego
                .placeholder(R.drawable.placeholder_image)  // Imagen por defecto mientras carga
                .error(R.drawable.error_image)  // Imagen de error si falla la carga
                .into(holder.image);

        // Establecer la descripción del juego
        holder.description.setText(game.getDescription());  // Aquí agregamos la descripción del juego

        // Configurar el listener para el click sobre el item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            // Pasamos el objeto `Game` como un extra en el intent
            intent.putExtra("GAME", game);
            v.getContext().startActivity(intent);
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

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.gameTitle);
            image = itemView.findViewById(R.id.gameImage);
            description = itemView.findViewById(R.id.gameDescription);
        }
    }
}
