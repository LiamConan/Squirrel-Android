package com.layne.squirrel.core.usecases.data

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Data

class SearchKeys {
	operator fun invoke(data: Data, applicationName: String): List<Account> {
		return data.directories.map {
			it.accounts.filter { account ->
				applicationName.equals(account.title, ignoreCase = true)
			}
		}.flatten()
	}
}