package com.layne.squirrel.fixture

import com.layne.squirrel.core.domain.CreditCard
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.core.domain.Directory

object DataFixture {

	data class Builder(
		val directories: List<Directory> = listOf(
			DirectoryFixture.Builder().build()),
		val cards: List<CreditCard> = listOf(CreditCardFixture.Builder().build())
	) {
		fun build() = Data(
			directories = directories,
			creditCards = cards
		)
	}
}