package com.layne.squirrel.core.usecases.password

import com.layne.squirrel.core.data.PasswordRepository

class SavePassword(private val repository: PasswordRepository) {
	suspend operator fun invoke(key: String, password: String) =
		repository.write(key, password)
}