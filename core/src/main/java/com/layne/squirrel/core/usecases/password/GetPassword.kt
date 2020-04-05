package com.layne.squirrel.core.usecases.password

import com.layne.squirrel.core.data.PasswordRepository

class GetPassword(private val dataSource: PasswordRepository) {
	suspend operator fun invoke(key: String) = dataSource.read(key)
}