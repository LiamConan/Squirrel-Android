package com.layne.squirrel.core.usecases.preferences.filepath

import com.layne.squirrel.core.data.PreferencesRepository

class GetLastFilePath(private val repository: PreferencesRepository) {
	suspend operator fun invoke() = repository.readFilePath()
}