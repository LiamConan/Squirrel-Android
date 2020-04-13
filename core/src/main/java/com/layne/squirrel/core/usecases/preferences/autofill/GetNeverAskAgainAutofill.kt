package com.layne.squirrel.core.usecases.preferences.autofill

import com.layne.squirrel.core.data.PreferencesRepository

class GetNeverAskAgainAutofill(private val repository: PreferencesRepository) {
	suspend operator fun invoke(): Boolean = repository.getNeverAskAgainAutofill()
}