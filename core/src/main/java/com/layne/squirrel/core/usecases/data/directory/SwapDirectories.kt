package com.layne.squirrel.core.usecases.data.directory

import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.core.domain.Directory

class SwapDirectories {

	operator fun invoke(data: Data, first: Int, second: Int): Data = data.copy(
		directories = copyDirectories(data.directories, first, second)
	)

	private fun copyDirectories(
		directories: List<Directory>,
		first: Int,
		second: Int
	): List<Directory> = directories.mapIndexed { i, dir ->
		val firstDir = directories[first]
		val secondDir = directories[second]
		when (i) {
			first  -> secondDir
			second -> firstDir
			else   -> dir.copy()
		}
	}
}