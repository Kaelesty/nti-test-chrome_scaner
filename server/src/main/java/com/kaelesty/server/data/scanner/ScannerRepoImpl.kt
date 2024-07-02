package com.kaelesty.server.data.scanner

import com.kaelesty.server.data.database.ScanDao
import com.kaelesty.server.data.database.ScanDbModel
import com.kaelesty.server.data.logs.LogsTool
import com.kaelesty.server.domain.scanner.ScannerRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScannerRepoImpl @Inject constructor(
	private val scanDao: ScanDao,
): ScannerRepo {

	override suspend fun makeScan() {
		LogsTool.log("Scanning started...")
		val lastScan = scanDao.getLastScan()?.let {
			ScanTool.getScanFromDbModel(it)
		}
		val scan = ScanTool.makeScan(lastScan)
		val equals = lastScan?.let { ScanTool.equals(scan, it) } ?: false
		LogsTool.log("Scanning completed")
		if (lastScan == null || ! equals) {

			val metaFilePath = FilesTool.saveScan(
				scan
			)
			val fileSystemArchivePath = FilesTool.saveFileSystem(
				scan.id
			)
			scanDao.saveScan(
				ScanDbModel(
					scan.id,
					fileSystemArchivePath, metaFilePath
				)
			)
			LogsTool.log("New scan saved with id ${scan.id}")
		}
		else {
			LogsTool.log("No changes detected. Scan results will be dropped")
		}
	}

	override fun getScans(): Flow<List<com.kaelesty.shared.domain.Scan>> {
		return scanDao.getScans().map {
			it.map {
				FilesTool.getScan(it.metaFilePath)
			}
		}
	}

	override suspend fun restoreFileSystemByScan(scanId: Int) {
		val archivePath = scanDao.getScanById(scanId).archiveFilePath
		FilesTool.restoreFileSystem(archivePath)
		LogsTool.log("File system restored by scan with id $scanId")
	}
}