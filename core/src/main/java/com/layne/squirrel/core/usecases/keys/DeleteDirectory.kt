package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Directory

class DeleteDirectory {
	operator fun invoke(directories: MutableList<Directory>, directoryIndex: Int) {
		directories.removeAt(directoryIndex)
	}
}