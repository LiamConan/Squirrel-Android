package com.squirrel.core.usecases.password

import com.squirrel.core.data.PasswordDataSource

class DeletePassword(private val dataSource: PasswordDataSource) {
	suspend operator fun invoke(key: String) = dataSource.delete(key)
}