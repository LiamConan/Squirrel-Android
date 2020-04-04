package com.squirrel.presentation.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squirrel.core.domain.Account
import com.squirrel.core.domain.Directory
import com.squirrel.framework.KeysUseCases
import com.squirrel.framework.PasswordUseCases
import com.squirrel.framework.Squirrel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var keysUseCases: KeysUseCases
    @Inject
    lateinit var passwordUseCases: PasswordUseCases

    var uri: Uri? = null
    var password: String = ""

    var data: MutableLiveData<MutableList<Directory>> = MutableLiveData()
    var currentKey: Int = -1
    private var onSubkeyDeleted: () -> Unit = {}
    private var onKeyDeleted: () -> Unit = {}

    init {
        Squirrel.dagger.inject(this)
    }

    fun saveData() {
        GlobalScope.launch {
            val list = data.value?.toList() ?: listOf()
            keysUseCases.saveKeys(list, uri.toString(), password)
        }
    }

    fun changePassword(newPassword: String) {
        password = newPassword
        saveData()
    }

    fun checkPasswordExists(key: String, block: (Boolean) -> Unit) {
        GlobalScope.launch {
            block(passwordUseCases.checkPasswordExists(key))
        }
    }

    fun savePassword(key: String, password: String) {
        GlobalScope.launch {
            passwordUseCases.savePassword(key, password)
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
        keysUseCases.addAccount(
            data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
            account
        )
        data.value = data.value
    }

    fun updateAccount(directoryIndex: Int, accountIndex: Int, account: Account) {
        keysUseCases.updateAccount(
            data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
            accountIndex,
            account
        )
        data.value = data.value
    }

    fun swapAccounts(directoryIndex: Int, first: Int, second: Int) {
        keysUseCases.swapAccounts(
            data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
            first,
            second
        )
        saveData()
    }

    fun deleteAccount(directoryIndex: Int, accountIndex: Int) {
        keysUseCases.deleteAccount(
            data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
            accountIndex
        )
        data.value = data.value
    }

    fun remitAccount(directoryIndex: Int, accountIndex: Int, account: Account) {
        keysUseCases.remiteAccount(
            data.value?.get(directoryIndex)?.accounts ?: mutableListOf(),
            accountIndex,
            account
        )
        data.value = data.value
    }

    fun addDirectory(title: String) {
        keysUseCases.addDirectory(data.value ?: mutableListOf(), Directory(title))
        data.value = data.value
    }

    fun deleteDirectory(i: Int) {
        keysUseCases.deleteDirectory(data.value ?: mutableListOf(), i)
        data.value = data.value
    }

    fun remitDirectory(directory: Directory, i: Int) {
        data.value?.let {
            keysUseCases.remiteDirectory(it, i, directory)
        }
        data.value = data.value
    }

    fun swapDirectories(from: Int, to: Int) {
        keysUseCases.swapDirectories(data.value ?: mutableListOf(), from, to)
        saveData()
    }
}