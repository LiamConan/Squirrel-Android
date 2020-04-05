package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Directory

class AddDirectory {
	operator fun invoke(directories: MutableList<Directory>, directory: Directory) {
		directories.add(directory)
	}
}