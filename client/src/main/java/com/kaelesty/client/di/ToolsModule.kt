package com.kaelesty.client.di

import com.kaelesty.client.data.uilock.UiLockTool
import com.kaelesty.client.domain.uilock.UiLock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ToolsModule {

	@Provides
	fun bindUiLock(): UiLock = UiLockTool

}