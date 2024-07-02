package com.kaelesty.nti_test_chrome_scaner.presentation.memoryusage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.nti_test_chrome_scaner.domain.memoryusage.GetMemoryUsageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoryUsageViewModel @Inject constructor(
	private val getMemoryUsageUseCase: GetMemoryUsageUseCase
): ViewModel() {

	init {
		viewModelScope.launch {
			getMemoryUsageUseCase().collect {
				Log.d("WebSocketClient", "MemoryUsage set to ${it.toString()}")
			}
		}
	}

	fun getMemoryUsage() = getMemoryUsageUseCase()
}