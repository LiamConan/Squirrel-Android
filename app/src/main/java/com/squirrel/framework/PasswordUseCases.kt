package com.squirrel.framework

import com.squirrel.core.usecases.password.*

class PasswordUseCases(
	val checkPasswordExists: CheckPasswordExists,
	val deletePassword: DeletePassword,
	val generatePassword: GeneratePassword,
	val getPassword: GetPassword,
	val savePassword: SavePassword
)