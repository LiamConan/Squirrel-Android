package com.squirrel.core.usecases.keys

import com.squirrel.core.data.KeysRepository
import com.squirrel.core.domain.Directory

class SaveKeys(private val keysRepository: KeysRepository) {
	suspend operator fun invoke(directories: List<Directory>, uri: String, password: String) {
		keysRepository.write(directories, uri, password)
	}
}