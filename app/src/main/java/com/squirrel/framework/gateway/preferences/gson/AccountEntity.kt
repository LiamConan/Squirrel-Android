package com.squirrel.framework.gateway.preferences.gson

import com.google.gson.annotations.SerializedName
import com.squirrel.core.domain.Account

data class AccountEntity(
	@SerializedName("name")
	var title: String,
	@SerializedName("date")
	var date: String,
	@SerializedName("subkeys")
	var keys: MutableList<KeyEntity>
) {
	companion object {
		fun build(account: Account): AccountEntity {
			return AccountEntity(
				account.title,
				account.date,
				account.keys.map { KeyEntity.build(it) }.toMutableList()
			)
		}
	}

	fun toAccount(): Account = Account(title, date, keys.map { it.toKey() }.toMutableList())
}