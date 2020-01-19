package com.squirrel.core.usecases.keys

import com.squirrel.core.domain.Directory

class AddDirectory {
	operator fun invoke(directories: MutableList<Directory>, directory: Directory) {
		directories.add(directory)
	}
}