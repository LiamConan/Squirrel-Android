package com.layne.squirrel.core.usecases.data.creditcard

import com.layne.squirrel.core.domain.Data

class DeleteCreditCard {
	operator fun invoke(data: Data, index: Int): Data {
		return data.copy(
			creditCards = data.creditCards.mapIndexed { i, card ->
				if (i == index)
					null
				else card.copy()
			}.filterNotNull()
		)
	}
}