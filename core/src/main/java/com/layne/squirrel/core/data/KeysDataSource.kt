package com.layne.squirrel.core.data

import com.layne.squirrel.core.domain.Data

interface KeysDataSource {

	suspend fun read(path: String, password: String): Data?

	suspend fun write(data: Data, uri: String, password: String)
}