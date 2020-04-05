package com.layne.squirrel.framework

import com.layne.squirrel.core.usecases.lastfilepath.GetLastFilePath
import com.layne.squirrel.core.usecases.lastfilepath.SaveLastFilePath

class LastFileUseCases(
	val getLastFilePath: GetLastFilePath,
	val saveLastFilePath: SaveLastFilePath
)