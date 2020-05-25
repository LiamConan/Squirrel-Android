package com.layne.squirrel.fixture

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.core.domain.Key

object KeyFixture {

	data class Builder(
		var username: String = "John",
		var email: String = "john.doe@gmail.com",
		var password: String = "password",
		var url: String = "airbnb.com",
		var note: String = ""
	) {
		fun build() = Key(
			username = username,
			email = email,
			password = password,
			url = url,
			note = note
		)
	}
}