package com.layne.squirrel.framework.gateway.file.gson

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.layne.squirrel.core.domain.FilePreferences

data class FilePreferencesEntity(
	@SerializedName("path")
	var path: String,
	@SerializedName("passwordSaved")
	var passwordSaved: Boolean,
	@SerializedName("biometricsSaved")
	var biometricsSaved: Boolean,
	@SerializedName("password")
	var password: String
) {
	companion object {
		fun build(preferences: FilePreferences): FilePreferencesEntity =
			FilePreferencesEntity(
				preferences.path,
				preferences.passwordSaved,
				preferences.biometricsSaved,
				preferences.password
			)
	}

	override fun toString(): String {
		return Gson().toJson(this)
	}

	fun toFilePreferences() = FilePreferences(path, passwordSaved, biometricsSaved, password)
}