package com.kaelesty.nti_test_chrome_scaner.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kaelesty.nti_test_chrome_scaner.presentation.config.ConfigContent
import com.kaelesty.nti_test_chrome_scaner.presentation.navigation.NavGraph
import com.kaelesty.nti_test_chrome_scaner.presentation.navigation.Screen
import com.kaelesty.nti_test_chrome_scaner.presentation.navigation.rememberMusicNavigationState

@Composable
fun MainScreen() {

	val navigationState = rememberMusicNavigationState()

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		bottomBar = {
			NavBar(navigationState = navigationState)
		}
	) {
		Column(
			Modifier.padding(it)
		) {
			NavGraph(
				navHostController = navigationState.navHostController,
				startDestination = Screen.Visual.route,
				configContent = { ConfigContent() },
				visualContent = { Text(text = "2") },
				scanlistContent = { Text(text = "3")}
			)
		}
	}
}