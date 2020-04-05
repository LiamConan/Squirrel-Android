package com.layne.squirrel.core.data

import com.layne.squirrel.core.domain.Directory

interface KeysDataSource {

	suspend fun read(path: String, password: String): List<Directory>?

	suspend fun write(data: List<Directory>, uri: String, password: String)
}