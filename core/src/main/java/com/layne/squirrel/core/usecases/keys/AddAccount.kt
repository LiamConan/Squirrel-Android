package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Account

class AddAccount {
	operator fun invoke(accounts: MutableList<Account>, account: Account) {
		var alreadyExists = false
		accounts.forEach {
			if (it.title == account.title) {
				it.keys.addAll(account.keys)
				alreadyExists = true
			}
		}
		if (!alreadyExists)
			accounts.add(account)
	}
}