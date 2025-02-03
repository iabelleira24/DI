package Viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import Repositories.UserRepository;

public class LoginViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();

    // LiveData para el resultado del login
    private final MutableLiveData<String> _loginResult = new MutableLiveData<>("");
    public LiveData<String> loginResult = _loginResult;

    // LiveData para el estado de carga
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    // LiveData para errores
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     * Actualiza el estado de carga y maneja el resultado o errores.
     */
    public void loginUser(String email, String password) {
        _isLoading.setValue(true);

        userRepository.loginUser(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                _loginResult.setValue(message);  // Mensaje de éxito
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(String error) {
                _errorMessage.setValue(error);  // Mensaje de error
                _isLoading.setValue(false);
            }
        });
    }
}
