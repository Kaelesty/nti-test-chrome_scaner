package com.kaelesty.nti_test_chrome_scaner.di

import com.kaelesty.nti_test_chrome_scaner.data.config.ServerConfigRepoImpl
import com.kaelesty.nti_test_chrome_scaner.data.memoryusage.MemoryUsageRepoImpl
import com.kaelesty.nti_test_chrome_scaner.domain.config.ServerConfigRepo
import com.kaelesty.nti_test_chrome_scaner.domain.memoryusage.MemoryUsageRepo
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
		fun provideMemoryUsageRepoImpl(): MemoryUsageRepo = MemoryUsageRepoImpl
	}
}