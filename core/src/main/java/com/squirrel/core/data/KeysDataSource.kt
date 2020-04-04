package com.squirrel.core.data

import com.squirrel.core.domain.Directory

interface KeysDataSource {

	suspend fun read(path: String, password: String): List<Directory>?

	suspend fun write(data: List<Directory>, uri: String, password: String)
}