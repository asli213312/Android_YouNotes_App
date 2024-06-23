package com.example.android_younotes_app.core

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

object UserPreferencesKeys {
    val LANGUAGE = stringPreferencesKey("language")
    val SYSTEM_THEME = booleanPreferencesKey("system_theme")
}

class UserPreferencesRepository(private val context: Context) {
    val themeSettingFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferencesKeys.SYSTEM_THEME] ?: false
        }

    val languageSettingFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferencesKeys.LANGUAGE] ?: "en"
        }

    suspend fun saveThemeSetting(isSystemTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.SYSTEM_THEME] = isSystemTheme
        }
    }

    suspend fun saveLanguageSetting(language: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.LANGUAGE] = language
        }
    }
}