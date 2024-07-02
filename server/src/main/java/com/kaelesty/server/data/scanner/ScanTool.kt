package com.kaelesty.server.data.scanner

import com.kaelesty.server.data.database.ScanDbModel
import com.kaelesty.shared.domain.Scan
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ScanTool {

	const val CHROME_DIR = FilesTool.CHROME_DIR
	const val HOME_DIR = FilesTool.HOME_DIR

	fun makeScan(lastScan: com.kaelesty.shared.domain.Scan?): com.kaelesty.shared.domain.Scan {
		val scan = com.kaelesty.shared.domain.Scan(
			id = lastScan?.id?.plus(1) ?: 0,
			meta = com.kaelesty.shared.domain.Scan.Meta(
				System.currentTimeMillis(),
				0,
				0
			),
			root = com.kaelesty.shared.domain.Scan.Node.DirectoryNode(
				com.kaelesty.shared.domain.Scan.Node.NodeType.DEFAULT,
				name = CHROME_DIR,
				subNodes = ExecTool.exec("su -c ls $CHROME_DIR -q -l -F")
					.filter {
						! it.startsWith("total")
					}
					.map {
						val fileMeta = lsOutputToFileMeta(it)
						convertLsOutputToNode(
							output = it,
							lastScanNode = lastScan?.let {
								(lastScan.root as com.kaelesty.shared.domain.Scan.Node.DirectoryNode).let { dirNode ->
									dirNode.subNodes.find { node ->
										node.name == fileMeta.name
									}
								}
							},
							parentPath = CHROME_DIR
						)
					}
			)
		)

		return scan.copy(
			meta = scan.meta.copy(
				scanTime = System.currentTimeMillis() - scan.meta.startTime,
				allFilesSize = measureScanFilesSize(scan)
			)
		)
	}

	fun getScanFromDbModel(dbModel: ScanDbModel): com.kaelesty.shared.domain.Scan {
		val scanJson = File(dbModel.metaFilePath).bufferedReader().readLines()[0]
		return Json.decodeFromString<com.kaelesty.shared.domain.Scan>(scanJson)
	}

	fun equals(scan1: com.kaelesty.shared.domain.Scan, scan2: com.kaelesty.shared.domain.Scan): Boolean {
		return scan1.root == scan2.root
	}

	private fun convertLsOutputToNode(
		output: String,
		lastScanNode: com.kaelesty.shared.domain.Scan.Node?,
		parentPath: String
	): com.kaelesty.shared.domain.Scan.Node {
		// -rw------- 1 u0_a107 u0_a107 322436 2024-06-30 13:52 0
		// -rw------- 1 u0_a107 u0_a107  14820 2024-06-30 13:52 0.jpeg
		// drwx------ 26 u0_a107 u0_a107       4096 2024-07-01 07:39 app_chrome/
		// drwxrws--x 10 u0_a107 u0_a107_cache 4096 2024-07-01 07:39 cache/
		val fileMeta = lsOutputToFileMeta(output)
		return if (fileMeta.name.endsWith("/")) { // is dir
			com.kaelesty.shared.domain.Scan.Node.DirectoryNode(
				name = fileMeta.name,
				status = if (lastScanNode == null) com.kaelesty.shared.domain.Scan.Node.NodeType.NEW
				else com.kaelesty.shared.domain.Scan.Node.NodeType.DEFAULT,
				subNodes = ExecTool
					.exec("su -c ls $parentPath/${fileMeta.name} -q -l -F")
					.filter {
						! it.startsWith("total")
					}
					.map {
						convertLsOutputToNode(
							output = it,
							lastScanNode = lastScanNode?.let { dirNode ->
								(dirNode as com.kaelesty.shared.domain.Scan.Node.DirectoryNode).subNodes.find { node ->
									node.name == fileMeta.name
								}
							},
							parentPath = "$parentPath/${fileMeta.name}"
						)
					}
			)
		} else { // is file
			com.kaelesty.shared.domain.Scan.Node.FileNode(
				size = fileMeta.size,
				name = fileMeta.name,
				status = if (lastScanNode == null) com.kaelesty.shared.domain.Scan.Node.NodeType.NEW
				else if ((lastScanNode as com.kaelesty.shared.domain.Scan.Node.FileNode).size != fileMeta.size) com.kaelesty.shared.domain.Scan.Node.NodeType.MODIFIED
				else com.kaelesty.shared.domain.Scan.Node.NodeType.DEFAULT
			)
		}
	}

	private fun measureScanFilesSize(scan: com.kaelesty.shared.domain.Scan): Long {
		fun measureNodeSize(node: com.kaelesty.shared.domain.Scan.Node): Long {
			return when (node) {
				is com.kaelesty.shared.domain.Scan.Node.FileNode -> {
					node.size
				}

				is com.kaelesty.shared.domain.Scan.Node.DirectoryNode -> {
					node.subNodes.sumOf {
						measureNodeSize(it)
					}
				}
			}
		}

		return measureNodeSize(scan.root)
	}

	private fun lsOutputToFileMeta(output: String): FileMeta {
		val params = output
			.split(" ")
			.filter { it != "" }
			.subList(4, 8)
		return FileMeta(
			params[0].toLong(), dateTimeToTime(params[1], params[2]), params[3]
		)
	}

	private fun dateTimeToTime(date: String, time: String): Long {
		return LocalDateTime.parse(
			"$date $time",
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
		).second * 1000L
	}

	data class FileMeta(
		val size: Long,
		val modifiedTime: Long,
		val name: String
	)
}