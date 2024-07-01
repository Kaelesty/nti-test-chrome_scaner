package com.kaelesty.server.data.scanner

import android.util.Log

object ProcessTool {

	private const val CHROME_PROCESS_NAME = "chrome"

	fun killChrome() {
		Log.d("MainActivity.kt", "Chrome Killed")
		ExecTool.exec("su -c pgrep chrome").forEach {
			ExecTool.exec("su -c kill $it")
		}
	}
}