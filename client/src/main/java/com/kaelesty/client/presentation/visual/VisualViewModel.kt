package com.kaelesty.client.presentation.visual

import androidx.lifecycle.ViewModel
import com.kaelesty.client.domain.scanlist.GetScanListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisualViewModel @Inject constructor(
	private val getScanListUseCase: GetScanListUseCase
): ViewModel() {

	fun getScans() = getScanListUseCase()
}