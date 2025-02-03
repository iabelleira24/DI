package Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

    private String title;         // Título del juego
    private String description;   // Descripción del juego
    private String image;         // URL de la imagen del juego (ajustado para coincidir con Firebase)

    // Constructor vacío necesario para Firebase
    public Game() {}

    // Constructor con parámetros
    public Game(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    // Getters y Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Game{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    // Métodos de Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image);
    }

    // Constructor que recibe un Parcel
    protected Game(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readString();
    }

    // Creator de Parcelable
    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
