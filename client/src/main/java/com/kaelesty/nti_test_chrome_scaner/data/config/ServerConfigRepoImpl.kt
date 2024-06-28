package com.kaelesty.nti_test_chrome_scaner.data.config

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kaelesty.nti_test_chrome_scaner.domain.config.ServerConfig
import com.kaelesty.nti_test_chrome_scaner.domain.config.ServerConfigRepo
import com.kaelesty.nti_test_chrome_scaner.presentation.main.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ServerConfigRepoImpl @Inject constructor(
	@ApplicationContext private val context: Context
): ServerConfigRepo {

	private val CONFIG_DATASTORE_KEY = stringPreferencesKey("config")

	override suspend fun saveServerConfig(config: ServerConfig) {
		context.dataStore.edit { prefs ->
			prefs[CONFIG_DATASTORE_KEY] = Json.encodeToString(config)
		}
		Log.d("EditConfig", "OnFinish")
	}

	override fun getServerConfig(): Flow<ServerConfig?> = context
		.dataStore.data
		.map { prefs ->
			prefs[CONFIG_DATASTORE_KEY]?.let { Json.decodeFromString<ServerConfig>(it) }
		}
}