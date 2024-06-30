package com.kaelesty.server.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import com.kaelesty.server.presentation.service.ConnectionService
import com.kaelesty.server.presentation.theme.AppTheme


class MainActivity : ComponentActivity() {
	@RequiresApi(Build.VERSION_CODES.R)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		//startConnectionService()
		Runtime.getRuntime().exec("su")

		Runtime
			.getRuntime()
			.exec("su -c cp -R /data/data/com.android.chrome /data/data/com.kaelesty.server/files")
			.also {
				it.waitFor()
				Log.d("MainActivity.kt", String(it.inputStream.readBytes()))
			}
		Runtime
			.getRuntime()
			.exec("su -c cat /data/data/com.kaelesty.server/files/com.android.chrome/shared_prefs/com.android.chrome_preferences.xml")
			.also {
				it.waitFor()
				Log.d("MainActivity.kt", String(it.inputStream.readBytes()))
			}
		Runtime
			.getRuntime()
			.exec("su -c tar -zcvf /data/data/com.kaelesty.server/files/chrome.tar.gz /data/data/com.android.chrome")
			.also {
				it.waitFor()
				Log.d("MainActivity.kt", String(it.inputStream.readBytes()))
			}

		setContent {
			AppTheme {
				Column {
					Text(text = "Max Memory: ${Runtime.getRuntime().maxMemory()}")
					Text(text = "Total Memory: ${Runtime.getRuntime().totalMemory()}")
					Text(text = "Free Memory: ${Runtime.getRuntime().freeMemory()}")
				}
			}
		}
	}

	private fun startConnectionService() {
		startForegroundService(
			ConnectionService.newIntent(this)
		)
	}
}