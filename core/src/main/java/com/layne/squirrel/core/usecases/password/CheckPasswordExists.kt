package com.layne.squirrel.core.usecases.password

import com.layne.squirrel.core.data.PasswordRepository

class CheckPasswordExists(private val dataSource: PasswordRepository) {
	suspend operator fun invoke(key: String): Boolean = dataSource.exists(key)
}