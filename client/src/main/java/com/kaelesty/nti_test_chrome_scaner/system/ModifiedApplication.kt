package com.kaelesty.nti_test_chrome_scaner.system

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ModifiedApplication : Application() {
//	val component by lazy {
//		DaggerApplicationComponent
//			.factory()
//			.create(
//				this@ModifiedApplication
//			)
//	}
}