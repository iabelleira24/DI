package com.example.videojuegoslista.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.videojuegoslista.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private EditText currentPasswordEditText, newPasswordEditText;
    private Switch darkModeSwitch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Configuración de la Toolbar
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(view.findViewById(R.id.toolbar));  // Configura la Toolbar en el fragmento

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Habilita el botón de retroceso
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            activity.getSupportActionBar().setTitle("Perfil");  // Establece el título
        }

        // Inicialización de los componentes
        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        // Recupera la preferencia de modo oscuro
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        darkModeSwitch.setChecked(prefs.getBoolean("darkMode", false));

        // Acción para cambiar el modo oscuro
        darkModeSwitch.setOnCheckedChangeListener((compoundButton, checked) -> toggleDarkMode(checked));

        // Acción para cambiar la contraseña
        view.findViewById(R.id.changePasswordButton).setOnClickListener(v -> changePassword());

        return view;
    }

    // Método para cambiar la contraseña
    private void changePassword() {
        // Obtener las contraseñas ingresadas por el usuario
        String currentPass = currentPasswordEditText.getText().toString().trim();
        String newPass = newPasswordEditText.getText().toString().trim();

        // Verificar que ambos campos no estén vacíos
        if (currentPass.isEmpty() || newPass.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, ingresa la contraseña actual y la nueva", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear las credenciales usando el email del usuario y la contraseña actual ingresada
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPass);

        // Reautenticar al usuario
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Reautenticación exitosa, ahora actualizamos la contraseña
                        user.updatePassword(newPass)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(getContext(), "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Error al actualizar la contraseña: " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "Error en la reautenticación: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Método para habilitar o deshabilitar el modo oscuro
    private void toggleDarkMode(boolean enableDarkMode) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("darkMode", enableDarkMode).apply();
        AppCompatDelegate.setDefaultNightMode(enableDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        requireActivity().recreate();  // Recrea la actividad para aplicar los cambios
    }
}
