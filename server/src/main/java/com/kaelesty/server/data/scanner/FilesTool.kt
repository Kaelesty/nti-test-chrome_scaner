package com.kaelesty.server.data.scanner

import com.kaelesty.server.domain.scanner.Scan
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object FilesTool {

	const val CHROME_DIR = "/data/data/com.android.chrome"
	const val HOME_DIR = "/data/data/com.kaelesty.server"

	suspend fun saveFileSystem(scanId: Int): String {
		return "$HOME_DIR/files/scan_$scanId.tar".also {
			ExecTool.exec("su -c tar -cvf $it $CHROME_DIR")
		}
	}

	suspend fun restoreFileSystem(archivePath: String) {
		ProcessTool.killChrome()
		clearFileSystem()
		ExecTool.exec("su -c tar -xvf $archivePath -C /")
	}

	private fun clearFileSystem() {
		ExecTool.exec("su -c rm -rf /data/data/com.android.chrome")
	}

	fun saveScan(scan: Scan): String {
		val scanJson = Json.encodeToString(scan)
		return "$HOME_DIR/files/scan_meta_${scan.id}.json".also {
			File(it).writeText(scanJson)
		}
	}

	suspend fun getScan(path: String): Scan {
		val scanJson = File(path).bufferedReader().readText()
		return Json.decodeFromString<Scan>(scanJson)
	}


}