package com.kaelesty.server.data.scanner

import com.kaelesty.shared.domain.Scan
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object FilesTool {

	const val CHROME_DIR = "/data/data/com.android.chrome"
	const val HOME_DIR = "/data/data/com.kaelesty.server"

	fun saveFileSystem(scanId: Int, path: String): Process {
		return ExecTool.execProcessed("su -c nice tar -cf $path $CHROME_DIR")
	}

	fun restoreFileSystem(archivePath: String): Process {
		ProcessTool.killChrome()
		clearFileSystem()
		return ExecTool.execProcessed("su -c nice tar -xf $archivePath -C /")
	}

	fun saveScan(scan: Scan, path: String) {
		val scanJson = Json.encodeToString(scan)
		path.also {
			File(it).writeText(scanJson)
		}
	}

	fun getScan(path: String): Scan {
		val scanJson = File(path).bufferedReader().readText()
		return Json.decodeFromString<Scan>(scanJson)
	}

	private fun clearFileSystem() {
		ExecTool.exec("su -c rm -rf /data/data/com.android.chrome")
	}
}