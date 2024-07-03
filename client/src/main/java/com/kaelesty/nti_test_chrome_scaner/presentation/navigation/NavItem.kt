package com.kaelesty.nti_test_chrome_scaner.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
	val screen: Screen,
	val icon: ImageVector,
	val label: String,
) {

	object Visual: NavItem(
		Screen.Visual,
		Icons.Default.Share,
		"Visualization"
	)

	object Scanlist: NavItem(
		Screen.ScanList,
		Icons.Default.Menu,
		"Scans list"
	)

	object Config: NavItem(
		Screen.Config,
		Icons.Default.Settings,
		"Config"
	)
}