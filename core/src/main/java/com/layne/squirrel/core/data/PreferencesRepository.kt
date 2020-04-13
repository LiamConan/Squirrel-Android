package com.layne.squirrel.core.data

import com.layne.squirrel.core.domain.FilePreferences

class PreferencesRepository(private val dataSource: PreferencesDataSource) {

	suspend fun readFilePath(): String = dataSource.readFilePath()

	suspend fun writeFilePath(path: String) = dataSource.writeFilePath(path)

	suspend fun readNeedAskAutofill(): Boolean = dataSource.readNeedAskAutofill()

	suspend fun writeLastAskedAutofill() = dataSource.writeLastAskedAutofill()

	suspend fun getNeverAskAgainAutofill() = dataSource.getNeverAskAgainAutofill()

	suspend fun setNeverAskAgainAutofill(value: Boolean) =
		dataSource.setNeverAskAgainAutofill(value)

	suspend fun getFilePreferences(key: String) = dataSource.getFilePreferences(key)

	suspend fun setFilePreferences(preferences: FilePreferences) =
		dataSource.setFilePreferences(preferences)
}