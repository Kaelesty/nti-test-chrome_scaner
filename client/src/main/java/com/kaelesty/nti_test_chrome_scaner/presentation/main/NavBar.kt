package com.kaelesty.nti_test_chrome_scaner.presentation.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kaelesty.nti_test_chrome_scaner.presentation.navigation.NavItem
import com.kaelesty.nti_test_chrome_scaner.presentation.navigation.NavigationState

@Composable
fun NavBar(navigationState: NavigationState) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
	) {
		val items = listOf(
			NavItem.Visual, NavItem.Scanlist, NavItem.Config
		)

		val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

		NavigationBar {
			Row(verticalAlignment = Alignment.CenterVertically) {
				items.forEach { navItem ->
					val selected = navBackStackEntry?.destination?.hierarchy?.any {
						it.route == navItem.screen.route
					} ?: false

					NavigationBarItem(
						selected = selected,
						onClick = { navigationState.navigateTo(navItem.screen.route) },
						icon = {
							Icon(navItem.icon, contentDescription = null)
						},
						label = {
							Text(text = navItem.label)
						}
					)
				}
			}
		}
	}
}