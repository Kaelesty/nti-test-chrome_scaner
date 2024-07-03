package com.kaelesty.client.presentation.scanlist

import androidx.lifecycle.ViewModel
import com.kaelesty.client.domain.client.Client
import com.kaelesty.client.domain.scanlist.GetScanListUseCase
import com.kaelesty.shared.domain.ClientAction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanListViewModel @Inject constructor(
	private val getScanListUseCase: GetScanListUseCase,
	private val client: Client
): ViewModel() {

	fun getScanList() = getScanListUseCase()

	fun restoreScan(scanId: Int) {
		client.executeAction(
			ClientAction.RestoreScan(scanId)
		)
	}
}