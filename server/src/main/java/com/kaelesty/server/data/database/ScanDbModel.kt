package com.kaelesty.server.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "scans"
)
data class ScanDbModel(
	@PrimaryKey val id: Int,
	val archiveFilePath: String,
	val metaFilePath: String,
)