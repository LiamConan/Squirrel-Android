package com.layne.squirrel.core.usecases.data.creditcard

import com.layne.squirrel.core.domain.CreditCard
import com.layne.squirrel.core.domain.Data

class UpdateCreditCard {
	operator fun invoke(data: Data, index: Int, newCard: CreditCard): Data {
		return data.copy(
			creditCards = data.creditCards.mapIndexed { i, card ->
				if (i == index)
					newCard
				else card.copy()
			}
		)
	}
}