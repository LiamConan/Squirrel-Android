package com.squirrel.core.usecases.password

import com.squirrel.core.data.PasswordRepository

class SavePassword(private val dataSource: PasswordRepository) {
	suspend operator fun invoke(key: String, password: String) =
		dataSource.write(key, password)
}