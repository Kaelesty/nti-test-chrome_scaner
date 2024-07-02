package com.kaelesty.server.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaelesty.server.R
import com.kaelesty.shared.domain.bytesToMb

@Composable
fun MainContent() {

	val viewModel = hiltViewModel<MainViewModel>()
	val state by viewModel.state.collectAsState()

	Column(
		Modifier.padding(16.dp)
	) {
		ConnectionCard(state, viewModel)
		Spacer(modifier = Modifier.height(6.dp))
		MemoryUsageCard(state)
		Spacer(modifier = Modifier.height(6.dp))
		LogsCard(state)
	}
}

@Composable
private fun LogsCard(state: MainViewModel.State) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier.padding(12.dp)
		) {
			Text("Logs", fontSize = 24.sp)
			LazyColumn(
				modifier = Modifier.height(400.dp)
			) {
				items(state.logs) {
					Text(it)
					Spacer(modifier = Modifier.height(2.dp))
				}
			}
		}
	}
}

@Composable
private fun MemoryUsageCard(state: MainViewModel.State) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier.padding(12.dp)
		) {
			Text(stringResource(R.string.memory_usage), fontSize = 24.sp)
			Spacer(modifier = Modifier.height(12.dp))
			Text(text = "Used memory")
			Text(text = "${
				"%.2f".format(state.memoryUsage.usedMemory.bytesToMb())
			} MB", fontSize = 20.sp)
			Spacer(modifier = Modifier.height(6.dp))
			Text(text = "Available memory")
			Text(text = "${
				"%.2f".format(state.memoryUsage.availableMemory.bytesToMb())
			} MB", fontSize = 20.sp)
		}
	}
}

@Composable
private fun ConnectionCard(
	state: MainViewModel.State,
	viewModel: MainViewModel
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier.padding(12.dp)
		) {
			Text(text = stringResource(R.string.server_connection_parameters), fontSize = 24.sp)
			Spacer(modifier = Modifier.height(12.dp))
			TextField(
				value = state.editablePort,
				onValueChange = { viewModel.setPort(it) },
				supportingText = { Text(stringResource(R.string.port)) },
				isError = state.isPortInvalid,
				modifier = Modifier.fillMaxWidth(),
				singleLine = true,
				leadingIcon = {
					if (state.port == state.editablePort) {
						Icon(Icons.Default.Done, contentDescription = null)
					}
				}
			)
			Spacer(modifier = Modifier.height(12.dp))
			// save
			Button(onClick = { viewModel.savePort() }, Modifier.fillMaxWidth()) {
				Text(stringResource(R.string.save))
			}
			Text(
				text = stringResource(R.string.the_server_must_be_restarted_for_the_settings_to_apply),
				fontSize = 12.sp, color = Color.Gray
			)

			Spacer(modifier = Modifier.height(12.dp))
			Button(onClick = { viewModel.switchServer() }, Modifier.fillMaxWidth()) {
				Text(
					if (state.isServerStarted) {
						stringResource(R.string.stop_server)
					} else {
						stringResource(R.string.start_server)
					}
				)
			}
		}
	}
}