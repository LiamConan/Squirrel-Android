package com.layne.squirrel.presentation.main

import androidx.lifecycle.ViewModel
import com.layne.squirrel.core.domain.*
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.interactor.KeysInteractor
import com.layne.squirrel.framework.interactor.PreferencesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
	private var dataHolder: DataHolder
) : ViewModel(), CoroutineScope by MainScope() {

	@Inject
	lateinit var keysInteractor: KeysInteractor

	@Inject
	lateinit var preferencesInteractor: PreferencesInteractor

	var data: Data
		get() = dataHolder.data
		set(value) {
			dataHolder.data = value
			saveData()
		}
	var password: String
		get() = dataHolder.password
		set(value) {
			dataHolder.password = value
		}
	private var preferences: FilePreferences? = null

	init {
		Squirrel.dagger.inject(this)
		launch {
			preferences = preferencesInteractor.getFilePreferences(dataHolder.uri)
		}
	}

	private fun saveData() {
		launch {
			keysInteractor.saveKeys(dataHolder.data, dataHolder.uri, dataHolder.password)
		}
	}

	fun changePassword(newPassword: String) {
		dataHolder.password = newPassword
		saveData()
	}

	fun isBiometricsEnabled(): Boolean = preferences?.biometricsSaved ?: false

	fun enableBiometrics() {
		preferences?.password = dataHolder.password
		preferences?.passwordSaved = true
		preferences?.biometricsSaved = true
		launch {
			preferences?.let {
				preferencesInteractor.setFilePreferences(it)
			}
		}
	}

	fun isNeededToAskForAutofill(block: () -> Unit) {
		launch {
			if (preferencesInteractor.getAutofillNeedAsk())
				block()
		}
	}

	fun addDirectory(directory: Directory, i: Int? = null): Data =
		keysInteractor.addDirectory(data, directory, i).also { data = it }

	fun renameDirectory(i: Int, title: String): Data =
		keysInteractor.renameDirectory(data, i, title).also { data = it }

	fun deleteDirectory(i: Int): Data =
		keysInteractor.deleteDirectory(data, i).also { data = it }

	fun swapDirectories(from: Int, to: Int) {
		data = keysInteractor.swapDirectories(data, from, to)
	}

	fun addAccount(directoryIndex: Int, account: Account, i: Int? = null) {
		data = keysInteractor.addAccount(data, account, directoryIndex, i)
	}

	fun updateAccount(directoryIndex: Int, accountIndex: Int, account: Account) {
		data = keysInteractor.updateAccount(data, directoryIndex, accountIndex, account)
	}

	fun swapAccounts(directoryIndex: Int, first: Int, second: Int) {
		data = keysInteractor.swapAccounts(data, directoryIndex, first, second)
	}

	fun deleteAccount(directoryIndex: Int, accountIndex: Int) =
		keysInteractor.deleteAccount(data, directoryIndex, accountIndex).also { data = it }

	fun deleteKey(dirIndex: Int, accountIndex: Int, keyIndex: Int) {
		data = keysInteractor.deleteKey(data, dirIndex, accountIndex, keyIndex)
	}

	fun rememberPassword() {
		preferences?.password = dataHolder.password
		preferences?.passwordSaved = true
		launch {
			preferences?.let {
				preferencesInteractor.setFilePreferences(it)
			}
		}
	}

	fun setAutofillJustAsked() {
		launch {
			preferencesInteractor.setAutofillLastAsked()
		}
	}

	fun setAutofillNeverAskAgain() {
		launch {
			preferencesInteractor.setNeverAskAgainAutofill(true)
		}
	}

	fun disableBiometrics() {
		preferences?.biometricsSaved = false
		launch {
			preferences?.let {
				preferencesInteractor.setFilePreferences(it)
			}
		}
	}

	fun generatePassword(
		n: Int,
		lower: Boolean,
		upper: Boolean,
		numbers: Boolean,
		spaces: Boolean,
		specials: Boolean
	): String = keysInteractor.generatePassword(
		n,
		lower,
		upper,
		numbers,
		spaces,
		specials
	)
}