package com.layne.squirrel.core.data

import com.layne.squirrel.core.domain.Directory

class KeysRepository(private val dataSource: KeysDataSource) {

	suspend fun read(path: String, password: String): List<Directory>? =
		dataSource.read(path, password)

	suspend fun write(data: List<Directory>, uri: String, password: String) =
		dataSource.write(data, uri, password)
}