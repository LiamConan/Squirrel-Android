package com.layne.squirrel.core.usecases.data.account

import com.layne.squirrel.core.domain.Data

class DeleteAccount {
	operator fun invoke(data: Data, dirIndex: Int, accountIndex: Int): Data {
		return data.copy(
			directories = data.directories.mapIndexed { i, dir ->
				if (i == dirIndex)
					dir.copy(
						accounts = data.directories[i].accounts.mapIndexed { j, account ->
							if (j == accountIndex)
								null
							else account.copy()
						}.filterNotNull()
					)
				else dir.copy()
			}
		)
	}
}