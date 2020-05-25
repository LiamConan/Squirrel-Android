package com.layne.squirrel.framework.interactor

import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.usecases.data.GetKeys
import com.layne.squirrel.core.usecases.data.SaveKeys
import com.layne.squirrel.core.usecases.data.SearchKeys
import com.layne.squirrel.core.usecases.data.account.AddAccount
import com.layne.squirrel.core.usecases.data.account.DeleteAccount
import com.layne.squirrel.core.usecases.data.account.SwapAccounts
import com.layne.squirrel.core.usecases.data.account.UpdateAccount
import com.layne.squirrel.core.usecases.data.creditcard.AddCreditCard
import com.layne.squirrel.core.usecases.data.creditcard.DeleteCreditCard
import com.layne.squirrel.core.usecases.data.creditcard.SwapCreditCards
import com.layne.squirrel.core.usecases.data.creditcard.UpdateCreditCard
import com.layne.squirrel.core.usecases.data.directory.AddDirectory
import com.layne.squirrel.core.usecases.data.directory.DeleteDirectory
import com.layne.squirrel.core.usecases.data.directory.RenameDirectory
import com.layne.squirrel.core.usecases.data.directory.SwapDirectories
import com.layne.squirrel.core.usecases.data.key.DeleteKey
import com.layne.squirrel.core.usecases.password.GeneratePassword

class KeysInteractor(
	repository: KeysRepository,
	val addAccount: AddAccount = AddAccount(),
	val addCreditCard: AddCreditCard = AddCreditCard(),
	val addDirectory: AddDirectory = AddDirectory(),
	val deleteAccount: DeleteAccount = DeleteAccount(),
	val deleteCreditCard: DeleteCreditCard = DeleteCreditCard(),
	val deleteDirectory: DeleteDirectory = DeleteDirectory(),
	val deleteKey: DeleteKey = DeleteKey(),
	val getKeys: GetKeys = GetKeys(repository),
	val saveKeys: SaveKeys = SaveKeys(repository),
	val swapAccounts: SwapAccounts = SwapAccounts(),
	val swapCreditCards: SwapCreditCards = SwapCreditCards(),
	val swapDirectories: SwapDirectories = SwapDirectories(),
	val updateAccount: UpdateAccount = UpdateAccount(),
	val updateCreditCard: UpdateCreditCard = UpdateCreditCard(),
	val renameDirectory: RenameDirectory = RenameDirectory(),
	val searchKeys: SearchKeys = SearchKeys(),
	val generatePassword: GeneratePassword = GeneratePassword()
)