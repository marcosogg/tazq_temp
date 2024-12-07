package ie.setu.tazq.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.setu.tazq.data.model.AuthState
import ie.setu.tazq.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signUp(name, email, password).collect { result ->
                _authState.value = result.fold(
                    onSuccess = { AuthState.Success(it) },
                    onFailure = { AuthState.Error(it.message ?: "Sign up failed") }
                )
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signIn(email, password).collect { result ->
                _authState.value = result.fold(
                    onSuccess = { AuthState.Success(it) },
                    onFailure = { AuthState.Error(it.message ?: "Sign in failed") }
                )
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState.Error("Signed out")
    }
}
