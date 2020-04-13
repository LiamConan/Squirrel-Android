package com.layne.squirrel.core.data

class PasswordRepository(private val dataSource: PasswordDataSource) {

	suspend fun read(key: String): String = dataSource.read(key)

	suspend fun write(key: String, password: String) = dataSource.write(key, password)

	suspend fun delete(key: String) = dataSource.delete(key)
}