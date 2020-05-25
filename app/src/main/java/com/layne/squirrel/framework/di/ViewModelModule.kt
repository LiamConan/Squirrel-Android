package com.layne.squirrel.framework.di

import androidx.lifecycle.ViewModel
import com.layne.squirrel.presentation.creditcards.CreditCardViewModel
import com.layne.squirrel.presentation.login.LoginViewModel
import com.layne.squirrel.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
	@Binds
	@IntoMap
	@ViewModelKey(LoginViewModel::class)
	abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(MainViewModel::class)
	abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(CreditCardViewModel::class)
	abstract fun bindCreditCardViewModel(viewModel: CreditCardViewModel): ViewModel
}