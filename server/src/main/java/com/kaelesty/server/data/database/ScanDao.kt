package com.kaelesty.server.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {

	@Insert
	suspend fun saveScan(scanDbModel: ScanDbModel)

	@Query("SELECT * FROM scans")
	fun getScans(): Flow<List<ScanDbModel>>

	@Query("SELECT * FROM scans WHERE id = :scanId")
	fun getScanById(scanId: Int): ScanDbModel

	@Query("SELECT * FROM scans ORDER BY id DESC LIMIT 1")
	fun getLastScan(): ScanDbModel?
}