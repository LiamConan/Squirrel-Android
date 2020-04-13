package com.layne.squirrel.framework.gateway

import android.content.Context
import android.os.Build
import android.view.autofill.AutofillManager
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.layne.squirrel.core.data.PreferencesDataSource
import com.layne.squirrel.core.domain.FilePreferences
import com.layne.squirrel.framework.gateway.file.gson.FilePreferencesEntity
import java.util.*

class SharedPreferencesDataSource(private val context: Context) : PreferencesDataSource {

	companion object {
		const val PREF_LAST_URI = "last_uri"
		const val PREF_LAST_ASKED_AUTOFILL = "last_autofill_asked"
		const val PREF_NEVER_ASK_AGAIN_AUTOFILL = "never_ask_again_for_autofill"
	}

	private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
	private val hasFilePath: Boolean = prefs.contains(PREF_LAST_URI)

	override suspend fun readFilePath(): String {
		return if (hasFilePath)
			prefs.getString(PREF_LAST_URI, "") ?: ""
		else ""
	}

	override suspend fun writeFilePath(path: String) {
		prefs.edit().putString(PREF_LAST_URI, path).apply()
	}

	override suspend fun readNeedAskAutofill(): Boolean {
		return when {
			getNeverAskAgainAutofill()                     -> false
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
				val manager =
					context.getSystemService(AutofillManager::class.java) as AutofillManager
				val needToAsk = manager.isAutofillSupported && !manager.hasEnabledAutofillServices()
				val lastTimeAsked = prefs.getLong(PREF_LAST_ASKED_AUTOFILL, 0)
				val now = Calendar.getInstance().timeInMillis
				val aDay = 80400000
				val timeToAsk = now - aDay > lastTimeAsked

				needToAsk && timeToAsk
			}
			else                                           -> false
		}
	}

	override suspend fun writeLastAskedAutofill() {
		prefs.edit().putLong(PREF_LAST_ASKED_AUTOFILL, Calendar.getInstance().timeInMillis).apply()
	}

	override suspend fun getNeverAskAgainAutofill(): Boolean =
		prefs.getBoolean(PREF_NEVER_ASK_AGAIN_AUTOFILL, false)

	override suspend fun setNeverAskAgainAutofill(value: Boolean) {
		prefs.edit().putBoolean(PREF_NEVER_ASK_AGAIN_AUTOFILL, value).apply()
	}

	override suspend fun getFilePreferences(key: String): FilePreferences = Gson().fromJson(
		prefs.getString(key, Gson().toJson(FilePreferences(path = key))),
		FilePreferencesEntity::class.java
	).toFilePreferences()

	override suspend fun setFilePreferences(preferences: FilePreferences) {
		prefs.edit()
			.putString(preferences.path, FilePreferencesEntity.build(preferences).toString())
			.apply()
	}
}