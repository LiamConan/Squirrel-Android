package com.squirrel.framework

import com.squirrel.core.usecases.lastfilepath.GetLastFilePath
import com.squirrel.core.usecases.lastfilepath.SaveLastFilePath

class LastFileUseCases(
	val getLastFilePath: GetLastFilePath,
	val saveLastFilePath: SaveLastFilePath
)