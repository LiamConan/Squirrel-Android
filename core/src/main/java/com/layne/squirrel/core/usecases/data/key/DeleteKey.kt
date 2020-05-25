package com.layne.squirrel.core.usecases.data.key

import com.layne.squirrel.core.domain.Data

class DeleteKey {
	operator fun invoke(
		data: Data,
		dirIndex: Int,
		accountIndex: Int,
		keyIndex: Int
	): Data {
		val size = data.directories[dirIndex].accounts[accountIndex].keys.size
		return if (size > 1)
			deleteKey(data, dirIndex, accountIndex, keyIndex)
		else
			deleteAccount(data, dirIndex, accountIndex)
	}

	private fun deleteKey(
		data: Data,
		dirIndex: Int,
		accountIndex: Int,
		keyIndex: Int
	): Data = data.copy(
		directories = data.directories.mapIndexed { i, dir ->
			if (i == dirIndex) {
				dir.copy(
					accounts = dir.accounts.mapIndexed { j, account ->
						if (j == accountIndex) {
							account.copy(
								keys = account.keys.mapIndexed { k, key ->
									if (k == keyIndex)
										null
									else key.copy()
								}.filterNotNull()
							)
						} else account.copy()
					}
				)
			} else dir.copy()
		}
	)

	private fun deleteAccount(data: Data, dirIndex: Int, accountIndex: Int): Data = data.copy(
		directories = data.directories.mapIndexed { i, dir ->
			if (i == dirIndex) {
				dir.copy(
					accounts = dir.accounts.mapIndexed { j, account ->
						if (j == accountIndex)
							null
						else
							account.copy()
					}.filterNotNull()
				)
			} else dir.copy()
		}
	)
}