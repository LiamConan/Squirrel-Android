package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Account

class DeleteAccount {
	operator fun invoke(accounts: MutableList<Account>, accountIndex: Int) {
		accounts.removeAt(accountIndex)
	}
}