package com.layne.squirrel.presentation.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.core.domain.FilePreferences
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.interactor.KeysInteractor
import com.layne.squirrel.framework.interactor.PreferencesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel(), CoroutineScope by MainScope() {

	@Inject
	lateinit var keysInteractor: KeysInteractor

	@Inject
	lateinit var preferencesInteractor: PreferencesInteractor

	private var preferences: FilePreferences? = null
	private var newFile = false
	var selectedPath: MutableLiveData<String> = MutableLiveData()

	init {
		Squirrel.dagger.inject(this)

		launch {
			selectedPath.postValue(preferencesInteractor.getFilePath())
		}
	}

	fun openFile(path: String) {
		newFile = false
		selectedPath.value = path
	}

	fun createFile(path: String) {
		newFile = true
		selectedPath.value = path
	}

	fun getFilePreferences(): FilePreferences {
		if (preferences == null) {
			runBlocking {
				preferences = preferencesInteractor.getFilePreferences(selectedPath.value ?: "")
			}
		}
		return preferences ?: FilePreferences()
	}

	fun loadData(
		path: String?,
		password: String?,
		success: (Data) -> Unit,
		fail: () -> Unit
	) {
		if (path != null && password != null) {
			if (newFile) {
				newFile = false
				success(Data(mutableListOf(), mutableListOf()))
			} else {
				launch {
					try {
						keysInteractor.getKeys(path, password)?.let {
							preferencesInteractor.saveFilePath(path)
							success(it)
						}
					} catch (e: Exception) {
						Log.e("LoginViewModel", "loadData", e)
						fail()
					}
				}
			}
		}
	}
}