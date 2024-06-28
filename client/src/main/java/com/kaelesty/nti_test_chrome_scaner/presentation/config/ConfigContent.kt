package com.kaelesty.nti_test_chrome_scaner.presentation.config

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

	Card(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier.padding(12.dp)
		) {
			Text(text = "Server connection parameters", fontSize = 24.sp)
			Spacer(modifier = Modifier.height(12.dp))

			// ip
			TextField(
				value = state.editableServerConfig.ip, onValueChange = { viewModel.changeIP(it) },
				supportingText = { Text("IP Address") }, isError = state.isIpInvalid,
				modifier = Modifier.fillMaxWidth(), singleLine = true,
				leadingIcon = {
					if (state.currentServerConfig.ip == state.editableServerConfig.ip) {
						Icon(Icons.Default.Done, contentDescription = null)
					}
				}
			)

			// port
			TextField(
				value = state.editableServerConfig.port, onValueChange = { viewModel.changePort(it) },
				supportingText = { Text("Port") }, isError = state.isPortInvalid,
				modifier = Modifier.fillMaxWidth(), singleLine = true,
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
				text = "The server must be restarted for the settings to apply",
				fontSize = 12.sp, color = Color.Gray
			)
		}
	}
}