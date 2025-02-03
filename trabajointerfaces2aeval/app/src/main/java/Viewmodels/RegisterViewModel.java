package Viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import Repositories.UserRepository;

public class RegisterViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();

    // LiveData para manejar el resultado del registro
    private final MutableLiveData<String> _registrationResult = new MutableLiveData<>();
    public LiveData<String> registrationResult = _registrationResult;

    // LiveData para manejar mensajes de error
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    // LiveData para manejar el estado de carga
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    /**
     * Método para registrar un usuario. Llama al repositorio y actualiza los estados de LiveData.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param fullName Nombre completo del usuario.
     * @param phone Número de teléfono del usuario.
     * @param address Dirección del usuario.
     */
    public void registerUser(String email, String password, String fullName, String phone, String address) {
        _isLoading.setValue(true); // Iniciar la carga

        // Llamada al repositorio para registrar al usuario
        userRepository.registerUser(email, password, fullName, phone, address, new UserRepository.RegisterCallback() {
            @Override
            public void onSuccess(String message) {
                _registrationResult.setValue(message); // Actualizar resultado
                _isLoading.setValue(false); // Finalizar carga
            }

            @Override
            public void onFailure(String error) {
                _errorMessage.setValue(error); // Mostrar mensaje de error
                _isLoading.setValue(false); // Finalizar carga
            }
        });
    }
}
