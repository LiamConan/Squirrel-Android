package com.layne.squirrel.core.usecases.data.directory

import com.layne.squirrel.core.domain.Data

class RenameDirectory {
	operator fun invoke(data: Data, dirIndex: Int, newTitle: String): Data {
		return data.copy(
			directories = data.directories.mapIndexed { i, dir ->
				if (i == dirIndex)
					dir.copy(title = newTitle)
				else dir.copy()
			}
		)
	}
}