package com.kaelesty.client.presentation.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaelesty.nti_test_chrome_scaner.R

@Composable
fun ConfigContent() {

	val viewModel = hiltViewModel<ConfigViewModel>()
	val state by viewModel.state.collectAsState()

	Column {
		ConnectionParamsCard(state, viewModel)
	}
}

@Composable
private fun ConnectionParamsCard(
	state: ConfigViewModel.State,
	viewModel: ConfigViewModel
) {

	var startScanningDialogueShown by rememberSaveable {
		mutableStateOf(false)
	}

	if (startScanningDialogueShown) {
		StartScanningDialog(
			onAccept = { viewModel.startScanning(it) },
			onReject = { startScanningDialogueShown = false }
		)
	}

	Card(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier.padding(12.dp)
		) {
			Text(text = stringResource(R.string.server_connection_parameters), fontSize = 24.sp)
			Spacer(modifier = Modifier.height(12.dp))

			// ip
			TextField(
				value = state.editableServerConfig.ip, onValueChange = { viewModel.changeIP(it) },
				supportingText = { Text(stringResource(R.string.ip_address)) }, isError = state.isIpInvalid,
				modifier = Modifier.fillMaxWidth(), singleLine = true,
				leadingIcon = {
					if (state.currentServerConfig.ip == state.editableServerConfig.ip) {
						Icon(Icons.Default.Done, contentDescription = null)
					}
				}
			)

			// port
			TextField(
				value = state.editableServerConfig.port,
				onValueChange = { viewModel.changePort(it) },
				supportingText = { Text(stringResource(R.string.port)) },
				isError = state.isPortInvalid,
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				leadingIcon = {
					if (state.currentServerConfig.port == state.editableServerConfig.port) {
						Icon(Icons.Default.Done, contentDescription = null)
					}
				}
			)
			Spacer(modifier = Modifier.height(12.dp))
			// save
			Button(onClick = { viewModel.saveConfig() }, Modifier.fillMaxWidth()) {
				Text(text = stringResource(R.string.save))
			}
			Text(
				text = stringResource(R.string.connection_must_be_restarted_for_the_settings_to_apply),
				fontSize = 12.sp, color = Color.Gray
			)
			Spacer(modifier = Modifier.height(12.dp))
			// restart
			Button(onClick = { viewModel.reconnect() }, Modifier.fillMaxWidth()) {
				Text(stringResource(R.string.reconnect))
			}
			if (state.isScanningStarted) {
				Button(onClick = { viewModel.stopScanning() }, Modifier.fillMaxWidth()) {
					Text(stringResource(R.string.stop_scanning))
				}
			}
			else {
				Button(onClick = { startScanningDialogueShown = true }, Modifier.fillMaxWidth()) {
					Text(stringResource(R.string.start_scanning))
				}
			}
		}
	}
}