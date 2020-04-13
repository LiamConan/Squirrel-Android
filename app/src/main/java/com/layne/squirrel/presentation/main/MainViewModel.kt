package com.layne.squirrel.presentation.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.core.domain.FilePreferences
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.interactor.KeysInteractor
import com.layne.squirrel.framework.interactor.PreferencesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : ViewModel(), CoroutineScope by MainScope() {

	@Inject
	lateinit var keysInteractor: KeysInteractor

	@Inject
	lateinit var preferencesInteractor: PreferencesInteractor

	var uri: Uri? = null
	var password: String = ""
	private var preferences: FilePreferences? = null

	var data: MutableLiveData<MutableList<Directory>> = MutableLiveData()
	var currentKey: Int = -1
	private var onSubkeyDeleted: () -> Unit = {}
	private var onKeyDeleted: () -> Unit = {}

	init {
		Squirrel.dagger.inject(this)
		launch {
			preferences = preferencesInteractor.getFilePreferences(uri.toString())
		}
	}

	fun saveData() {
		launch {
			val list = data.value?.toList() ?: listOf()
			keysInteractor.saveKeys(list, uri.toString(), password)
		}
	}

	fun changePassword(newPassword: String) {
		password = newPassword
		saveData()
	}

	fun isBiometricsEnabled(): Boolean = preferences?.biometricsSaved ?: false

	fun enableBiometrics() {
		preferences?.password = password
		preferences?.passwordSaved = true
		preferences?.biometricsSaved = true
		launch {
			preferences?.let {
				preferencesInteractor.setFilePreferences(it)
			}
		}
	}

	fun isNeededToAskForAutofill(block: (Boolean) -> Unit) {
		launch {
			block(preferencesInteractor.getAutofillNeedAsk())
		}
	}

	fun setOnSubkeyDeleted(l: () -> Unit) {
		onSubkeyDeleted = l
	}

	fun setOnKeyDeleted(l: () -> Unit) {
		onKeyDeleted = l
	}

	fun deleteSubkey(dirIndex: Int, accountIndex: Int) {
		val size = data.value?.get(dirIndex)?.accounts?.get(accountIndex)?.keys?.size
			?: 0
		if (size > 1) {
			data.value?.get(dirIndex)?.accounts?.get(accountIndex)?.keys?.removeAt(currentKey)
			onSubkeyDeleted()
		} else {
			data.value?.get(dirIndex)?.accounts?.removeAt(accountIndex)
			onKeyDeleted()
		}
	}

	fun getAccounts(directoryPosition: Int): MutableList<Account> {
		if (directoryPosition == -1)
			return mutableListOf()
		return data.value?.get(directoryPosition)?.accounts ?: mutableListOf()
	}

	fun addAccount(directoryIndex: Int, account: Account) {
		keysInteractor.addAccount(
			data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
			account
		)
		data.value = data.value
	}

	fun updateAccount(directoryIndex: Int, accountIndex: Int, account: Account) {
		keysInteractor.updateAccount(
			data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
			accountIndex,
			account
		)
		data.value = data.value
	}

	fun swapAccounts(directoryIndex: Int, first: Int, second: Int) {
		keysInteractor.swapAccounts(
			data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
			first,
			second
		)
		saveData()
	}

	fun deleteAccount(directoryIndex: Int, accountIndex: Int) {
		keysInteractor.deleteAccount(
			data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
			accountIndex
		)
		data.value = data.value
	}

	fun remitAccount(directoryIndex: Int, accountIndex: Int, account: Account) {
		keysInteractor.remiteAccount(
			data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
			accountIndex,
			account
		)
		data.value = data.value
	}

	fun addDirectory(title: String) {
		keysInteractor.addDirectory(data.value ?: mutableListOf(), Directory(title))
		data.value = data.value
	}

	fun deleteDirectory(i: Int) {
		keysInteractor.deleteDirectory(data.value ?: mutableListOf(), i)
		data.value = data.value
	}

	fun remitDirectory(directory: Directory, i: Int) {
		data.value?.let {
			keysInteractor.remiteDirectory(it, i, directory)
		}
		data.value = data.value
	}

	fun swapDirectories(from: Int, to: Int) {
		keysInteractor.swapDirectories(data.value ?: mutableListOf(), from, to)
		saveData()
	}

	fun rememberPassword() {
		preferences?.password = password
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