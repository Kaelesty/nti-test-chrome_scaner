package com.kaelesty.server.di

import android.content.Context
import androidx.room.Room
import com.kaelesty.server.data.database.ScanDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

	@Provides
	fun provideScanDao(
		@ApplicationContext context: Context
	) = Room.databaseBuilder(
		context,
		ScanDatabase::class.java,
		name = "scan_database"
	).build().scanDao()
}