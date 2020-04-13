package com.layne.squirrel.core.usecases.preferences.autofill

import com.layne.squirrel.core.data.PreferencesRepository

class SetNeverAskAgainAutofill(private val repository: PreferencesRepository) {
	suspend operator fun invoke(value: Boolean) = repository.setNeverAskAgainAutofill(value)
}