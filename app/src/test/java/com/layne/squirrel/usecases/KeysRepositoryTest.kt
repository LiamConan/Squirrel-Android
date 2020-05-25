package com.layne.squirrel.usecases

import com.layne.squirrel.core.data.KeysDataSource
import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.fixture.AccountFixture
import com.layne.squirrel.fixture.DataFixture
import com.layne.squirrel.framework.interactor.KeysInteractor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test

class KeysRepositoryTest {

	private val data = DataFixture.Builder().build()
	private val repository = KeysRepository(object : KeysDataSource {
		override suspend fun read(path: String, password: String): Data? {
			return data
		}

		override suspend fun write(data: Data, uri: String, password: String) {}
	})
	private val interactor: KeysInteractor = KeysInteractor(repository)

	@Test
	fun `GIVEN use case WHEN get keys THEN returns keys`() {
		GlobalScope.launch {
			val data = interactor.getKeys("test", "test")
			Assert.assertEquals(data, data)
		}
	}

	@Test
	fun `GIVEN a keyword WHEN search on keys THEN returns expected keys`() {
		// Given
		val keywork = "airbnb"

		// When
		val keys = interactor.searchKeys(data.directories, keywork)

		// Then
		Assert.assertEquals(1, keys.size)
		Assert.assertEquals(AccountFixture.airbnb.title, keys.first().title)
	}
}