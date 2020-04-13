package com.layne.squirrel.core.usecases.preferences.autofill

import com.layne.squirrel.core.data.PreferencesRepository

class GetAutofillNeedAsk(private val repository: PreferencesRepository) {
	suspend operator fun invoke() = repository.readNeedAskAutofill()
}