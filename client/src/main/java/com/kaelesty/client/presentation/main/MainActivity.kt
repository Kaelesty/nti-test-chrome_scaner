package com.kaelesty.client.presentation.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.preferences.preferencesDataStore
import com.kaelesty.client.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore by preferencesDataStore(
	name = "preferences"
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		startService()

		setContent {
			AppTheme {
				MainScreen()
			}
		}
	}

	private fun startService() {
		startForegroundService(
			ClientService.newIntent(this)
		)
	}
}