package com.layne.squirrel.framework

import android.app.Application
import android.content.Context
import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.data.PasswordRepository
import com.layne.squirrel.core.data.PreferencesRepository
import com.layne.squirrel.core.usecases.keys.*
import com.layne.squirrel.core.usecases.password.DeletePassword
import com.layne.squirrel.core.usecases.password.GeneratePassword
import com.layne.squirrel.core.usecases.password.GetPassword
import com.layne.squirrel.core.usecases.password.SavePassword
import com.layne.squirrel.core.usecases.preferences.autofill.GetAutofillNeedAsk
import com.layne.squirrel.core.usecases.preferences.autofill.SetAutofillLastAsked
import com.layne.squirrel.core.usecases.preferences.autofill.SetNeverAskAgainAutofill
import com.layne.squirrel.core.usecases.preferences.file.GetFilePreferences
import com.layne.squirrel.core.usecases.preferences.file.SetFilePreferences
import com.layne.squirrel.core.usecases.preferences.filepath.GetLastFilePath
import com.layne.squirrel.core.usecases.preferences.filepath.SaveLastFilePath
import com.layne.squirrel.framework.autofill.PasswordFillService
import com.layne.squirrel.framework.gateway.KeysLocalFileDataSource
import com.layne.squirrel.framework.gateway.PreferencesPasswordDataSource
import com.layne.squirrel.framework.gateway.SharedPreferencesDataSource
import com.layne.squirrel.framework.interactor.KeysInteractor
import com.layne.squirrel.framework.interactor.PreferencesInteractor
import com.layne.squirrel.presentation.login.LoginViewModel
import com.layne.squirrel.presentation.main.MainViewModel
import com.layne.squirrel.presentation.main.directories.dialog.CancelBiometricsDialog
import com.layne.squirrel.presentation.main.directories.dialog.UseAutofillServiceDialog
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
	fun inject(useAutofillServiceDialog: UseAutofillServiceDialog)

	fun inject(passwordFillService: PasswordFillService)
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
	fun getKeysInteractor(): KeysInteractor {
		val repository = KeysRepository(KeysLocalFileDataSource(application))

		return KeysInteractor(
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
			UpdateAccount(),
			SearchKeys(),
			GeneratePassword()
		)
	}

	@Singleton
	@Provides
	fun getPreferencesInteractor(): PreferencesInteractor {
		val repository = PreferencesRepository(SharedPreferencesDataSource(application))

		return PreferencesInteractor(
			GetLastFilePath(repository),
			SaveLastFilePath(repository),
			GetAutofillNeedAsk(repository),
			SetAutofillLastAsked(repository),
			SetNeverAskAgainAutofill(repository),
			GetFilePreferences(repository),
			SetFilePreferences(repository)
		)
	}
}