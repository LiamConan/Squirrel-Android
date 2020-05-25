package com.layne.squirrel.framework.di

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import com.layne.squirrel.core.domain.DataHolder
import com.layne.squirrel.framework.Squirrel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val application: Application) {

	@Singleton
	@Provides
	fun getApplication(): Application = application

	@Singleton
	@Provides
	fun getContext(): Context = application.applicationContext

	@Singleton
	@Provides
	fun getDataHolder(): DataHolder = (application as Squirrel).dataHolder
}