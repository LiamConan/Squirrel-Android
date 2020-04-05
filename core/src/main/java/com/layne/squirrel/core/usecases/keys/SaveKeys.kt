package com.layne.squirrel.core.usecases.keys

import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.domain.Directory

class SaveKeys(private val keysRepository: KeysRepository) {
	suspend operator fun invoke(directories: List<Directory>, uri: String, password: String) {
		keysRepository.write(directories, uri, password)
	}
}