package com.layne.squirrel.framework.gateway

import android.content.Context
import androidx.preference.PreferenceManager
import com.layne.squirrel.core.data.LastFilePathDataSource

class PreferencesLastFilePathDataSource(context: Context) : LastFilePathDataSource {

	private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

	override suspend fun exists(): Boolean = prefs.contains("last_uri")

	override suspend fun read(): String {
		return if (exists())
			prefs.getString("last_uri", "") ?: ""
		else ""
	}

	override suspend fun write(path: String) {
		prefs.edit().putString("last_uri", path).apply()
	}
}