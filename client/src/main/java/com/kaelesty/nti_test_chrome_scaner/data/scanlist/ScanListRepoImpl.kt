package com.kaelesty.nti_test_chrome_scaner.data.scanlist

import com.kaelesty.nti_test_chrome_scaner.domain.scanlist.ScanListRepo
import com.kaelesty.shared.domain.Scan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ScanListRepoImpl: ScanListRepo {

	private val scope = CoroutineScope(Dispatchers.IO)

	private val _scanList = MutableStateFlow<List<Scan>>(listOf())

	override fun getScanList(): StateFlow<List<Scan>> {
		return _scanList.asStateFlow()
	}

	fun addScan(scan: Scan) {
		scope.launch {
			_scanList.emit(
				_scanList.value.toMutableList().apply {
					add(scan)
				}.toList()
			)
		}
	}

	fun setScanList(scanList: List<Scan>) {
		scope.launch {
			_scanList.emit(
				scanList
			)
		}
	}
}