package com.layne.squirrel.framework.di

import android.app.Application
import com.layne.squirrel.core.data.KeysRepository
import com.layne.squirrel.core.data.PreferencesRepository
import com.layne.squirrel.framework.gateway.KeysLocalFileDataSource
import com.layne.squirrel.framework.gateway.SharedPreferencesDataSource
import com.layne.squirrel.framework.interactor.KeysInteractor
import com.layne.squirrel.framework.interactor.PreferencesInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule(private val application: Application) {

	@Singleton
	@Provides
	fun getKeysInteractor(): KeysInteractor {
		return KeysInteractor(KeysRepository(KeysLocalFileDataSource(application)))
	}

	@Singleton
	@Provides
	fun getPreferencesInteractor(): PreferencesInteractor {
		return PreferencesInteractor(PreferencesRepository(SharedPreferencesDataSource(application)))
	}
}