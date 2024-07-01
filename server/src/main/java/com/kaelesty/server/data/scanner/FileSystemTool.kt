package com.kaelesty.server.data.scanner

import com.kaelesty.server.domain.scanner.Scan
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object FileSystemTool {

	const val CHROME_DIR = "/data/data/com.android.chrome"
	const val HOME_DIR = "/data/data/com.kaelesty.server"

	suspend fun saveFileSystem(scanId: Int): String {
		return "$HOME_DIR/files/scan_$scanId.tar".also {
			ExecTool.exec("su -c tar -zcvf $it $CHROME_DIR")
		}
	}

	suspend fun restoreFileSystem(path: String) {

	}

	fun saveScan(scan: Scan): String {
		val scanJson = Json.encodeToString(scan)
		return "$HOME_DIR/files/scan_meta_${scan.id}.json".also {
			ExecTool.exec("su -c echo $scanJson > $it")
		}
	}

	suspend fun getScan(path: String): Scan {
		val scanJson = ExecTool.execWithNonsplittedReturn("su -c cat $path")
		return Json.decodeFromString<Scan>(scanJson)
	}


}