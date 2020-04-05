package com.layne.squirrel.core.data

interface LastFilePathDataSource {

	suspend fun exists(): Boolean

	suspend fun read(): String

	suspend fun write(path: String)
}