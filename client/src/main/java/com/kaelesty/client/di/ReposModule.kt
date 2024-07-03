package com.kaelesty.client.di

import com.kaelesty.client.data.config.ServerConfigRepoImpl
import com.kaelesty.client.data.memoryusage.ServerStateRepoImpl
import com.kaelesty.client.data.scanlist.ScanListRepoImpl
import com.kaelesty.client.domain.config.ServerConfigRepo
import com.kaelesty.client.domain.scanlist.ScanListRepo
import com.kaelesty.client.domain.serverstate.ServerStateRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ReposModule {

	@Binds
	fun bindServerConfigRepo(impl: ServerConfigRepoImpl): ServerConfigRepo


	companion object {

		@Provides
		fun provideMemoryUsageRepo(): ServerStateRepo = ServerStateRepoImpl

		@Provides
		fun provideScanListRepo(): ScanListRepo = ScanListRepoImpl

	}
}