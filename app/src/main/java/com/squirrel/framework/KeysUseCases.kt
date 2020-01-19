package com.squirrel.framework

import com.squirrel.core.usecases.keys.*
import com.squirrel.core.usecases.lastfilepath.GetLastFilePath
import com.squirrel.core.usecases.lastfilepath.SaveLastFilePath
import com.squirrel.core.usecases.password.CheckPasswordExists
import com.squirrel.core.usecases.password.GeneratePassword
import com.squirrel.core.usecases.password.GetPassword
import com.squirrel.core.usecases.password.SavePassword

class KeysUseCases(
	val addAccount: AddAccount,
	val addDirectory: AddDirectory,
	val deleteAccount: DeleteAccount,
	val deleteDirectory: DeleteDirectory,
	val getKeys: GetKeys,
	val remiteAccount: RemitAccount,
	val remiteDirectory: RemitDirectory,
	val saveKeys: SaveKeys,
	val swapAccounts: SwapAccounts,
	val swapDirectories: SwapDirectories,
	val updateAccount: UpdateAccount
)