package Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import Models.Game;

import java.util.Objects;

public class FavouriteItem {
    private String itemId;
    private Game game; // Asociación con el objeto Game

    // Constructor vacío requerido para Firebase y Gson
    public FavouriteItem() {
    }

    public FavouriteItem(@NonNull String itemId, @NonNull Game game) {
        // Validación del itemId y game en el constructor
        if (itemId.isEmpty()) {
            throw new IllegalArgumentException("Item ID no puede ser nulo o vacío");
        }
        if (game == null) {
            throw new IllegalArgumentException("Game no puede ser nulo");
        }
        this.itemId = itemId;
        this.game = game;
    }

    // Getter de itemId
    @NonNull
    public String getItemId() {
        return itemId;
    }

    // Setter de itemId con validación
    public void setItemId(@NonNull String itemId) {
        if (itemId.isEmpty()) {
            throw new IllegalArgumentException("Item ID no puede ser nulo o vacío");
        }
        this.itemId = itemId;
    }

    // Getter de game
    @NonNull
    public Game getGame() {
        return game;
    }

    // Setter de game con validación
    public void setGame(@NonNull Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game no puede ser nulo");
        }
        this.game = game;
    }

    // Método para representar el objeto como cadena (toString)
    @NonNull
    @Override
    public String toString() {
        // Si game es nulo, devuelve un título no disponible
        return "FavouriteItem{" +
                "itemId='" + itemId + '\'' +
                ", gameTitle='" + (game != null ? game.getTitle() : "N/A") + '\'' +
                '}';
    }

    // Compara este objeto con otro de la misma clase (por itemId o game ID)
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavouriteItem that = (FavouriteItem) o;
        return itemId.equals(that.itemId) || (game != null && game.getId().equals(that.game.getId()));
    }

    // Genera un código hash basado en itemId y game ID
    @Override
    public int hashCode() {
        return Objects.hash(itemId, game != null ? game.getId() : null);
    }
}
