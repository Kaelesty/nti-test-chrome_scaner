package com.kaelesty.server.data.scanner

import java.io.BufferedReader
import java.io.InputStreamReader

object ExecTool {

	private val RUNTIME = Runtime.getRuntime()

	fun exec(command: String): List<String> {
		RUNTIME.exec(command).also {
			it.waitFor()
			val isr = InputStreamReader(it.inputStream)
			val br = BufferedReader(isr)
			return br.readLines()
		}
	}

	fun execWithNonsplittedReturn(command: String): String {
		RUNTIME.exec(command).also {
			it.waitFor()
			return it.inputStream
				.readBytes()
				.toString()
		}
	}
}