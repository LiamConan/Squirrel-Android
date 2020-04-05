package com.layne.squirrel.framework

import android.app.Application
import android.content.Context
import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.data.LastFilePathRepository
import com.layne.squirrel.core.data.PasswordRepository
import com.layne.squirrel.core.usecases.keys.*
import com.layne.squirrel.core.usecases.lastfilepath.GetLastFilePath
import com.layne.squirrel.core.usecases.lastfilepath.SaveLastFilePath
import com.layne.squirrel.core.usecases.password.*
import com.layne.squirrel.framework.gateway.KeysLocalFileDataSource
import com.layne.squirrel.framework.gateway.PreferencesLastFilePathDataSource
import com.layne.squirrel.framework.gateway.PreferencesPasswordDataSource
import com.layne.squirrel.presentation.login.LoginViewModel
import com.layne.squirrel.presentation.main.MainViewModel
import com.layne.squirrel.presentation.main.directories.dialog.CancelBiometricsDialog
import com.layne.squirrel.presentation.main.keys.PasswordGeneratorDialog
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, UseCasesModule::class])
interface Component {
	fun inject(loginViewModel: LoginViewModel)
	fun inject(mainViewModel: MainViewModel)

	fun inject(cancelBiometricsDialog: CancelBiometricsDialog)
	fun inject(passwordGeneratorDialog: PasswordGeneratorDialog)
}

@Module
class ContextModule(private val application: Application) {

	@Singleton
	@Provides
	fun getApplication(): Application = application

	@Singleton
	@Provides
	fun getContext(): Context = application.applicationContext
}

@Module
class UseCasesModule(private val application: Application) {

	@Singleton
	@Provides
	fun getKeysUseCases(): KeysUseCases {
		val repository = KeysRepository(KeysLocalFileDataSource(application))

		return KeysUseCases(
			AddAccount(),
			AddDirectory(),
			DeleteAccount(),
			DeleteDirectory(),
			GetKeys(repository),
			RemitAccount(),
			RemitDirectory(),
			SaveKeys(repository),
			SwapAccounts(),
			SwapDirectories(),
			UpdateAccount()
		)
	}

	@Singleton
	@Provides
	fun getLastFilePathUseCases(): LastFileUseCases {
		val repository = LastFilePathRepository(PreferencesLastFilePathDataSource(application))

		return LastFileUseCases(
			GetLastFilePath(repository),
			SaveLastFilePath(repository)
		)
	}

	@Singleton
	@Provides
	fun getPasswordUseCases(): PasswordUseCases {
		val repository = PasswordRepository(PreferencesPasswordDataSource(application))

		return PasswordUseCases(
			CheckPasswordExists(repository),
			DeletePassword(repository),
			GeneratePassword(),
			GetPassword(repository),
			SavePassword(repository)
		)
	}
}