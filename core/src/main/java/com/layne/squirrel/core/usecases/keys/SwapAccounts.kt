package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.swap

class SwapAccounts {
	operator fun invoke(accounts: MutableList<Account>, first: Int, second: Int) {
		accounts.swap(first, second)
	}
}