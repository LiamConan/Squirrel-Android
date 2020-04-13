package com.layne.squirrel.core.usecases.preferences.autofill

import com.layne.squirrel.core.data.PreferencesRepository

class SetAutofillLastAsked(private val repository: PreferencesRepository) {
	suspend operator fun invoke() = repository.writeLastAskedAutofill()
}