package com.layne.squirrel.fixture

import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.fixture.AccountFixture

object DirectoryFixture {

	data class Builder(
		val title: String = "Clés",
		val accounts: List<Account> = listOf(
			AccountFixture.airbnb,
			AccountFixture.netflix
		)
	) {
		fun build() = Directory(
			title,
			accounts
		)
	}
}