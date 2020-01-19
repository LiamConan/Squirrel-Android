package com.squirrel.core.usecases.lastfilepath

import com.squirrel.core.data.LastFilePathDataSource

class GetLastFilePath(private val dataSource: LastFilePathDataSource) {
	suspend operator fun invoke() = dataSource.read()
}