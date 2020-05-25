package com.layne.squirrel.core.usecases.data.account

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Data

class AddAccount {
	operator fun invoke(
		data: Data,
		newAccount: Account,
		dirIndex: Int,
		accountIndex: Int? = null
	): Data = if (accountIndex == null)
		addWithoutIndex(data, newAccount, dirIndex)
	else addWithIndex(data, newAccount, dirIndex, accountIndex)

	private fun addWithoutIndex(data: Data, newAccount: Account, dirIndex: Int): Data {
		val existingIndex: Int = data.directories[dirIndex].accounts.mapIndexed { i, it ->
			if (it.title == newAccount.title) i else -1
		}.find { it > -1 } ?: -1
		val alreadyExists = existingIndex > -1

		return data.copy(
			directories = data.directories.mapIndexed { i, dir ->
				when {
					i == dirIndex && alreadyExists -> {
						dir.copy(
							accounts = data.directories[i].accounts.mapIndexed { j, account ->
								if (j == existingIndex)
									account.copy(
										keys = data.directories[i].accounts[existingIndex].keys + newAccount.keys
									)
								else account.copy()
							}
						)
					}
					i == dirIndex                  -> dir.copy(accounts = data.directories[i].accounts + newAccount)
					else                           -> dir.copy()
				}
			}
		)
	}

	private fun addWithIndex(data: Data, newAccount: Account, dirIndex: Int, accountIndex: Int) =
		data.copy(
			directories = data.directories.mapIndexed { i, dir ->
				if (i == dirIndex) {
					val accounts = data.directories[i].accounts.map { it.copy() }
					dir.copy(
						accounts = accounts.toMutableList().apply { add(accountIndex, newAccount) }
					)
				} else dir.copy()
			}
		)
}