package com.squirrel.core.usecases.keys

import com.squirrel.core.domain.Account

class AddAccount {
	operator fun invoke(accounts: MutableList<Account>, account: Account) {
		accounts.add(account)
	}
}