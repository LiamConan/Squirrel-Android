package com.layne.squirrel.core.usecases.data.creditcard

import com.layne.squirrel.core.domain.CreditCard
import com.layne.squirrel.core.domain.Data

class AddCreditCard {
	operator fun invoke(data: Data, creditCard: CreditCard, index: Int? = null): Data {
		return if (index != null) {
			val cards = data.creditCards.map { it.copy() }.toMutableList()
			data.copy(creditCards = cards.apply { add(index, creditCard) })
		} else data.copy(creditCards = data.creditCards + creditCard)
	}
}