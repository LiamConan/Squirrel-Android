package com.layne.squirrel.framework.interactor

import com.layne.squirrel.core.data.PreferencesRepository
import com.layne.squirrel.core.usecases.preferences.autofill.GetAutofillNeedAsk
import com.layne.squirrel.core.usecases.preferences.autofill.SetAutofillLastAsked
import com.layne.squirrel.core.usecases.preferences.autofill.SetNeverAskAgainAutofill
import com.layne.squirrel.core.usecases.preferences.file.GetFilePreferences
import com.layne.squirrel.core.usecases.preferences.file.SetFilePreferences
import com.layne.squirrel.core.usecases.preferences.filepath.GetLastFilePath
import com.layne.squirrel.core.usecases.preferences.filepath.SaveLastFilePath

class PreferencesInteractor(
	repository: PreferencesRepository,
	val getFilePath: GetLastFilePath = GetLastFilePath(repository),
	val saveFilePath: SaveLastFilePath = SaveLastFilePath(repository),
	val getAutofillNeedAsk: GetAutofillNeedAsk = GetAutofillNeedAsk(repository),
	val setAutofillLastAsked: SetAutofillLastAsked = SetAutofillLastAsked(repository),
	val setNeverAskAgainAutofill: SetNeverAskAgainAutofill = SetNeverAskAgainAutofill(repository),
	val getFilePreferences: GetFilePreferences = GetFilePreferences(repository),
	val setFilePreferences: SetFilePreferences = SetFilePreferences(repository)
)