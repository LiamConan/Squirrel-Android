package com.layne.squirrel.usecases

import com.layne.squirrel.core.data.KeysDataSource
import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.fixture.CreditCardFixture
import com.layne.squirrel.fixture.DataFixture
import com.layne.squirrel.framework.interactor.KeysInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.junit.Assert
import org.junit.Test

class CreditCardUseCasesTest {

	private val data = DataFixture.Builder().build()
	private val repository = KeysRepository(object : KeysDataSource {
		override suspend fun read(path: String, password: String): Data? {
			return data
		}

		override suspend fun write(data: Data, uri: String, password: String) {}
	})
	private val interactor: KeysInteractor = KeysInteractor(repository)

	@Test
	fun `GIVEN data WHEN adding card THEN card is well added`() {
		// Given
		val card = CreditCardFixture.Builder().build()

		// When
		val result = interactor.addCreditCard(data, card)

		// Then
		Assert.assertEquals(data.creditCards.size + 1, result.creditCards.size)
		Assert.assertEquals(card, result.creditCards.last())
	}

	@Test
	fun `GIVEN data WHEN updating card THEN card is well updated`() {
		// Given
		val cardIndex = 0
		val newCard = CreditCardFixture.Builder(
			number = "9302859483624928",
			expiry = "0223",
			denomination = 1,
			firstname = "JEANNE",
			name = "DOE",
			cvv = "047"
		).build()

		// When
		val result = interactor.updateCreditCard(data, cardIndex, newCard)

		// Then
		Assert.assertEquals(data.creditCards.size, result.creditCards.size)
		Assert.assertNotEquals(data.creditCards[cardIndex], result.creditCards[cardIndex])
		Assert.assertEquals(newCard, result.creditCards[cardIndex])
	}

	@Test
	fun `GIVEN data WHEN deleting directory THEN directory is well deleted`() {
		// Given
		val cardIndex = 0

		// When
		val result = interactor.deleteCreditCard(data, cardIndex)

		// Then
		Assert.assertEquals(data.creditCards.size - 1, result.creditCards.size)
	}
}