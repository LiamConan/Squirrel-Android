package com.layne.squirrel.core.domain

data class Directory(
	var title: String,
	var accounts: List<Account> = listOf()
)