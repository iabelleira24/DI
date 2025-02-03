package Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videojuegoslista.R;
import com.squareup.picasso.Picasso;

import Models.Game;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView gameImageView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);  // Asegúrate de tener un layout para esta activity

        // Inicialización de los componentes
        titleTextView = findViewById(R.id.gameTitle);
        descriptionTextView = findViewById(R.id.gameDescription);
        gameImageView = findViewById(R.id.gameImage);
        backButton = findViewById(R.id.backButton);

        // Obtener el objeto Game desde el Intent
        Intent intent = getIntent();
        Game game = intent.getParcelableExtra("GAME");

        if (game != null) {
            // Establecer los datos en los elementos de la UI
            titleTextView.setText(game.getTitle());
            descriptionTextView.setText(game.getDescription());

            // Usar Picasso para cargar la imagen
            Picasso.get()
                    .load(game.getImage())
                    .placeholder(R.drawable.placeholder_image)  // Asegúrate de tener una imagen de placeholder
                    .error(R.drawable.error_image)  // Y también una imagen en caso de error
                    .into(gameImageView);
        } else {
            // Mostrar mensaje de error si algún dato falta
            titleTextView.setText("Datos del juego no disponibles");
            descriptionTextView.setText("Información no disponible.");
        }

        // Lógica para el botón "Volver"
        backButton.setOnClickListener(v -> finish()); // Cierra la actividad y vuelve a la anterior
    }
}
