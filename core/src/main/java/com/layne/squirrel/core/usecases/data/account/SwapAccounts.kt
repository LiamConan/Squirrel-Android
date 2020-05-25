package com.layne.squirrel.core.usecases.data.account

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.core.domain.Directory

class SwapAccounts {

	operator fun invoke(data: Data, dirIndex: Int, first: Int, second: Int): Data = data.copy(
		directories = copyDirectories(data.directories, dirIndex, first, second)
	)

	private fun copyDirectories(
		directories: List<Directory>,
		dirIndex: Int,
		first: Int,
		second: Int
	): List<Directory> = directories.mapIndexed { i, dir ->
		if (i == dirIndex) {
			dir.copy(
				accounts = copyAccounts(dir.accounts, first, second)
			)
		} else dir.copy()
	}

	private fun copyAccounts(accounts: List<Account>, first: Int, second: Int): List<Account> {
		val firstAccount = accounts[first]
		val secondAccount = accounts[second]
		return accounts.mapIndexed { j, account ->
			when (j) {
				first  -> secondAccount
				second -> firstAccount
				else   -> account.copy()
			}
		}
	}
}