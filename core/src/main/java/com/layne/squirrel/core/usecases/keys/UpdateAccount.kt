package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Account

class UpdateAccount {
	operator fun invoke(accounts: MutableList<Account>, accountIndex: Int, account: Account) {
		accounts[accountIndex] = account
	}
}