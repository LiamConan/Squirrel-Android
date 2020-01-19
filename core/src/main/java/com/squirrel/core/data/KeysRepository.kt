package com.squirrel.core.data

import com.squirrel.core.domain.Directory

class KeysRepository(private val dataSource: KeysDataSource) {

	suspend fun read(path: String, password: String): List<Directory>? =
		dataSource.read(path, password)

	suspend fun write(data: List<Directory>) = dataSource.write(data)
}