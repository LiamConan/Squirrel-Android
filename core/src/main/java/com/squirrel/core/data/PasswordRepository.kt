package com.squirrel.core.data

class PasswordRepository(private val dataSource: PasswordDataSource): PasswordDataSource {

	override suspend fun exists(key: String): Boolean = dataSource.exists(key)

	override suspend fun read(key: String): String = dataSource.read(key)

	override suspend fun write(key: String, password: String) = dataSource.write(key, password)

	override suspend fun delete(key: String) = dataSource.delete(key)
}