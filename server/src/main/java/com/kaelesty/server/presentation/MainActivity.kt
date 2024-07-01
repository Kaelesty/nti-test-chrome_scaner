package com.kaelesty.server.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import com.kaelesty.server.domain.scanner.ScannerRepo
import com.kaelesty.server.presentation.service.ConnectionService
import com.kaelesty.server.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	@Inject lateinit var scannerRepo: ScannerRepo
	private val scope = CoroutineScope(Dispatchers.IO)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		//startConnectionService()

		scope.launch {
			scannerRepo.makeScan()
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