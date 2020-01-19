package com.squirrel.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squirrel.core.domain.Directory
import com.squirrel.framework.KeysUseCases
import com.squirrel.framework.LastFileUseCases
import com.squirrel.framework.PasswordUseCases
import com.squirrel.framework.Squirrel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel : ViewModel() {

	@Inject
	lateinit var keysUseCases: KeysUseCases
	@Inject
	lateinit var passwordUseCases: PasswordUseCases
	@Inject
	lateinit var lastFileUseCases: LastFileUseCases

	var newFile = false
	var selectedPath = MutableLiveData<String>("")

	init {
		Squirrel.dagger.inject(this)
		loadLastUsedPath()
	}

	fun hasRegisteredPassword(path: String, block: (Boolean) -> Unit) {
		GlobalScope.launch(context = Dispatchers.Main) {
			val password = passwordUseCases.checkPasswordExists(path)
			block(password)
		}
	}

	fun getRegisteredPassword(key: String, block: (String) -> Unit) {
		GlobalScope.launch {
			block(passwordUseCases.getPassword(key))
		}
	}

	private fun loadLastUsedPath() {
		GlobalScope.launch {
			selectedPath.postValue(lastFileUseCases.getLastFilePath())
		}
	}

	fun loadData(path: String?, password: String?, block: (MutableList<Directory>) -> Unit) {
		if (path != null && password != null) {
			GlobalScope.launch {
				keysUseCases.getKeys(path, password)?.toMutableList()?.let {
					lastFileUseCases.saveLastFilePath(path)
					block(it)
				}
			}
		}
	}
}