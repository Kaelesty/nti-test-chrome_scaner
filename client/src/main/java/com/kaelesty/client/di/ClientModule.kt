package com.kaelesty.client.di

import com.kaelesty.client.data.client.ClientImpl
import com.kaelesty.client.domain.client.Client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ClientModule {

	@Provides
	fun provideClient(): Client = ClientImpl
}