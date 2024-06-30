package com.kaelesty.server.presentation.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.kaelesty.server.data.KtorConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionService : Service() {

	@Inject lateinit var connection: KtorConnection

	private val scope = CoroutineScope(Dispatchers.IO)

	override fun onBind(intent: Intent): IBinder? {
		return null
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		scope.launch {
			Log.d("ConnectionService", "Started!")
//			connection.getStateStream().collect {
//				Log.d("ConnectionService", it)
//			}
			for (i in 0..99) {
				connection.sendAction(i.toString())
			}
		}
		return START_STICKY
	}

	companion object {
		fun newIntent(context: Context) = Intent(
			context, ConnectionService::class.java
		)
	}
}