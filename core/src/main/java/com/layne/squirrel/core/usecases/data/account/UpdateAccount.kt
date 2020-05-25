package com.layne.squirrel.core.usecases.data.account

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Data

class UpdateAccount {
	operator fun invoke(data: Data, dirIndex: Int, accountIndex: Int, newAccount: Account): Data {
		return data.copy(
			directories = data.directories.mapIndexed { i, dir ->
				if (i == dirIndex)
					dir.copy(
						accounts = data.directories[dirIndex].accounts.mapIndexed { j, account ->
							if (j == accountIndex)
								newAccount
							else account.copy()
						}
					)
				else dir.copy()
			}
		)
	}
}