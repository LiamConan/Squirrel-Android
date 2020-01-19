package com.squirrel.core.data

class LastFilePathRepository(private val dataSource: LastFilePathDataSource) :
	LastFilePathDataSource {

	override suspend fun exists() = dataSource.exists()

	override suspend fun read(): String = dataSource.read()

	override suspend fun write(path: String) = dataSource.write(path)
}