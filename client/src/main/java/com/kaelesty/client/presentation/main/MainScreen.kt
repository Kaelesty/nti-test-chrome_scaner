package com.kaelesty.client.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.client.presentation.config.ConfigContent
import com.kaelesty.client.presentation.serverstate.MemoryUsageCard
import com.kaelesty.client.presentation.navigation.NavGraph
import com.kaelesty.client.presentation.navigation.Screen
import com.kaelesty.client.presentation.navigation.rememberMusicNavigationState
import com.kaelesty.client.presentation.scanlist.ScanListContent
import com.kaelesty.client.presentation.visual.VisualContent
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainScreen(
	uiLockedFlow: StateFlow<Boolean>,
) {

	val navigationState = rememberMusicNavigationState()
	val uiLocked by uiLockedFlow.collectAsState()

	if (uiLocked) {
		UiLockedDialog()
	}

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