package com.squirrel.core.usecases.keys

import com.squirrel.core.domain.Directory
import com.squirrel.core.swap

class SwapDirectories {
	operator fun invoke(directories: MutableList<Directory>, from: Int, to: Int) {
		directories.swap(from, to)
	}
}