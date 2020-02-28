package com.squirrel.core.usecases.keys

import com.squirrel.core.domain.Account
import com.squirrel.core.swap
import java.util.*

class SwapAccounts {
	operator fun invoke(accounts: MutableList<Account>, first: Int, second: Int) {
		accounts.swap(first, second)
	}
}