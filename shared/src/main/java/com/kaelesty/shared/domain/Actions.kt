package com.kaelesty.shared.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientAction {

	@Serializable
	data class StartScanning(
		val delaySeconds: Int,
	): ClientAction

	@Serializable
	object StopScanning: ClientAction

	@Serializable
	object RequestScanningState: ClientAction

	@Serializable
	object RequestScanList: ClientAction

	@Serializable
	data class RestoreScan(
		val scanId: Int
	): ClientAction
}

@Serializable
sealed interface ServerAction {

	@Serializable
	data class UpdateMemoryUsage(
		val memoryUsage: MemoryUsage
	): ServerAction

	@Serializable
	data class SetScanningState(
		val isScanningStarted: Boolean,
	): ServerAction

	@Serializable
	data class SetScanList(
		val scans: List<Scan>
	): ServerAction

	@Serializable
	data class NewScan(
		val scan: Scan
	): ServerAction
}