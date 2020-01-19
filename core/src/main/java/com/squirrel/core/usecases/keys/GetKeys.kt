package com.squirrel.core.usecases.keys

import com.squirrel.core.data.KeysRepository

class GetKeys(private val keysRepository: KeysRepository) {
	suspend operator fun invoke(path: String, password: String) =
		keysRepository.read(path, password)
}