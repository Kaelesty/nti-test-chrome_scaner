package com.kaelesty.server.data.scanner

import com.kaelesty.server.data.database.ScanDao
import com.kaelesty.server.data.database.ScanDbModel
import com.kaelesty.server.data.logs.LogsTool
import com.kaelesty.server.data.scanner.FilesTool.HOME_DIR
import com.kaelesty.server.domain.scanner.ScannerRepo
import com.kaelesty.shared.domain.Scan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScannerRepoImpl @Inject constructor(
	private val scanDao: ScanDao,
): ScannerRepo {

	override suspend fun makeScan(): Scan? {
		val lastScan = scanDao.getLastScan()?.let {
			ScanTool.getScanFromDbModel(it)
		}
		val scan = ScanTool.makeScan(lastScan)
		val equals = lastScan?.let { ScanTool.equals(scan, it) } ?: false
		if (lastScan == null || ! equals) {

			val metaFilePath = "$HOME_DIR/files/scan_meta_${scan.id}.json"
			val fileSystemFilePath = "$HOME_DIR/files/scan_${scan.id}.tar"

			scanDao.saveScan(
				ScanDbModel(
					scan.id,
					fileSystemFilePath, metaFilePath
				)
			)

			withContext(Dispatchers.IO) {
				FilesTool.saveScan(
					scan, metaFilePath
				)
				FilesTool.saveFileSystem(
					scan.id, fileSystemFilePath
				)
			}
			LogsTool.log("New scan saved with id ${scan.id}")
			return scan
		}
		else {
			LogsTool.log("No changes detected. Scan results will be dropped")
			return null
		}
	}

	override fun getScans(): Flow<List<Scan>> {
		return scanDao.getScans().map {
			it.map {
				FilesTool.getScan(it.metaFilePath)
			}
		}
	}

	suspend fun getScansStatic(): List<Scan> {
		return scanDao.getScansStatic().map {
			FilesTool.getScan(it.metaFilePath)
		}
	}

	override suspend fun restoreFileSystemByScan(scanId: Int) {
		val archivePath = scanDao.getScanById(scanId).archiveFilePath
		FilesTool.restoreFileSystem(archivePath)
		LogsTool.log("File system restored by scan with id $scanId")
	}
}