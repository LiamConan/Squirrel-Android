package com.squirrel

import com.google.gson.Gson
import com.squirrel.core.data.KeysDataSource
import com.squirrel.core.data.KeysRepository
import com.squirrel.core.domain.Account
import com.squirrel.core.domain.Directory
import com.squirrel.core.domain.Key
import com.squirrel.core.usecases.keys.*
import com.squirrel.framework.KeysUseCases
import com.squirrel.framework.gateway.preferences.gson.DataEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test

class DataTest {

	private val json =
		"{\"dirs\":[{\"name\":\"Clés\",\"keys\":[{\"date\":\"Lundi 30 Décembre 2019\",\"subkeys\":[{\"mail\":\"\",\"note\":\"\",\"password\":\"blablabla\",\"url\":\"\",\"user\":\"the-kid2005@hotmail.fr\"}],\"name\":\"Adobe\"},{\"date\":\"Samedi 28 Avril 2018\",\"subkeys\":[{\"mail\":\"raven0320@yahoo.fr\",\"note\":\"\",\"password\":\"blablabla\",\"url\":\"http://www.airbnb.fr\",\"user\":\"Gabriel\"}],\"name\":\"Airbnb\"}]}]}"
	private val directories =
		Gson().fromJson(json, DataEntity::class.java).toDirectories().toMutableList()
	private val repository = KeysRepository(object : KeysDataSource {
		override suspend fun read(path: String, password: String): List<Directory>? {
			return directories
		}

		override suspend fun write(data: List<Directory>) {}
	})
	private val useCases: KeysUseCases = KeysUseCases(
		AddAccount(),
		AddDirectory(),
		DeleteAccount(),
		DeleteDirectory(),
		GetKeys(repository),
		RemitAccount(),
		RemitDirectory(),
		SaveKeys(repository),
		SwapAccounts(),
		SwapDirectories(),
		UpdateAccount()
	)

	@Test
	fun `GIVEN data WHEN deserialize THEN return corresponding object`() {

		Assert.assertEquals(1, directories.size)
		Assert.assertEquals("Clés", directories[0].title)
		Assert.assertEquals(2, directories[0].accounts.size)

		val adobe = directories[0].accounts[0]
		Assert.assertEquals("Adobe", adobe.title)
		Assert.assertEquals("Lundi 30 Décembre 2019", adobe.date)
		Assert.assertEquals(1, adobe.keys.size)
		Assert.assertEquals("the-kid2005@hotmail.fr", adobe.keys[0].username)
		Assert.assertEquals("", adobe.keys[0].email)
		Assert.assertEquals("blablabla", adobe.keys[0].password)
		Assert.assertEquals("", adobe.keys[0].url)

		val airbnb = directories[0].accounts[1]
		Assert.assertEquals("Airbnb", airbnb.title)
		Assert.assertEquals("Samedi 28 Avril 2018", airbnb.date)
		Assert.assertEquals(1, airbnb.keys.size)
		Assert.assertEquals("Gabriel", airbnb.keys[0].username)
		Assert.assertEquals("raven0320@yahoo.fr", airbnb.keys[0].email)
		Assert.assertEquals("blablabla", airbnb.keys[0].password)
		Assert.assertEquals("http://www.airbnb.fr", airbnb.keys[0].url)
	}

	@Test
	fun `GIVEN data WHEN adding directory THEN directory is well added`() {
		val directory = Directory("test", mutableListOf())

		useCases.addDirectory(directories, directory)

		Assert.assertEquals(2, directories.size)
		Assert.assertEquals(directory, directories[1])
	}

	@Test
	fun `GIVEN data WHEN deleting directory THEN directory is well deleted`() {
		useCases.deleteDirectory(directories, 0)

		Assert.assertEquals(0, directories.size)
	}

	@Test
	fun `GIVEN data WHEN remit directory THEN directory is well remited`() {
		val directory = directories[0]
		useCases.deleteDirectory(directories, 0)
		useCases.remiteDirectory(directories, 0, directory)

		Assert.assertEquals(1, directories.size)
		Assert.assertEquals("Clés", directories[0].title)
		Assert.assertEquals(2, directories[0].accounts.size)
	}

	@Test
	fun `GIVEN data WHEN adding account THEN account is well added`() {
		val key = Key(
			"user",
			"test@test.fr",
			"test",
			"http://test.fr",
			"test"
		)
		val account = Account(
			"Test",
			"Lundi 30 Décembre 2019",
			mutableListOf(key)
		)

		useCases.addAccount(directories[0].accounts, account)

		Assert.assertEquals(3, directories[0].accounts.size)
		Assert.assertEquals(account, directories[0].accounts[2])
	}

	@Test
	fun `GIVEN data WHEN updating account THEN account is well updated`() {
		val key = Key(
			"user",
			"test@test.fr",
			"test",
			"http://test.fr",
			"test"
		)
		val account = Account(
			"Test",
			"Lundi 30 Décembre 2019",
			mutableListOf(key)
		)
		useCases.updateAccount(directories[0].accounts, 1, account)

		Assert.assertEquals(2, directories[0].accounts.size)
		Assert.assertEquals(account, directories[0].accounts[1])
	}

	@Test
	fun `GIVEN data WHEN deleting account THEN account is well deleted`() {
		useCases.deleteAccount(directories[0].accounts, 0)

		Assert.assertEquals(1, directories[0].accounts.size)
		Assert.assertEquals("Airbnb", directories[0].accounts[0].title)
	}

	@Test
	fun `GIVEN data WHEN remit account THEN account is well remited`() {
		val account = directories[0].accounts[0]
		useCases.deleteAccount(directories[0].accounts, 0)
		useCases.remiteAccount(directories[0].accounts, 0, account)

		Assert.assertEquals(2, directories[0].accounts.size)
		Assert.assertEquals("Adobe", directories[0].accounts[0].title)
	}

	@Test
	fun `GIVEN use case WHEN get keys THEN returns keys`() {
		GlobalScope.launch {
			val data = useCases.getKeys("test", "test")
			Assert.assertEquals(directories, data)
		}
	}
}