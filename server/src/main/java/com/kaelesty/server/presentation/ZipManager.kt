package com.kaelesty.server.presentation

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipManager {

	fun zipFolder(sourceFolder: File, outputFile: File) {
		ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
			sourceFolder.walkTopDown().forEach { file ->
				val entryName = sourceFolder.toPath().relativize(file.toPath()).toString()
				if (file.isDirectory) {
					zos.putNextEntry(ZipEntry("$entryName/"))
				} else {
					zos.putNextEntry(ZipEntry(entryName))
					FileInputStream(file).use { input ->
						input.copyTo(zos)
					}
				}
				zos.closeEntry()
			}
		}
	}

}