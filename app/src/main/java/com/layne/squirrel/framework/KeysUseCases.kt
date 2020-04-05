package com.layne.squirrel.framework

import com.layne.squirrel.core.usecases.keys.*

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