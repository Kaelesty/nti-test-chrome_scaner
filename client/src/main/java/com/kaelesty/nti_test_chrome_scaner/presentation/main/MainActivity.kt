package com.kaelesty.nti_test_chrome_scaner.presentation.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.preferences.preferencesDataStore
import com.kaelesty.nti_test_chrome_scaner.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore by preferencesDataStore(
	name = "preferences"
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//	private val component by lazy {
//		(application as ModifiedApplication)
//			.component
//	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//component.inject(this)

		setContent {
			AppTheme {
				MainScreen()
			}
		}
	}
}