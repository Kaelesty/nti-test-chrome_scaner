package com.kaelesty.server.domain.scanner

import com.kaelesty.shared.domain.Scan
import kotlinx.coroutines.flow.Flow

interface ScannerRepo {

	suspend fun makeScan(): Scan?

	fun getScans(): Flow<List<Scan>>

	suspend fun restoreFileSystemByScan(scanId: Int)
}