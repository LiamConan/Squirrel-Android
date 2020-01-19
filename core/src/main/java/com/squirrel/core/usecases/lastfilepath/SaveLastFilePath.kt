package com.squirrel.core.usecases.lastfilepath

import com.squirrel.core.data.LastFilePathDataSource

class SaveLastFilePath(private val dataSource: LastFilePathDataSource) {
	suspend operator fun invoke(path: String) = dataSource.write(path)
}