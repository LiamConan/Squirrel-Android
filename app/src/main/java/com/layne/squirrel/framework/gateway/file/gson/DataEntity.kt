package com.layne.squirrel.framework.gateway.file.gson

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.layne.squirrel.core.domain.Data

class DataEntity(
	@SerializedName("dirs")
	var directories: List<DirectoryEntity>,
	@SerializedName("creditCards")
	var creditCards: List<CreditCardEntity>
) {

	companion object {
		fun build(data: Data): DataEntity {
			return DataEntity(
				data.directories.map { DirectoryEntity.build(it) },
				data.creditCards.map { CreditCardEntity.build(it) }
			)
		}
	}

	override fun toString(): String {
		return Gson().toJson(this)
	}

	fun toData(): Data = Data(
		directories.map { it.toDirectory() },
		creditCards.map { it.toCreditCard() }
	)
}
