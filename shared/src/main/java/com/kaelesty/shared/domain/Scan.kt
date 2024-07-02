package com.kaelesty.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Scan(
	val id: Int,
	val meta: Meta,
	val root: Node,
) {

	@Serializable
	data class Meta(
		val startTime: Long, // millis
		val scanTime: Long, // millis
		val allFilesSize: Long, // bytes
	)

	@Serializable
	sealed interface Node {

		@Serializable
		enum class NodeType {
			DEFAULT,
			NEW, // GREEN
			MODIFIED, // VIOLET
		}

		val status: NodeType

		val name: String

		@Serializable
		data class FileNode(
			override val status: NodeType,
			val size: Long, // bytes
			override val name: String,
		): Node

		@Serializable
		data class DirectoryNode(
			override val status: NodeType,
			val subNodes: List<Node>,
			override val name: String,
		): Node
	}
}