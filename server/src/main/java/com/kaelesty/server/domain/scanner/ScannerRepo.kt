package com.kaelesty.server.domain.scanner

import kotlinx.coroutines.flow.Flow

interface ScannerRepo {

	suspend fun makeScan()

	fun getScans(): Flow<List<Scan>>

	suspend fun restoreFileSystemByScan(scanId: Int)
}