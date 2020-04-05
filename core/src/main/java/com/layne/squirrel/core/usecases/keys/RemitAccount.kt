package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Account

class RemitAccount {
	operator fun invoke(accounts: MutableList<Account>, accountPosition: Int, account: Account) {
		accounts.add(accountPosition, account)
	}
}