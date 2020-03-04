package com.squirrel.framework.gateway.file.gson

import com.google.gson.annotations.SerializedName
import com.squirrel.core.domain.Key

data class KeyEntity(
	@SerializedName("user")
	var username: String,
	@SerializedName("mail")
	var email: String,
	@SerializedName("password")
	var password: String,
	@SerializedName("url")
	var url: String,
	@SerializedName("note")
	var note: String
) {
	companion object {
		fun build(key: Key): KeyEntity {
			return KeyEntity(key.username, key.email, key.password, key.url, key.note)
		}
	}

	fun toKey(): Key = Key(username, email, password, url, note)
}