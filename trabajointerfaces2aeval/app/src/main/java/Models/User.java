package Models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@IgnoreExtraProperties
public class User implements Serializable {

    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Set<String> favoritos;  // Set para evitar duplicados de juegos favoritos

    // 🔹 Constructor vacío requerido por Firebase
    public User() {
        this.favoritos = new HashSet<>();  // Siempre inicializar como mutable
    }

    // 🔹 Constructor completo
    public User(String fullName, String email, String phone, String address, Set<String> favoritos) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.favoritos = (favoritos != null) ? new HashSet<>(favoritos) : new HashSet<>();
    }

    // 🔹 Constructor adicional sin favoritos
    public User(String fullName, String email, String phone, String address) {
        this(fullName, email, phone, address, new HashSet<>());  // Llama al otro constructor
    }

    // 🔹 Getters y setters con validaciones
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo no puede estar vacío");
        }
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.([A-Za-z]{2,})$";
        if (email != null && Pattern.matches(emailRegex, email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Correo electrónico inválido");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de teléfono no puede estar vacío");
        }
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía");
        }
        if (address.length() < 5) {
            throw new IllegalArgumentException("La dirección debe tener al menos 5 caracteres");
        }
        this.address = address;
    }

    public Set<String> getFavoritos() {
        return new HashSet<>(favoritos);  // Devolver copia para evitar modificaciones externas
    }

    public void setFavoritos(Set<String> favoritos) {
        this.favoritos = (favoritos != null) ? new HashSet<>(favoritos) : new HashSet<>();
    }

    // 🔹 Métodos para manipular favoritos
    public void addFavorito(String gameId) {
        if (gameId != null && !gameId.trim().isEmpty()) {
            favoritos.add(gameId);
        }
    }

    public void removeFavorito(String gameId) {
        if (gameId != null && !gameId.trim().isEmpty()) {
            favoritos.remove(gameId);
        }
    }

    public boolean isFavorite(String gameId) {
        return favoritos.contains(gameId);
    }

    // 🔹 Métodos de depuración
    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", favoritos=" + favoritos.size() + " juegos" +
                '}';
    }

    public String toDebugString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", favoritos=" + favoritos +
                '}';
    }
}
