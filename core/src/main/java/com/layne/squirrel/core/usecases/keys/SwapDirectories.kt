package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.core.swap

class SwapDirectories {
	operator fun invoke(directories: MutableList<Directory>, from: Int, to: Int) {
		directories.swap(from, to)
	}
}