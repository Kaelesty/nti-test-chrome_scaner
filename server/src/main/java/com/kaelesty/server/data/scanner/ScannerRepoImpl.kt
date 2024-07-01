package com.kaelesty.server.data.scanner

import android.util.Log
import com.kaelesty.server.data.database.ScanDao
import com.kaelesty.server.data.database.ScanDbModel
import com.kaelesty.server.domain.scanner.Scan
import com.kaelesty.server.domain.scanner.ScannerRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScannerRepoImpl @Inject constructor(
	private val scanDao: ScanDao
): ScannerRepo {

	override suspend fun makeScan() {
		val lastScan = scanDao.getLastScan()?.let {
			ScanTool.getScanFromDbModel(it)
		}
		val scan = ScanTool.makeScan(lastScan)
		if (lastScan == null || ! ScanTool.equals(scan, lastScan)) {
			val metaFilePath = FileSystemTool.saveScan(
				scan
			)
			val fileSystemArchivePath = FileSystemTool.saveFileSystem(
				scan.id
			)
			scanDao.saveScan(
				ScanDbModel(
					scan.id,
					fileSystemArchivePath, metaFilePath
				)
			)
		}
		Log.d("MainActivity.kt", "Scan is finished")
	}

	override fun getScans(): Flow<Scan> {
		return scanDao.getScans().map {
			FileSystemTool.getScan(it.metaFilePath)
		}
	}

	override suspend fun restoreFileSystemByScan(scanId: Int) {
		val archivePath = scanDao.getScanById(scanId).archiveFilePath
		FileSystemTool.restoreFileSystem(archivePath)
	}
}