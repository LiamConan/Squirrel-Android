package com.layne.squirrel.core.usecases.data.creditcard

import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.core.swap

class SwapCreditCards {
	operator fun invoke(data: Data, first: Int, second: Int): Data {
		return data.copy(
			creditCards = data.creditCards.toMutableList().apply {
				swap(first, second)
			}
		)
	}
}