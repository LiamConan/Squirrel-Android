package com.layne.squirrel.core.domain

data class CreditCard(
	var number: String = "",
	var expiry: String = "",
	var denominaton: Int = 0,
	var firstname: String = "",
	var name: String = "",
	var cvv: String = ""
) {
	val fullname = "$firstname $name"
}