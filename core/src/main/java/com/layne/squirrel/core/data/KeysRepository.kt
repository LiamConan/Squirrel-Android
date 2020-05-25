package com.layne.squirrel.core.data

import com.layne.squirrel.core.domain.Data

class KeysRepository(private val dataSource: KeysDataSource) {

	suspend fun read(path: String, password: String): Data? =
		dataSource.read(path, password)

	suspend fun write(data: Data, uri: String, password: String) =
		dataSource.write(data, uri, password)
}