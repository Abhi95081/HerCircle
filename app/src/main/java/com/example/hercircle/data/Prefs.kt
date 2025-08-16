package com.example.hercircle.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("hercircle_prefs")

object Keys {
    val LAST_PERIOD = longPreferencesKey("last_period_epoch_day")
    val CYCLE_LEN = intPreferencesKey("cycle_length_days")
    val PERIOD_LEN = intPreferencesKey("period_length_days")

    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val THEME_MODE = stringPreferencesKey("theme_mode") // light, dark, system

    // 👇 Add this
    val ONBOARDED = booleanPreferencesKey("is_onboarded")
}

class Prefs(private val context: Context) {
    val lastPeriod: Flow<Long?> = context.dataStore.data.map { it[Keys.LAST_PERIOD] }
    val cycleLen: Flow<Int?> = context.dataStore.data.map { it[Keys.CYCLE_LEN] }
    val periodLen: Flow<Int?> = context.dataStore.data.map { it[Keys.PERIOD_LEN] }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[Keys.NOTIFICATIONS_ENABLED] ?: true
    }

    val themeMode: Flow<String> = context.dataStore.data.map {
        it[Keys.THEME_MODE] ?: "system"
    }

    // 👇 New flow
    val isOnboarded: Flow<Boolean> = context.dataStore.data.map {
        it[Keys.ONBOARDED] ?: false
    }

    suspend fun save(lastPeriodEpochDay: Long, cycleLength: Int, periodLength: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LAST_PERIOD] = lastPeriodEpochDay
            prefs[Keys.CYCLE_LEN] = cycleLength
            prefs[Keys.PERIOD_LEN] = periodLength
        }
    }

    suspend fun reset() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun setNotifications(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setTheme(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = mode
        }
    }

    // 👇 New setter
    suspend fun setOnboarded(done: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ONBOARDED] = done
        }
    }
}
