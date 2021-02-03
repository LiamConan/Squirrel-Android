package com.layne.squirrel.presentation.main

import androidx.lifecycle.MutableLiveData
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

	val liveData: MutableLiveData<Data> = MutableLiveData()
	var data: Data
		get() = liveData.value!!
		set(value) {
			liveData.value = value
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
		liveData.value = dataHolder.data
	}

	private fun saveData() {
		launch { keysInteractor.saveKeys(dataHolder.data, dataHolder.uri, dataHolder.password) }
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

	fun addDirectory(directory: Directory, i: Int? = null) {
		data = keysInteractor.addDirectory(data, directory, i)
	}

	fun renameDirectory(i: Int, title: String) {
		data = keysInteractor.renameDirectory(data, i, title)
	}

	fun deleteDirectory(i: Int) {
		data = keysInteractor.deleteDirectory(data, i)
	}

	fun swapDirectories(from: Int, to: Int) {
		data = keysInteractor.swapDirectories(data, from, to)
	}

	fun addAccount(directoryIndex: Int, account: Account, insertIndex: Int? = null) {
		data = keysInteractor.addAccount(data, account, directoryIndex, insertIndex)
	}

	fun updateAccount(directoryIndex: Int, accountIndex: Int, account: Account) {
		data = keysInteractor.updateAccount(data, directoryIndex, accountIndex, account)
	}

	fun swapAccounts(directoryIndex: Int, first: Int, second: Int) {
		data = keysInteractor.swapAccounts(data, directoryIndex, first, second)
	}

	fun deleteAccount(directoryIndex: Int, accountIndex: Int) {
		data = keysInteractor.deleteAccount(data, directoryIndex, accountIndex)
	}

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