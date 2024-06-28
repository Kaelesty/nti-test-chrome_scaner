package com.kaelesty.nti_test_chrome_scaner.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
	val screen: Screen,
	val icon: ImageVector,
) {

	object Visual: NavItem(
		Screen.Visual,
		Icons.Default.Share,
	)

	object Scanlist: NavItem(
		Screen.ScanList,
		Icons.Default.Menu
	)

	object Config: NavItem(
		Screen.Config,
		Icons.Default.Settings
	)
}