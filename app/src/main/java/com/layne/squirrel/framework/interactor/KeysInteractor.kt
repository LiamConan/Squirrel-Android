package com.layne.squirrel.framework.interactor

import com.layne.squirrel.core.usecases.keys.*
import com.layne.squirrel.core.usecases.password.GeneratePassword

class KeysInteractor(
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
	val updateAccount: UpdateAccount,
	val searchKeys: SearchKeys,
	val generatePassword: GeneratePassword
)