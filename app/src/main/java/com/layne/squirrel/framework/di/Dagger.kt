package com.layne.squirrel.framework.di

import com.layne.squirrel.framework.autofill.PasswordFillService
import com.layne.squirrel.presentation.creditcards.CreditCardActivity
import com.layne.squirrel.presentation.creditcards.CreditCardFragment
import com.layne.squirrel.presentation.creditcards.CreditCardListFragment
import com.layne.squirrel.presentation.creditcards.CreditCardViewModel
import com.layne.squirrel.presentation.login.LoginFragment
import com.layne.squirrel.presentation.login.LoginViewModel
import com.layne.squirrel.presentation.main.MainActivity
import com.layne.squirrel.presentation.main.MainViewModel
import com.layne.squirrel.presentation.main.accounts.AccountListFragment
import com.layne.squirrel.presentation.main.directories.DirectoryListFragment
import com.layne.squirrel.presentation.main.directories.dialog.CancelBiometricsDialog
import com.layne.squirrel.presentation.main.directories.dialog.UseAutofillServiceDialog
import com.layne.squirrel.presentation.main.keys.KeyFragment
import com.layne.squirrel.presentation.main.keys.KeyListFragment
import com.layne.squirrel.presentation.main.keys.PasswordGeneratorDialog
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
	modules = [
		ContextModule::class,
		InteractorModule::class,
		ViewModelFactoryModule::class,
		ViewModelModule::class
	]
)
interface Component {
	fun inject(mainActivity: MainActivity)
	fun inject(creditCardActivity: CreditCardActivity)

	fun inject(loginFragment: LoginFragment)
	fun inject(directoryListFragment: DirectoryListFragment)
	fun inject(accountListFragment: AccountListFragment)
	fun inject(keyListFragment: KeyListFragment)
	fun inject(keyFragment: KeyFragment)
	fun inject(creditCardListFragment: CreditCardListFragment)
	fun inject(creditCardFragment: CreditCardFragment)

	fun inject(loginViewModel: LoginViewModel)
	fun inject(mainViewModel: MainViewModel)
	fun inject(creditCardViewModel: CreditCardViewModel)

	fun inject(cancelBiometricsDialog: CancelBiometricsDialog)
	fun inject(passwordGeneratorDialog: PasswordGeneratorDialog)
	fun inject(useAutofillServiceDialog: UseAutofillServiceDialog)

	fun inject(passwordFillService: PasswordFillService)
}