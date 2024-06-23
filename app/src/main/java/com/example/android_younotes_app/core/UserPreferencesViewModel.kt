package com.example.android_younotes_app.core

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    private val _state = mutableStateOf(UserPreferencesData(false, "en"))
    val state: State<UserPreferencesData> = _state

    init {
        viewModelScope.launch {
            repository.themeSettingFlow.collect { isSystemTheme ->
                _state.value = _state.value.copy(isSystemTheme = isSystemTheme)
            }

            repository.languageSettingFlow.collect { language ->
                _state.value = _state.value.copy(language = language)
            }

            Log.d("UserPreferences", "Current system theme state: ${_state.value.isSystemTheme}")
            Log.d("UserPreferences", "Current language: ${_state.value.language}")
        }
    }

    fun saveThemeSetting(isSystemTheme: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(isSystemTheme)
            _state.value = state.value.copy(isSystemTheme = isSystemTheme)
            Log.d("UserPreferences", "Current system theme state: ${_state.value.isSystemTheme}")
        }
    }
}

class UserPreferencesViewModelFactory(private val repository: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserPreferencesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}