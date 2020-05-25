package com.layne.squirrel.core.usecases.data.directory

import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.core.domain.Directory

class AddDirectory {
	operator fun invoke(data: Data, directory: Directory, index: Int? = null): Data {
		return if (index == null)
			data.copy(directories = data.directories + directory)
		else
			data.copy(
				directories = data.directories.toMutableList().apply {
					add(index, directory)
				}
			)
	}
}