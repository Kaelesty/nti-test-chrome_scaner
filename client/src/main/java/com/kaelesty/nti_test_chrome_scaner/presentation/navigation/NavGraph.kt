package com.kaelesty.nti_test_chrome_scaner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(
	navHostController: NavHostController,
	startDestination: String,
	configContent: @Composable () -> Unit,
	visualContent: @Composable () -> Unit,
	scanlistContent: @Composable () -> Unit,
) {
	NavHost(
		navController = navHostController,
		startDestination = startDestination
	) {

		composable(route = Screen.Visual.route) {
			visualContent()
		}
		composable(route = Screen.Config.route) {
			configContent()
		}
		composable(route = Screen.ScanList.route) {
			scanlistContent()
		}
	}
}