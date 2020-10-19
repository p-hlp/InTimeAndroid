package com.example.intimesimple.repositories

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
    private val SOUND_STATE = preferencesKey<String>("sound_state")

    val soundStateFlow: Flow<String?> = dataStore.data
        .map {
            it[SOUND_STATE] ?: return@map null
        }.distinctUntilChanged()

    suspend fun getCurrentSoundState(): String {
        return dataStore.data.map { it[SOUND_STATE] ?: "MUTE"}.first()
    }

    suspend fun setSoundState(state: String) = dataStore.edit {
        it[SOUND_STATE] = state
    }
}