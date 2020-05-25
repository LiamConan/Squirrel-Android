package com.layne.squirrel.fixture

import com.layne.squirrel.core.domain.CreditCard

object CreditCardFixture {

	data class Builder(
		val number: String = "5516837204737492",
		val expiry: String = "0522",
		val denomination: Int = 0,
		val firstname: String = "JOHN",
		val name: String = "DOE",
		val cvv: String = "839"
	) {
		fun build() = CreditCard(
			number = number,
			expiry = expiry,
			denominaton = denomination,
			firstname = firstname,
			name = name,
			cvv = cvv
		)
	}
}