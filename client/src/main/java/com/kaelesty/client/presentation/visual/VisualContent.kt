package com.kaelesty.client.presentation.visual

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaelesty.client.presentation.scanlist.ScanCard
import com.kaelesty.shared.domain.Scan
import com.kaelesty.shared.domain.bytesToMb

@Composable
fun VisualContent() {
	Log.d("VisualContent", "Recomposition")
	val viewModel = hiltViewModel<VisualViewModel>()
	val scans by viewModel.getScans().collectAsState()
	var menuExpanded by remember {
		mutableStateOf(false)
	}

	var selected by remember {
		mutableStateOf(scans.getOrNull(0))
	}
	var expandedNodes by remember {
		mutableStateOf(listOf<Scan.Node>())
	}
	Column(
		modifier = Modifier.padding(16.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
			contentAlignment = Alignment.Center
		) {
			Button(onClick = { menuExpanded = true }) {
				Text(
					text = "<---Select scan to visualize--->",
				)
			}
			DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
				scans.filter { it.id != selected?.id }.forEach {
					DropdownMenuItem(
						text = { Text("Scan #${it.id}") },
						onClick = {
							selected = it
							menuExpanded = false
							expandedNodes = listOf()
						}
					)
				}
			}
		}
		Card(
			modifier = Modifier.padding(horizontal = 12.dp)
		) {
		}
		selected?.let {
			ScanCard(scan = it, null)
			ScanVisualization(
				scan = it,
				expandedNodes = expandedNodes,
				onExpandNode = {
					expandedNodes = expandedNodes.toMutableList().apply {
						if (it in expandedNodes) remove(it) else add(it)
					}.toList()
				}
			)
		}

	}
}

@Composable
fun ScanVisualization(
	scan: Scan,
	expandedNodes: List<Scan.Node>,
	onExpandNode: (Scan.Node) -> Unit,
) {

	val verticalScrollState = rememberScrollState()
	val horizontalScrollState = rememberScrollState()

	Row(
		Modifier
			.horizontalScroll(horizontalScrollState)
	) {
		Column(
			Modifier
				.verticalScroll(verticalScrollState)
		) {
			NodeVisualization(
				node = scan.root,
				expandedNodes = expandedNodes,
				onExpandNode = onExpandNode,
				horizontalOffsetMult = 0
			)
		}
	}
}

@Composable
fun NodeVisualization(
	node: Scan.Node,
	expandedNodes: List<Scan.Node>,
	onExpandNode: (Scan.Node) -> Unit,
	horizontalOffsetMult: Int
) {
	when (node) {
		is Scan.Node.FileNode -> {
			Text(
				text = "${node.name} (${("%.2f".format(node.size.bytesToMb() * 1024))} KB)",
				color = getColorByNodeType(node.status),
				modifier = Modifier
					.padding(
						start = (45 * horizontalOffsetMult).dp
					)
			)
		}
		is Scan.Node.DirectoryNode -> {
			Text(
				text = node.name,
				color = getColorByNodeType(node.status),
				modifier = Modifier
					.clickable {
						onExpandNode(node)
					}
					.padding(
						start = (45 * horizontalOffsetMult).dp
					)
			)
			if (node in expandedNodes) {
				node.subNodes.forEach {
					NodeVisualization(
						node = it,
						expandedNodes = expandedNodes,
						horizontalOffsetMult = horizontalOffsetMult + 1,
						onExpandNode = onExpandNode
					)
				}
			}
		}
	}
}

fun getColorByNodeType(
	type: Scan.Node.NodeType
) = when (type) {
	Scan.Node.NodeType.NEW -> Color(27, 102, 62)
	Scan.Node.NodeType.MODIFIED -> Color.Magenta
	Scan.Node.NodeType.DEFAULT -> Color.Black
}

