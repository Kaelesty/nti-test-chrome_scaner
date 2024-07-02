package com.kaelesty.server.presentation.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.preferences.preferencesDataStore
import com.kaelesty.server.presentation.service.ServerConnectionService
import com.kaelesty.server.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore by preferencesDataStore(
	name = "preferences"
)


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		startConnectionService()

		setContent {
			AppTheme {
				MainContent()
			}
		}
	}

	private fun startConnectionService() {
		startForegroundService(
			ServerConnectionService.newIntent(this)
		)
	}
}