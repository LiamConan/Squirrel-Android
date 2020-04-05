package com.layne.squirrel.core.usecases.lastfilepath

import com.layne.squirrel.core.data.LastFilePathDataSource

class GetLastFilePath(private val dataSource: LastFilePathDataSource) {
	suspend operator fun invoke() = dataSource.read()
}