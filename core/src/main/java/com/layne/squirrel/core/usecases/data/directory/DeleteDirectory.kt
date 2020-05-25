package com.layne.squirrel.core.usecases.data.directory

import com.layne.squirrel.core.domain.Data

class DeleteDirectory {
	operator fun invoke(data: Data, index: Int): Data {
		return data.copy(
			directories = data.directories.mapIndexed { i, dir ->
				if (i == index)
					null
				else dir.copy()
			}.filterNotNull()
		)
	}
}