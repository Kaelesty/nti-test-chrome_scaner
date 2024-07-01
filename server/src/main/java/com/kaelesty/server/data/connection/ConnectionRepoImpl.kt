package com.kaelesty.server.data.connection

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kaelesty.server.data.logs.LogsTool
import com.kaelesty.server.domain.connection.ConnectionRepo
import com.kaelesty.server.presentation.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConnectionRepoImpl @Inject constructor(
	@ApplicationContext private val context: Context,
): ConnectionRepo {

	private val PORT_DATASTORE_KEY = stringPreferencesKey("port")

	override suspend fun saveServerPort(port: String) {
		context.dataStore.edit { prefs ->
			prefs[PORT_DATASTORE_KEY] = port
		}
		LogsTool.log("Server port changed to $port")
	}

	override fun getServerPort(): Flow<String?> = context
		.dataStore.data
		.map { prefs ->
			prefs[PORT_DATASTORE_KEY]
		}
}