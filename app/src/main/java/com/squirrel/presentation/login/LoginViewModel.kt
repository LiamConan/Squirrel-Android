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
    var selectedPath = MutableLiveData("")
    var isPasswordSaved = false

    init {
        Squirrel.dagger.inject(this)
        loadLastUsedPath()
    }

    fun hasRegisteredPassword(path: String, block: (Boolean) -> Unit) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val isPasswordSaved = passwordUseCases.checkPasswordExists(path)
            block(isPasswordSaved)
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

    fun loadData(
        path: String?,
        password: String?,
        success: (MutableList<Directory>) -> Unit,
        fail: () -> Unit
    ) {
        if (path != null && password != null) {
            if (newFile) {
                newFile = false
                success(mutableListOf())
            } else {
                GlobalScope.launch {
                    try {
                        keysUseCases.getKeys(path, password)?.toMutableList()?.let {
                            lastFileUseCases.saveLastFilePath(path)
                            success(it)
                        }
                    } catch (e: Exception) {
                        fail()
                    }
                }
            }
        }
    }
}