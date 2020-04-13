package com.layne.squirrel.core.usecases.preferences.file

import com.layne.squirrel.core.data.PreferencesRepository

class GetFilePreferences(private val repository: PreferencesRepository) {
	suspend operator fun invoke(key: String) = repository.getFilePreferences(key)
}