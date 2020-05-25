package com.layne.squirrel.core.domain

data class DataHolder(
	var data: Data = Data(mutableListOf(), mutableListOf()),
	var password: String = "",
	var uri: String = ""
)