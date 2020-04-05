package com.layne.squirrel.core.data

interface PasswordDataSource {

	suspend fun exists(key: String): Boolean

	suspend fun read(key: String): String

	suspend fun write(key: String, password: String)

	suspend fun delete(key: String)
}