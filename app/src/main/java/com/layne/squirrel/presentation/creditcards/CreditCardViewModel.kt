package com.layne.squirrel.presentation.creditcards

import androidx.lifecycle.ViewModel
import com.layne.squirrel.core.domain.CreditCard
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.core.domain.DataHolder
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.interactor.KeysInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreditCardViewModel @Inject constructor(
	private var dataHolder: DataHolder
) : ViewModel(), CoroutineScope by MainScope() {

	@Inject
	lateinit var keysInteractor: KeysInteractor

	var data: Data
		get() = dataHolder.data
		set(value) {
			dataHolder.data = value
			saveData()
		}
	var cardPosition: Int = 0

	init {
		Squirrel.dagger.inject(this)
	}

	private fun saveData() {
		launch {
			keysInteractor.saveKeys(data, dataHolder.uri, dataHolder.password)
		}
	}

	fun addCreditCard(creditCard: CreditCard, i: Int? = null) {
		data = keysInteractor.addCreditCard(data, creditCard, i)
		saveData()
	}

	fun updateCreditCard(index: Int, card: CreditCard) {
		data = keysInteractor.updateCreditCard(data, index, card)
		saveData()
	}

	fun swapCreditCards(from: Int, to: Int) {
		data = keysInteractor.swapCreditCards(data, from, to)
		saveData()
	}

	fun deleteCreditCard(i: Int) {
		data = keysInteractor.deleteCreditCard(data, i)
		saveData()
	}
}