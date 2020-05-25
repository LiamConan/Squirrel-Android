package com.layne.squirrel.core.domain

data class Account(
	var title: String,
	var date: String = "",
	var keys: List<Key> = listOf()
)