package com.layne.squirrel.core.data

import com.layne.squirrel.core.domain.FilePreferences

interface PreferencesDataSource {

	suspend fun readFilePath(): String

	suspend fun writeFilePath(path: String)

	suspend fun readNeedAskAutofill(): Boolean

	suspend fun writeLastAskedAutofill()

	suspend fun getNeverAskAgainAutofill(): Boolean

	suspend fun setNeverAskAgainAutofill(value: Boolean)

	suspend fun getFilePreferences(key: String): FilePreferences

	suspend fun setFilePreferences(preferences: FilePreferences)
}