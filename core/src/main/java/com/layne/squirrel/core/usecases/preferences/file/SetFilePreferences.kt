package com.layne.squirrel.core.usecases.preferences.file

import com.layne.squirrel.core.data.PreferencesRepository
import com.layne.squirrel.core.domain.FilePreferences

class SetFilePreferences(private val repository: PreferencesRepository) {
	suspend operator fun invoke(preferences: FilePreferences) =
		repository.setFilePreferences(preferences)
}