package com.layne.squirrel.usecases

import com.layne.squirrel.core.data.KeysDataSource
import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.fixture.AccountFixture
import com.layne.squirrel.fixture.DataFixture
import com.layne.squirrel.fixture.KeyFixture
import com.layne.squirrel.framework.interactor.KeysInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AccountUseCasesTest {

	private val data = DataFixture.Builder().build()
	private val repository = KeysRepository(object : KeysDataSource {
		override suspend fun read(path: String, password: String): Data? {
			return data
		}

		override suspend fun write(data: Data, uri: String, password: String) {}
	})
	private val interactor: KeysInteractor = KeysInteractor(repository)

	@Before
	fun init() {

	}

	@Test
	fun `GIVEN data WHEN adding account THEN account is well added`() {
		// Given
		val directoryIndex = 0
		val account = AccountFixture.Builder(
			"Test",
			"Lundi 30 Décembre 2019",
			mutableListOf(
				KeyFixture.Builder(
					"user",
					"test@test.fr",
					"test",
					"http://test.fr",
					"test"
				).build()
			)
		).build()

		// When
		val result = interactor.addAccount(data, account, directoryIndex)

		// Then
		Assert.assertEquals(
			data.directories[directoryIndex].accounts.size + 1,
			result.directories[directoryIndex].accounts.size
		)
		Assert.assertEquals(account, result.directories[directoryIndex].accounts.last())
	}

	@Test
	fun `GIVEN data WHEN updating account THEN account is well updated`() {
		// Given
		val directoryIndex = 0
		val accountIndex = 1
		val account = AccountFixture.Builder(
			"Test",
			"Lundi 30 Décembre 2019",
			mutableListOf(
				KeyFixture.Builder(
					"user",
					"test@test.fr",
					"test",
					"http://test.fr",
					"test"
				).build()
			)
		).build()

		// When
		val result = interactor.updateAccount(data, directoryIndex, accountIndex, account)

		// Then
		Assert.assertEquals(
			data.directories[directoryIndex].accounts.size,
			result.directories[directoryIndex].accounts.size
		)
		Assert.assertNotEquals(data, result)
		Assert.assertEquals(account, result.directories[directoryIndex].accounts[accountIndex])
	}

	@Test
	fun `GIVEN data WHEN swap accounts THEN accounts are swapped`() {
		// Given
		val directoryIndex = 0
		val from = 0
		val to = 1

		// When
		val result = interactor.swapAccounts(data, directoryIndex, from, to)

		// Then
		Assert.assertEquals(
			data.directories[directoryIndex].accounts.size,
			result.directories[directoryIndex].accounts.size
		)
		Assert.assertNotEquals(data, result)
		Assert.assertEquals(
			data.directories[directoryIndex].accounts[from],
			result.directories[directoryIndex].accounts[to]
		)
		Assert.assertEquals(
			data.directories[directoryIndex].accounts[to],
			result.directories[directoryIndex].accounts[from]
		)
	}

	@Test
	fun `GIVEN data WHEN remit account THEN account is well remited`() {
		// Given
		val directoryIndex = 0
		val accountIndex = 0
		val account = data.directories[directoryIndex].accounts[accountIndex]

		// When
		val whenDeleted = interactor.deleteAccount(data, directoryIndex, accountIndex)
		val result = interactor.addAccount(whenDeleted, account, directoryIndex, accountIndex)

		// Then
		Assert.assertEquals(
			data.directories[directoryIndex].accounts.size,
			result.directories[directoryIndex].accounts.size
		)
		Assert.assertEquals(
			account.title,
			result.directories[directoryIndex].accounts[accountIndex].title
		)
	}

	@Test
	fun `GIVEN data WHEN deleting account THEN account is well deleted`() {
		// Given
		val directoryIndex = 0
		val accountIndex = 0

		// When
		val result = interactor.deleteAccount(data, directoryIndex, accountIndex)

		// Then
		Assert.assertEquals(
			data.directories[directoryIndex].accounts.size - 1,
			result.directories[directoryIndex].accounts.size
		)
		Assert.assertEquals(
			AccountFixture.netflix.title,
			result.directories[directoryIndex].accounts[accountIndex].title
		)
	}
}