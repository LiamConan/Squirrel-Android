package com.layne.squirrel.usecases

import com.layne.squirrel.core.data.KeysDataSource
import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.fixture.DataFixture
import com.layne.squirrel.fixture.DirectoryFixture
import com.layne.squirrel.framework.interactor.KeysInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.junit.Assert
import org.junit.Test

class DirectoryUseCasesTest {

	private val data = DataFixture.Builder().build()
	private val repository = KeysRepository(object : KeysDataSource {
		override suspend fun read(path: String, password: String): Data? {
			return data
		}

		override suspend fun write(data: Data, uri: String, password: String) {}
	})
	private val interactor: KeysInteractor = KeysInteractor(repository)

	@Test
	fun `GIVEN data WHEN adding directory THEN directory is well added`() {
		// Given
		val directory = DirectoryFixture.Builder().build()

		// When
		val result = interactor.addDirectory(data, directory)

		// Then
		Assert.assertEquals(data.directories.size + 1, result.directories.size)
		Assert.assertEquals(directory, result.directories.last())
	}

	@Test
	fun `GIVEN data WHEN renaming directory THEN directory is well renamed`() {
		// Given
		val directoryIndex = 0
		val newName = "newName"

		// When
		val result = interactor.renameDirectory(data, directoryIndex, newName)

		// Then
		Assert.assertEquals(data.directories.size, result.directories.size)
		Assert.assertNotEquals(
			data.directories[directoryIndex].title,
			result.directories[directoryIndex].title
		)
		Assert.assertEquals(newName, result.directories[directoryIndex].title)
	}

	@Test
	fun `GIVEN data WHEN swap accounts THEN accounts are swapped`() {
		// Given
		val from = 0
		val to = 1
		val directory = DirectoryFixture.Builder().build()

		// When
		val whenAdded = interactor.addDirectory(data, directory)
		val result = interactor.swapDirectories(whenAdded, from, to)

		// Then
		Assert.assertEquals(whenAdded.directories.size, result.directories.size)
		Assert.assertNotEquals(data, result)
		Assert.assertEquals(whenAdded.directories[from], result.directories[to])
		Assert.assertEquals(whenAdded.directories[to], result.directories[from])
	}

	@Test
	fun `GIVEN data WHEN remit directory THEN directory is well remited`() {
		// Given
		val directoryIndex = 0
		val directory = data.directories[directoryIndex]

		// When
		val whenDeleted = interactor.deleteDirectory(data, directoryIndex)
		val result = interactor.addDirectory(whenDeleted, directory, directoryIndex)

		// Then
		Assert.assertEquals(data.directories.size, result.directories.size)
		Assert.assertEquals(data.directories[directoryIndex], result.directories[directoryIndex])
	}

	@Test
	fun `GIVEN data WHEN deleting directory THEN directory is well deleted`() {
		// Given
		val directoryIndex = 0

		// When
		val result = interactor.deleteDirectory(data, directoryIndex)

		// Then
		Assert.assertEquals(data.directories.size - 1, result.directories.size)
	}
}