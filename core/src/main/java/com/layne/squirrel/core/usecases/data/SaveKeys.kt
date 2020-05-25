package com.layne.squirrel.core.usecases.data

import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.domain.Data

class SaveKeys(private val keysRepository: KeysRepository) {
	suspend operator fun invoke(directories: Data, uri: String, password: String) {
		keysRepository.write(directories, uri, password)
	}
}