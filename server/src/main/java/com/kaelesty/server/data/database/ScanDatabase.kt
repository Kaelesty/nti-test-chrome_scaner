package com.kaelesty.server.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
	entities = [
		ScanDbModel::class
	],
	version = 1,
	exportSchema = false
)
abstract class ScanDatabase: RoomDatabase() {

	abstract fun scanDao(): ScanDao
}