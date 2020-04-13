package com.layne.squirrel.core.usecases.preferences.filepath

import com.layne.squirrel.core.data.PreferencesRepository

class SaveLastFilePath(private val repository: PreferencesRepository) {
	suspend operator fun invoke(path: String) = repository.writeFilePath(path)
}