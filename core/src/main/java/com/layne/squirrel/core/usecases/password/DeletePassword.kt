package com.layne.squirrel.core.usecases.password

import com.layne.squirrel.core.data.PasswordRepository

class DeletePassword(private val repository: PasswordRepository) {
	suspend operator fun invoke(key: String) = repository.delete(key)
}