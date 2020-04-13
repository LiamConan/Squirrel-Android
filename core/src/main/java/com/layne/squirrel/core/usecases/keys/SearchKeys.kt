package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Directory
import java.util.*

class SearchKeys {
	operator fun invoke(data: List<Directory>, keyword: String): List<Account> {
		val res = mutableListOf<Account>()
		data.forEach {
			res += it.accounts.filter { account ->
				keyword.toLowerCase(Locale.FRENCH)
					.contains(account.title.toLowerCase(Locale.FRENCH))
			}
		}
		return res
	}
}