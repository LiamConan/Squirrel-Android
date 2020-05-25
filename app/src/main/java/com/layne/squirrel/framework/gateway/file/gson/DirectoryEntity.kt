package com.layne.squirrel.framework.gateway.file.gson

import com.google.gson.annotations.SerializedName
import com.layne.squirrel.core.domain.Directory

data class DirectoryEntity(
	@SerializedName("name")
	var title: String,
	@SerializedName("keys")
	var accounts: List<AccountEntity>
) {
	companion object {
		fun build(directory: Directory): DirectoryEntity {
			return DirectoryEntity(
				directory.title,
				directory.accounts.map { AccountEntity.build(it) }
			)
		}
	}

	fun toDirectory(): Directory = Directory(title, accounts.map { it.toAccount() })
}