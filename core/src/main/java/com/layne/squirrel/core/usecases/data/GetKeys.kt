package com.layne.squirrel.core.usecases.data

import com.layne.squirrel.core.data.KeysRepository

class GetKeys(private val keysRepository: KeysRepository) {
	suspend operator fun invoke(path: String, password: String) =
		keysRepository.read(path, password)
}