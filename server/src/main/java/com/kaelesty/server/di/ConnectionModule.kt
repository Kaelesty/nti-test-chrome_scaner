package com.kaelesty.server.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ConnectionModule {

//	@Singleton
//	@Provides
//	fun provideHttpClient(): HttpClient {
//		return HttpClient(CIO) {
//			install(Logging)
//			install(WebSockets)
//		}
//	}

}