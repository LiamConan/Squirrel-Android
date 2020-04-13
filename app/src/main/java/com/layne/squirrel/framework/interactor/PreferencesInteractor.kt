package com.layne.squirrel.framework.interactor

import com.layne.squirrel.core.usecases.preferences.autofill.GetAutofillNeedAsk
import com.layne.squirrel.core.usecases.preferences.autofill.SetAutofillLastAsked
import com.layne.squirrel.core.usecases.preferences.autofill.SetNeverAskAgainAutofill
import com.layne.squirrel.core.usecases.preferences.file.GetFilePreferences
import com.layne.squirrel.core.usecases.preferences.file.SetFilePreferences
import com.layne.squirrel.core.usecases.preferences.filepath.GetLastFilePath
import com.layne.squirrel.core.usecases.preferences.filepath.SaveLastFilePath

class PreferencesInteractor(
	val getFilePath: GetLastFilePath,
	val saveFilePath: SaveLastFilePath,
	val getAutofillNeedAsk: GetAutofillNeedAsk,
	val setAutofillLastAsked: SetAutofillLastAsked,
	val setNeverAskAgainAutofill: SetNeverAskAgainAutofill,
	val getFilePreferences: GetFilePreferences,
	val setFilePreferences: SetFilePreferences
)