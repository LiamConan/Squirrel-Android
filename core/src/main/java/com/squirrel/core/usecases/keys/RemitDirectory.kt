package com.squirrel.core.usecases.keys

import com.squirrel.core.domain.Directory

class RemitDirectory {
	operator fun invoke(directories: MutableList<Directory>, directoryIndex: Int, directory: Directory) {
		directories.add(directoryIndex, directory)
	}
}