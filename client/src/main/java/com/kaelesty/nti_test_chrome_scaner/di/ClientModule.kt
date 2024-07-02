package com.kaelesty.nti_test_chrome_scaner.di

import com.kaelesty.nti_test_chrome_scaner.data.client.ClientImpl
import com.kaelesty.nti_test_chrome_scaner.data.config.ServerConfigRepoImpl
import com.kaelesty.nti_test_chrome_scaner.domain.client.Client
import com.kaelesty.nti_test_chrome_scaner.domain.config.ServerConfigRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ClientModule {

	@Binds
	fun bindClient(impl: ClientImpl): Client
}