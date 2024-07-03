package com.kaelesty.nti_test_chrome_scaner.di

import com.kaelesty.nti_test_chrome_scaner.data.config.ServerConfigRepoImpl
import com.kaelesty.nti_test_chrome_scaner.data.memoryusage.ServerStateRepoImpl
import com.kaelesty.nti_test_chrome_scaner.data.scanlist.ScanListRepoImpl
import com.kaelesty.nti_test_chrome_scaner.domain.config.ServerConfigRepo
import com.kaelesty.nti_test_chrome_scaner.domain.scanlist.ScanListRepo
import com.kaelesty.nti_test_chrome_scaner.domain.serverstate.ServerStateRepo
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