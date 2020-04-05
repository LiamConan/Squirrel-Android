package com.layne.squirrel.framework.gateway

import android.content.Context
import androidx.preference.PreferenceManager
import com.layne.squirrel.core.data.PasswordDataSource

class PreferencesPasswordDataSource(context: Context): PasswordDataSource {

	private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

	override suspend fun exists(key: String): Boolean = prefs.contains(key)

	override suspend fun read(key: String): String = prefs.getString(key, "") ?: ""

	override suspend fun write(key: String, password: String) {
		prefs.edit().putString(key, password).apply()
	}

	override suspend fun delete(key: String) {
		prefs.edit().remove(key).apply()
	}
}