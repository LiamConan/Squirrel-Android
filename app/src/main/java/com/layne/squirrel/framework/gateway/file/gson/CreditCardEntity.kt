package com.layne.squirrel.framework.gateway.file.gson

import com.google.gson.annotations.SerializedName
import com.layne.squirrel.core.domain.CreditCard

data class CreditCardEntity(
	@SerializedName("number")
	var number: String,
	@SerializedName("expiry")
	var expiry: String,
	@SerializedName("denomination")
	var denomination: Int,
	@SerializedName("firstname")
	var firstname: String,
	@SerializedName("name")
	var name: String,
	@SerializedName("cvv")
	var cvv: String
) {
	companion object {
		fun build(creditCard: CreditCard): CreditCardEntity {
			return CreditCardEntity(
				creditCard.number,
				creditCard.expiry,
				creditCard.denominaton,
				creditCard.firstname,
				creditCard.name,
				creditCard.cvv
			)
		}
	}

	fun toCreditCard(): CreditCard =
		CreditCard(number, expiry, denomination, firstname, name, cvv)
}