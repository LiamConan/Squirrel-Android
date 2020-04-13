package com.layne.squirrel.core.data

interface PasswordDataSource {

	suspend fun read(key: String): String

	suspend fun write(key: String, password: String)

	suspend fun delete(key: String)
}