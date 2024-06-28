package com.kaelesty.nti_test_chrome_scaner.presentation.navigation

sealed class Screen(
	val route: String
) {

	object Config: Screen("config")
	object ScanList: Screen("scanlist")
	object Visual: Screen("visual")
}