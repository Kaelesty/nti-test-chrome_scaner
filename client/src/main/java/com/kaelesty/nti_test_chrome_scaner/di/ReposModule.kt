package com.kaelesty.nti_test_chrome_scaner.di

import com.kaelesty.nti_test_chrome_scaner.data.config.ServerConfigRepoImpl
import com.kaelesty.nti_test_chrome_scaner.domain.config.ServerConfigRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ReposModule {

	@Binds
	fun bindServerConfigRepo(impl: ServerConfigRepoImpl): ServerConfigRepo
}