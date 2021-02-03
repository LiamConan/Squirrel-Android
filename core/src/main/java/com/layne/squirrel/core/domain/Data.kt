package com.layne.squirrel.core.domain

import com.google.gson.annotations.SerializedName

data class Data(
	@SerializedName("dirs")
	var directories: List<Directory>,
	@SerializedName("creditCards")
	var creditCards: List<CreditCard>
)
