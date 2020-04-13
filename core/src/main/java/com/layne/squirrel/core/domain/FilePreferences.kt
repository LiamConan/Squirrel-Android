package com.layne.squirrel.core.domain

data class FilePreferences(
	var path: String = "",
	var passwordSaved: Boolean = false,
	var biometricsSaved: Boolean = false,
	var password: String = ""
)