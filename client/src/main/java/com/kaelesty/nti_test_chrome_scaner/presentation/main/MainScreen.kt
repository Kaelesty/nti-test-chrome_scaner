package com.kaelesty.nti_test_chrome_scaner.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.nti_test_chrome_scaner.presentation.config.ConfigContent
import com.kaelesty.nti_test_chrome_scaner.presentation.serverstate.MemoryUsageCard
import com.kaelesty.nti_test_chrome_scaner.presentation.navigation.NavGraph
import com.kaelesty.nti_test_chrome_scaner.presentation.navigation.Screen
import com.kaelesty.nti_test_chrome_scaner.presentation.navigation.rememberMusicNavigationState
import com.kaelesty.nti_test_chrome_scaner.presentation.scanlist.ScanListContent
import com.kaelesty.nti_test_chrome_scaner.presentation.visual.VisualContent

@Composable
fun MainScreen() {

	val navigationState = rememberMusicNavigationState()

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		bottomBar = {
			Column(
				modifier = Modifier
					.padding(horizontal = 16.dp)
			) {
				MemoryUsageCard()
				Spacer(modifier = Modifier.height(4.dp))
				NavBar(navigationState = navigationState)
			}
		}
	) {
		Column(
			Modifier.padding(it)
		) {
			NavGraph(
				navHostController = navigationState.navHostController,
				startDestination = Screen.Visual.route,
				configContent = { ConfigContent() },
				visualContent = { VisualContent() },
				scanlistContent = { ScanListContent() }
			)
		}
	}
}