package com.layne.squirrel.fixture

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Key

object AccountFixture {

	data class Builder(
		val title: String = "",
		val date: String = "",
		val keys: List<Key> = listOf()
	) {
		fun build() = Account(
			title,
			date,
			keys
		)
	}

	val airbnb = Builder(
		title = "Airbnb",
		date = "Lundi 30 Décembre 2019",
		keys = listOf(
			KeyFixture.Builder(
				username = "John",
				email = "john.doe@gmail.com",
				password = "password",
				url = "airbnb.com",
				note = ""
			).build()
		)
	).build()

	val netflix = Builder(
		title = "Netflix",
		date = "Mardi 31 Décembre 2019",
		keys = listOf(
			KeyFixture.Builder(
				username = "John",
				email = "john.doe@outlook.com",
				password = "password",
				url = "netflix.com",
				note = ""
			).build()
		)
	).build()
}