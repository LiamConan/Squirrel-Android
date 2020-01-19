package com.squirrel.core.data

import com.squirrel.core.domain.Directory
import javax.xml.crypto.Data

interface KeysDataSource {

	suspend fun read(path: String, password: String): List<Directory>?

	suspend fun write(data: List<Directory>)
}